import {Records} from "./records";

export class DataBase {
    // 添加数据
    insert(record: Records): void {
        const request = DB.transaction([OBJECT_NAME], 'readwrite')
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
                articleNameValue: config.articleNameValue,
            });

        request.onsuccess = (e: Event) => {
            localStorage[localStorageIndexName] = (Number(localStorage[localStorageIndexName]) + 1).toString();
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
        const objectStore = DB.transaction([OBJECT_NAME], 'readwrite').objectStore(OBJECT_NAME);
        let html = '';
        objectStore.openCursor(IDBKeyRange.upperBound(record.id), "prev").onsuccess = (e: Event) => {
            const cursor = (e.target as IDBRequest<IDBCursorWithValue>).result;
            if (cursor) {
                html = html + record.getHtmlWithCursor(cursor);
                const gradesElement = document.querySelector('#grades') as HTMLElement;
                if (gradesElement) gradesElement.innerHTML = html;
                cursor.continue(); // 移到下一个位置
            }
        };
    }

    // 删除一条数据
    delete(id: number, sender: HTMLElement): void {
        const objectStore = DB.transaction([OBJECT_NAME], 'readwrite').objectStore(OBJECT_NAME);
        objectStore.delete(id).onsuccess = (e: Event) => {
            sender.parentElement?.parentElement?.remove();
            localStorage[localStorageIndexName] = (Number(localStorage[localStorageIndexName]) - 1).toString();
            this.fetchAll();
            console.log(e);
        };
    }

    clear(sender: HTMLElement): void {
        if (sender.innerText !== '确定清除') {
            sender.innerText = '确定清除';
            sender.classList.add('danger');
        } else {
            const objectStore = DB.transaction([OBJECT_NAME], 'readwrite').objectStore(OBJECT_NAME);
            const that = this;
            objectStore.clear().onsuccess = (e: Event) => {
                localStorage[localStorageIndexName] = '1';
                that.fetchAll();
                location.reload();
                console.log(e);
            };
        }
    }
}
