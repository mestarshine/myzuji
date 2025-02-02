import {Records} from "./records";
import {DB_NAME, LOCAL_STORAGE_INDEXNAME, OBJECT_NAME} from "./constants";
import {Config} from "./config";

export class DataBase {
    request = window.indexedDB.open(DB_NAME);
    DB: IDBDatabase=this.request.result;

    constructor(private config: Config,private record:Records) {}

    init():void {
        // INDEX DB
        this.request.onsuccess = (e: Event) => {
            if (!e.preventDefault) {
                return;
            }
            this.fetchAll();
        };

        this.request.onerror = (e: Event) => {
            console.log(e);
        };

        this.request.onupgradeneeded = () => {
            if (!this.DB) {
                this.DB = this.request.result;
            }
            this.DB.createObjectStore(OBJECT_NAME, { keyPath: 'id' });
        };
    }
    // 添加数据
    insert(record: Records): void {
        const request = this.DB.transaction([OBJECT_NAME], 'readwrite')
            .objectStore(OBJECT_NAME)
            .add({
                id: record.id,
                speed: record.speed,
                codeLength: record.codeLength,
                hitRate: record.hitRate,
                backspace: record.backspace,
                wordCount: record.wordCount,
                articleName: record.articleName,
                timeStart: record.timeStart,
                duration: record.duration,
                articleNameValue: this.config.articleNameValue,
            });

        request.onsuccess = (e: Event) => {
            localStorage[LOCAL_STORAGE_INDEXNAME] = (Number(localStorage[LOCAL_STORAGE_INDEXNAME]) + 1).toString();
            // 插入最后的数据到顶部
            const tr = document.createElement('tr');
            tr.innerHTML = record.getHtml();
            const tbody = document.querySelector('#grades') as HTMLElement;
            if (tbody) tbody.insertBefore(tr, tbody.firstChild);
            console.log(e);
        };

        request.onerror = (e: Event) => {
            console.log(e);
        };
    }

    // 获取所有数据
    fetchAll(): void {
        const objectStore = this.DB.transaction([OBJECT_NAME], 'readwrite').objectStore(OBJECT_NAME);
        let html = '';
        objectStore.openCursor(IDBKeyRange.upperBound(this.record.id), "prev").onsuccess = (e: Event) => {
            const cursor = (e.target as IDBRequest<IDBCursorWithValue>).result;
            if (cursor) {
                html = html + this.record.getHtmlWithCursor(cursor);
                const gradesElement = document.querySelector('#grades') as HTMLElement;
                if (gradesElement) gradesElement.innerHTML = html;
                cursor.continue(); // 移到下一个位置
            }
        };
    }

    // 删除一条数据
    delete(id: number, sender: HTMLElement): void {
        const objectStore = this.DB.transaction([OBJECT_NAME], 'readwrite').objectStore(OBJECT_NAME);
        objectStore.delete(id).onsuccess = (e: Event) => {
            sender.parentElement?.parentElement?.remove();
            localStorage[LOCAL_STORAGE_INDEXNAME] = (Number(localStorage[LOCAL_STORAGE_INDEXNAME]) - 1).toString();
            this.fetchAll();
            console.log(e);
        };
    }

    clear(sender: HTMLElement): void {
        if (sender.innerText !== '确定清除') {
            sender.innerText = '确定清除';
            sender.classList.add('danger');
        } else {
            const objectStore = this.DB.transaction([OBJECT_NAME], 'readwrite').objectStore(OBJECT_NAME);
            const that = this;
            objectStore.clear().onsuccess = (e: Event) => {
                localStorage[LOCAL_STORAGE_INDEXNAME] = '1';
                that.fetchAll();
                location.reload();
                console.log(e);
            };
        }
    }
}
