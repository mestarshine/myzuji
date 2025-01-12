import {LOCAL_STORAGE_INDEXNAME, SPEED_GAP} from "./constants";
import {dateFormatter, formatTimeLeft} from "./utils";

export class Records {
    id: number;
    speed: string = '0';
    codeLength: string = '0';
    hitRate: string = '0';
    backspace: number = 0;
    wordCount: number = 0;
    articleName: string = '';
    timeStart: number = 0;
    duration: number = 0;

    constructor() {
        const index = localStorage[LOCAL_STORAGE_INDEXNAME];
        this.id = index ? Number(index) : 1;
        localStorage[LOCAL_STORAGE_INDEXNAME] = this.id.toString();
    }

    getHtml(): string {
        const level = Math.floor(Number(this.speed) / SPEED_GAP);
        const levelClass = level > 6 ? 6 : level;
        return `<tr>
              <td class="text-center">${this.id}</td>
              <td class="bold roboto-mono lv-${levelClass}">${this.speed}</td>
              <td>${this.codeLength}</td>
              <td>${this.hitRate}</td>
              <td>${this.backspace}</td>
              <td>${this.wordCount}</td>
              <td>${this.articleName}</td>
              <td class="hidden-sm">${dateFormatter(new Date(this.timeStart), '')}</td>
              <td class="time">${formatTimeLeft(this.duration)}</td>
              <td><button class="btn btn-danger btn-sm" onclick="dataBase.delete(${this.id},this)" type="button">删除</button></td>
            </tr>`;
    }

    getHtmlWithCursor(cursor: IDBCursorWithValue): string {
        const level = Math.floor(cursor.value.speed / SPEED_GAP);
        const levelClass = level > 6 ? 6 : level;
        return `<tr>
              <td class="text-center">${cursor.key}</td>
              <td class="bold roboto-mono lv-${levelClass}">${cursor.value.speed}</td>
              <td>${cursor.value.codeLength}</td>
              <td>${cursor.value.hitRate}</td>
              <td>${cursor.value.backspace}</td>
              <td>${cursor.value.wordCount}</td>
              <td>${cursor.value.articleName ? cursor.value.articleName : ''}</td>
              <td class="hidden-sm">${dateFormatter(new Date(cursor.value.timeStart), '')}</td>
              <td class="time">${formatTimeLeft(cursor.value.duration)}</td>
              <td><button class="btn btn-danger btn-sm" onclick="dataBase.delete(${cursor.key},this)" type="button">删除</button></td>
            </tr>`;
    }
}
