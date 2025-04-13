import {Records} from "./records.js";
import {DB_NAME, LOCAL_STORAGE_INDEXNAME, OBJECT_NAME} from "./constants.js";
import {Config} from "./config.js";

type DBOperationCallback<T = any> = (data?: T) => void;

export class DataBase {
    private db: IDBDatabase | null = null;
    private isReady = false;
    private pendingOperations: (() => void)[] = [];

    constructor(private config: Config,private record:Records) {
        this.initializeDB();
    }

    private initializeDB(): void {
        const request = indexedDB.open(DB_NAME);

        request.onsuccess = (event: Event) => {
            this.db = (event.target as IDBRequest).result;
            this.isReady = true;
            this.processPendingOperations();
        };

        request.onerror = (event: Event) => {
            console.error('Database initialization failed:', (event.target as IDBRequest).error);
        };

        request.onupgradeneeded = (event: IDBVersionChangeEvent) => {
            const db = (event.target as IDBRequest).result;
            if (!db.objectStoreNames.contains(OBJECT_NAME)) {
                const store = db.createObjectStore(OBJECT_NAME, { keyPath: 'id' });
                // 可根据需要创建索引
                // store.createIndex('speed_index', 'speed', { unique: false });
            }
        };
    }

    private ensureDBReady(callback: () => void): void {
        this.isReady ? callback() : this.pendingOperations.push(callback);
    }

    private processPendingOperations(): void {
        while (this.pendingOperations.length > 0) {
            const operation = this.pendingOperations.shift();
            operation?.();
        }
    }
    // 基础事务封装
    private async createTransaction(
        mode: IDBTransactionMode,
        callback: (store: IDBObjectStore) => IDBRequest
    ): Promise<any> {
        return new Promise((resolve, reject) => {
            this.ensureDBReady(() => {
                const transaction = this.db!.transaction(OBJECT_NAME, mode);
                const store = transaction.objectStore(OBJECT_NAME);
                const request = callback(store);

                request.onsuccess = (event: Event) => {
                    resolve((event.target as IDBRequest).result);
                };

                request.onerror = (event: Event) => {
                    reject((event.target as IDBRequest).error);
                };

                transaction.oncomplete = () => {
                    // 可添加事务完成后的通用处理
                };
            });
        });
    }
    // 数据插入（带类型校验）
    async insert(record: Records): Promise<void> {
        const data = this.validateRecord(record);
        await this.createTransaction('readwrite', (store) => {
            return store.add(data);
        });
        this.updateLocalStorageCounter(1);
    }

    // 批量数据获取（带分页支持）
    async fetchAll(options: {
        page?: number;
        pageSize?: number;
        direction?: IDBCursorDirection
    } = {}): Promise<Records[]> {
        return new Promise((resolve, reject) => {
            this.ensureDBReady(() => {
                const result: Records[] = [];
                const transaction = this.db!.transaction(OBJECT_NAME, 'readonly');
                const store = transaction.objectStore(OBJECT_NAME);
                const request = store.openCursor(IDBKeyRange.upperBound(this.record.id), options.direction || 'prev');

                let counter = 0;
                const { page = 1, pageSize = Infinity } = options;
                const skip = (page - 1) * pageSize;

                request.onsuccess = (event: Event) => {
                    const cursor = (event.target as IDBRequest<IDBCursorWithValue>).result;
                    if (cursor) {
                        if (counter >= skip && result.length < pageSize) {
                            result.push(this.createRecordFromCursor(cursor));
                        }
                        counter++;
                        cursor.continue();
                    } else {
                        resolve(result);
                    }
                };

                request.onerror = (event: Event) => {
                    reject((event.target as IDBRequest).error);
                };
            });
        });
    }

    // 带确认的删除操作
    async delete(id: number): Promise<void> {
        await this.createTransaction('readwrite', (store) => {
            return store.delete(id);
        });
        this.updateLocalStorageCounter(-1);
    }

    // 安全清除操作
    async clear(confirm: boolean = false): Promise<void> {
        if (!confirm) return;

        await this.createTransaction('readwrite', (store) => {
            return store.clear();
        });
        localStorage[LOCAL_STORAGE_INDEXNAME] = '0';
    }
    // UI 相关操作分离
    async insertWithUI(record: Records, updateCallback?: DBOperationCallback): Promise<void> {
        try {
            await this.insert(record);
            updateCallback?.(this.createUIElement(record));
        } catch (error) {
            console.error('Insert operation failed:', error);
            // 可触发全局错误处理
        }
    }

    private validateRecord(record: Records): any {
        // 实现数据校验逻辑
        if (!record.id || Number.isNaN(record.speed)) {
            throw new Error('Invalid record format');
        }
        return {
            id: record.id,
            speed: record.speed,
            codeLength: record.codeLength,
            hitRate: Math.min(Math.max(Number(record.hitRate), 0), 100),
            backspace: record.backspace,
            wordCount: record.wordCount,
            articleName: record.articleName,
            timeStart: record.timeStart,
            duration: Math.abs(record.duration),
            articleNameValue: this.config.articleNameValue,
        };
    }

    private createRecordFromCursor(cursor: IDBCursorWithValue): Records {
        // 实现从游标创建 Records 实例的逻辑
        console.log("实现从游标创建 Records 实例的逻辑:"+cursor.value)
        return new Records();
    }

    private createUIElement(record: Records): HTMLElement {
        const tr = document.createElement('tr');
        tr.innerHTML = record.getHtml();
        return tr;
    }

    private updateLocalStorageCounter(delta: number): void {
        const current = parseInt(localStorage[LOCAL_STORAGE_INDEXNAME] || '0', 10);
        localStorage[LOCAL_STORAGE_INDEXNAME] = Math.max(current + delta, 0).toString();
    }

}
