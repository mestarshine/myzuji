/**
 * Count 所有按键记录
 * Config 用户配置，字数、乱序与否
 * Engine 跟打器引擎，开始、结束、暂停
 * Record 每段的打字数据记录
 * Database IndexedDB 相关操作
 *
 */
import {Config} from './config';
import {ARTICLE,currentOriginWords} from './constants'

const localStorageIndexName: string = 'type_pad_idb_index';
const untypedStringClassName: string = 'untyped-part';
const HEIGHT_TEMPLATE: number = 150; // 对照区高度

const REG: { [key: string]: RegExp } = {
    all: /.*/,
    az: /^[a-zA-Z]$/,
    number: /\d/,
    function: /^(Control|Alt|Meta|Shift|Tab)$/,
    ctrl: /^(Control|Alt|Meta|Shift)$/,
    shift: /^Shift$/,
    meta: /^Meta$/,
    alt: /^Alt$/,
    space: /^ $/,
    backspace: /^Backspace$/,
    delete: /^Delete$/,
    semicolon: /;/,
    quot: /'/,
};

// 按键记录
class KeyCount {
    all: number = 0;
    az: number = 0;
    number: number = 0;
    ctrl: number = 0;
    shift: number = 0;
    meta: number = 0;
    alt: number = 0;
    function: number = 0;
    space: number = 0;
    backspace: number = 0;
    semicolon: number = 0;
    quot: number = 0;

    reset(): void {
        this.all = 0;
        this.az = 0;
        this.number = 0;
        this.ctrl = 0;
        this.shift = 0;
        this.meta = 0;
        this.alt = 0;
        this.function = 0;
        this.space = 0;
        this.backspace = 0;
        this.semicolon = 0;
        this.quot = 0;
    }
}

// 跟打器引擎
class Engine {
    isFinished: boolean = false;
    isStarted: boolean = false;
    isPaused: boolean = false;
    timeStart: number = Date.now(); // ms
    timeEnd: number = Date.now(); // ms
    duration: number = 0; // ms
    handleRefresh: number | undefined;
    refreshRate: number = 500; // ms

    start(): void {
        this.isStarted = true;
        this.timeStart = (new Date()).getTime();
        this.startRefresh();
    }

    startRefresh(): void {
        this.handleRefresh = window.setInterval(() => {
            const timeNow = (new Date()).getTime();
            this.duration = timeNow - this.timeStart;
            this.updateCountInfo();
            this.showTime();
        }, this.refreshRate);
    }

    stopRefresh(): void {
        if (this.handleRefresh) {
            clearInterval(this.handleRefresh);
        }
    }

    showTime(): void {
        const minuteElement = document.querySelector('.minute') as HTMLElement;
        const btnMinuteElement = document.querySelector('.btn-minute') as HTMLElement;
        const secondElement = document.querySelector('.second') as HTMLElement;
        const btnSecondElement = document.querySelector('.btn-second') as HTMLElement;

        if (this.isStarted) {
            const secondAll = this.duration / 1000;
            const minute = Math.floor(secondAll / 60);
            const second = Math.floor(secondAll % 60);
            const minuteText = minute >= 10 ? minute.toString() : `0${minute}`;
            const secondText = second >= 10 ? second.toString() : `0${second}`;

            if (minuteElement) minuteElement.innerText = minuteText;
            if (btnMinuteElement) btnMinuteElement.innerText = minuteText;
            if (secondElement) secondElement.innerText = secondText;
            if (btnSecondElement) btnSecondElement.innerText = secondText;
        } else {
            if (minuteElement) minuteElement.innerText = '00';
            if (btnMinuteElement) btnMinuteElement.innerText = '00';
            if (secondElement) secondElement.innerText = '00';
            if (btnSecondElement) btnSecondElement.innerText = '00';
        }
    }

    // 暂停
    pause(): void {
        this.isPaused = true;
        const typingPad = document.querySelector('#pad') as HTMLTextAreaElement;
        if (typingPad) typingPad.blur();
        this.stopRefresh();
    }

    // 继续
    resume(): void {
        this.timeStart = (new Date()).getTime() - this.duration;
        this.isPaused = false;
        this.startRefresh();
    }

    // 重置计数器
    reset(): void {
        record = new Records();
        const content = document.querySelector('.content p') as HTMLElement;
        if (content) content.innerHTML = currentWords;

        const typingPad = document.querySelector('#pad') as HTMLTextAreaElement;
        if (typingPad) typingPad.value = '';

        count.reset();
        this.updateCountInfo();
        this.isPaused = false;
        this.isStarted = false;
        this.isFinished = false;
        this.stopRefresh();
        this.showTime();
    }

    // 乱序当前段
    shuffle(): void {
        const array = currentWords.split('');
        currentWords = shuffle(array).join('');
        const content = document.querySelector('.content p') as HTMLElement;
        if (content) content.innerText = currentWords;
        this.isFinished = false;
        this.reset();
    }

    compare(): void {
        correctWordsCount = 0;
        const typingPad = document.querySelector('#pad') as HTMLTextAreaElement;
        const typedWords = typingPad ? typingPad.value : '';
        const arrayOrigin = currentWords.split('');
        const arrayTyped = typedWords.split('');
        let html = '';
        let lastCharacterIsCorrect = false; // 上一个字符是正确的
        let wordsCorrect = '';
        let wordsWrong = '';

        arrayTyped.forEach((current, index) => {
            const origin = arrayOrigin[index] || ' ';
            const currentCharacterIsCorrect = current === origin;
            if (currentCharacterIsCorrect) {
                correctWordsCount++;
                wordsCorrect = wordsCorrect.concat(origin);
            } else {
                wordsWrong = wordsWrong.concat(origin);
            }

            if (wordsCorrect && !lastCharacterIsCorrect && index) {
                html = html.concat(`<span class="wrong">${wordsWrong}</span>`);
                wordsWrong = '';
            } else if (wordsWrong && lastCharacterIsCorrect && index) {
                html = html.concat(`<span class="correct">${wordsCorrect}</span>`);
                wordsCorrect = '';
            }
            if ((index + 1) === typedWords.length) {
                if (wordsCorrect) {
                    html = html.concat(`<span class="correct">${wordsCorrect}</span>`);
                } else {
                    html = html.concat(`<span class="wrong">${wordsWrong}</span>`);
                }
            }
            lastCharacterIsCorrect = current === origin;
        });

        const untypedString = currentWords.substring(arrayTyped.length);
        const untypedHtml = `<span class='${untypedStringClassName}'>${untypedString}</span>`;
        html = html.concat(untypedHtml);
        const content = document.querySelector('.content p') as HTMLElement;
        if (content) content.innerHTML = html;

        // 滚动对照区到当前所输入的位置
        const untypedElement = document.querySelector(`.${untypedStringClassName}`) as HTMLElement;
        const contentWrapper = document.querySelector('.content') as HTMLElement;
        if (untypedElement && contentWrapper) {
            const offsetTop = untypedElement.offsetTop;
            contentWrapper.scrollTo(0, offsetTop - HEIGHT_TEMPLATE / 2);
        }
    }

    // 当前段打完
    finish(): void {
        this.isStarted = false;
        this.isFinished = true;
        this.stopRefresh();
        this.timeEnd = (new Date()).getTime();
        this.duration = this.timeEnd - this.timeStart;

        // update record
        record.backspace = count.backspace;
        record.timeStart = this.timeStart;
        record.duration = this.duration;
        record.wordCount = currentWords.length;
        record.articleName = config.articleName;
        this.updateCountInfo();
        dataBase.insert(record);

        if (config.isAutoNext) {
            this.nextChapter();
        }
    }

    // 更新界面信息
    updateCountInfo(): void {
        const timeElement = document.querySelector('.time') as HTMLElement;
        if (timeElement) {
            if (this.isStarted && !this.isPaused) {
                timeElement.classList.add('text-green');
            } else {
                timeElement.classList.remove('text-green');
            }
        }

        // KEY COUNT
        for (const type in count) {
            const element = document.querySelector(`.word-${type} p`) as HTMLElement;
            if (element) element.innerText = count[type as keyof KeyCount].toString();
        }

        const countTotalElement = document.querySelector('.count-total') as HTMLElement;
        if (countTotalElement) countTotalElement.innerText = currentWords.length.toString();

        const countCurrentElement = document.querySelector('.count-current') as HTMLElement;
        const typingPad = document.querySelector('#pad') as HTMLTextAreaElement;
        if (countCurrentElement && typingPad) countCurrentElement.innerText = typingPad.value.length.toString();

        // speed
        const speedElement = document.querySelector('.speed') as HTMLElement;
        const btnSpeedElement = document.querySelector('.btn-speed') as HTMLElement;
        const countKeyRateElement = document.querySelector('.count-key-rate') as HTMLElement;
        const countKeyLengthElement = document.querySelector('.count-key-length') as HTMLElement;
        const countKeyBackspaceElement = document.querySelector('.count-key-backspace') as HTMLElement;

        if (!this.isStarted && !this.isFinished) {
            if (speedElement) speedElement.innerText = '--';
            if (btnSpeedElement) btnSpeedElement.innerText = '--';
            if (countKeyRateElement) countKeyRateElement.innerText = '--';
            if (countKeyLengthElement) countKeyLengthElement.innerText = '--';
            if (countKeyBackspaceElement) countKeyBackspaceElement.innerText = '--';
        } else {
            record.speed = (correctWordsCount / this.duration * 1000 * 60).toFixed(2);
            if (speedElement) speedElement.innerText = record.speed;
            if (btnSpeedElement) btnSpeedElement.innerText = record.speed;

            const keyCount = count.all - count.function;
            record.hitRate = (keyCount / this.duration * 1000).toFixed(2);
            if (countKeyRateElement) countKeyRateElement.innerText = record.hitRate;

            // code length
            if (correctWordsCount) {
                record.codeLength = (keyCount / correctWordsCount).toFixed(2);
            } else {
                record.codeLength = '0';
            }
            if (countKeyLengthElement) countKeyLengthElement.innerText = record.codeLength;

            // backspace count
            if (countKeyBackspaceElement) countKeyBackspaceElement.innerText = count.backspace.toString();
        }

        const chapterCurrentElement = document.querySelector('.chapter-current') as HTMLElement;
        if (chapterCurrentElement) chapterCurrentElement.innerText = config.chapter.toString();

        const chapterTotalElement = document.querySelector('.chapter-total') as HTMLElement;
        if (chapterTotalElement) chapterTotalElement.innerText = config.chapterTotal.toString();
    }

    changeArticle(): void {
        const articleSelect = document.querySelector('#article') as HTMLSelectElement;
        if (!articleSelect) return;

        const articleNameValue = articleSelect.value;
        const article = ARTICLE[articleNameValue];
        if (!article) return;

        config.articleName = article.name;
        config.articleNameValue = articleNameValue;
        const content = ARTICLE[config.articleNameValue].content;
        currentOriginWords = config.isShuffle ? shuffle(content.split('')) : content.split('');
        config.article = currentOriginWords.join('');
        this.changePerCount();
    }

    // 改变数字时
    changePerCount(): void {
        const countRadio = document.querySelector('input[type=radio]:checked') as HTMLInputElement;
        if (!countRadio) return;

        config.count = Number(countRadio.value);
        if (config.count === Number.MAX_SAFE_INTEGER) {
            currentWords = currentOriginWords.join('');
        } else {
            currentWords = currentOriginWords.slice(0, config.count).join('');
        }
        config.chapter = 1;
        const originTol = currentOriginWords.length / config.count;
        const tempTol = Math.floor(originTol);
        if (config.count === Number.MAX_SAFE_INTEGER) {
            config.chapterTotal = 1;
        } else {
            config.chapterTotal = originTol > tempTol ? tempTol + 1 : tempTol;
        }
        config.save();
        this.reset();
    }

    // 自动发文
    autoNext(): void {
        const autoNextCheckbox = document.querySelector('#autoNext') as HTMLInputElement;
        if (!autoNextCheckbox) return;

        config.isAutoNext = autoNextCheckbox.checked;
        config.save();
    }

    // 切换乱序模式
    shuffleCurrentArticle(): void {
        const shuffleModeCheckbox = document.querySelector('#shuffleMode') as HTMLInputElement;
        if (!shuffleModeCheckbox) return;

        config.isShuffle = shuffleModeCheckbox.checked;
        currentOriginWords = config.isShuffle ? shuffle(ARTICLE[config.articleNameValue].content.split('')) : ARTICLE[config.articleNameValue].content.split('');
        config.article = currentOriginWords.join('');
        currentWords = currentOriginWords.slice(0, config.count).join('');
        config.chapter = 1;
        config.save(); // save config
        this.reset();
    }

    // 上一段
    prevChapter(): void {
        if (config.chapter !== 1) {
            currentWords = currentOriginWords.slice(config.count * (config.chapter - 2), config.count * (config.chapter - 1)).join('');
            config.chapter--;
            this.reset();
            config.save();
        }
    }

    // 下一段
    nextChapter(): void {
        if (config.chapter !== config.chapterTotal) {
            currentWords = currentOriginWords.slice(config.count * config.chapter, config.count * (config.chapter + 1)).join('');
            config.chapter++;
            this.reset();
            config.save();
        }
    }
}

class Records {
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
        const index = localStorage[localStorageIndexName];
        this.id = index ? Number(index) : 1;
        localStorage[localStorageIndexName] = this.id.toString();
    }

    getHtml(): string {
        const level = Math.floor(Number(this.speed) / speedGap);
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
        const level = Math.floor(cursor.value.speed / speedGap);
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

class DataBase {
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

const speedGap:number = 30;// 速度阶梯，每增30新增一个颜色
const content:HTMLElement|null = document.querySelector('.content p');
const typingPad:HTMLTextAreaElement|null = document.querySelector('#pad');
let currentWords:string = '';
let correctWordsCount:number = 0;
let count:KeyCount = new KeyCount();
let engine:Engine = new Engine();
let config:Config = new Config();
let record:Records = new Records();

// database
let DB: IDBDatabase;
const DBName:string = "TypePad";
let dataBase:DataBase = new DataBase();
const OBJECT_NAME:string = 'TypingRecord';

// 初始化
window.onload = () => {
    // 载入文章选项列表
    loadArticles();
    // 最开始的时候，如果没有检测到存储的数据，初始化
    if (config.hasSavedData()) {
        config.get();
        config.setWithCurrentConfig();
    } else {
        config.save();
        config.get();
        config.setWithCurrentConfig();
        engine.changePerCount();
    }

    currentWords = currentOriginWords.slice(config.count * (config.chapter - 1), config.count * config.chapter).join('');
    if (content) content.innerText = currentWords;
    engine.updateCountInfo();

    if (typingPad) {
        typingPad.onblur = () => {
            if (engine.isStarted && !engine.isPaused) {
                engine.pause();
            }
        };

        typingPad.onfocus = () => {
            if (engine.isStarted && engine.isPaused) {
                engine.resume();
            }
        };

        // INDEX DB
        const request = window.indexedDB.open(DBName);
        request.onsuccess = (e: Event) => {
            if (!e.preventDefault) {
                return;
            }
            DB = request.result;
            dataBase.fetchAll();
        };

        request.onerror = (e: Event) => {
            console.log(e);
        };

        request.onupgradeneeded = () => {
            if (!DB) {
                DB = request.result;
            }
            DB.createObjectStore(OBJECT_NAME, { keyPath: 'id' });
        };

        /**
         * 按键过滤器
         * ⌘ + g: 重置
         * ⌘ + k: 乱序
         * ⌘ + u: 上一段
         * ⌘ + j: 下一段
         */
        typingPad.onkeydown = (e: KeyboardEvent) => {
            if (e.key === 'Tab' || ((e.metaKey || e.ctrlKey) && (/[nqwefgplt]/.test(e.key)))) {
                e.preventDefault();
            } else if ((e.metaKey || e.ctrlKey) && e.key === 'y') {
                e.preventDefault();
                engine.reset();
            } else if ((e.metaKey || e.ctrlKey) && e.key === 'k') {
                e.preventDefault();
                engine.shuffle();
            } else if ((e.metaKey || e.ctrlKey) && e.key === 'u') {
                engine.prevChapter();
                e.preventDefault();
            } else if ((e.metaKey || e.ctrlKey) && e.key === 'j') {
                engine.nextChapter();
                e.preventDefault();
            } else if (e.key === 'Escape') {
                engine.pause();
                e.preventDefault();
            } else if (REG.az.test(e.key) && !e.ctrlKey && !e.metaKey && !e.altKey && !engine.isStarted && !engine.isFinished) {
                engine.start();
            }
        };

        typingPad.onkeyup = (e: KeyboardEvent) => {
            e.preventDefault();
            if (!engine.isFinished) {
                countKeys(e);
                engine.compare();
                // 末字时结束的时候
                if (typingPad.value.length >= currentWords.length) {
                    if (typingPad.value === currentWords) {
                        engine.finish();
                    }
                }
            }
        };

        typingPad.oninput = () => {
            if (!engine.isFinished && engine.isStarted) {
                engine.compare();
                // 末字时结束的时候
                if (typingPad.value.length >= currentWords.length) {
                    if (typingPad.value === currentWords) {
                        engine.finish();
                    }
                }
            } else if (!engine.isFinished) {
                engine.start();
            }
        };
    }
}

function countKeys(e: KeyboardEvent): void {
    for (const type in count) {
        if (typeof (count[type as keyof KeyCount]) !== 'function') {
            if (REG[type as keyof typeof REG].test(e.key)) {
                count[type as keyof KeyCount]++;
            }
        }
    }
}

// Update infos
/**
 * 数组乱序算法
 */
function shuffle(arr: string[]): string[] {
    let length = arr.length, r = length, rand = 0;

    while (r) {
        rand = Math.floor(Math.random() * r--);
        [arr[r], arr[rand]] = [arr[rand], arr[r]];
    }
    return arr;
}

/**
 * 格式化时间，输出字符串
 *
 * @param   date    要格式化的时间
 * @param   formatString    返回时间的格式：
 * @return  格式化后的时间字符串
 * */
function dateFormatter(date: Date, formatString: string): string {
    formatString = formatString ? formatString : 'yyyy-MM-dd hh:mm:ss';
    const dateRegArray: { [key: string]: number | string } = {
        "M+": date.getMonth() + 1,                      // 月份
        "d+": date.getDate(),                           // 日
        "h+": date.getHours(),                          // 小时
        "m+": date.getMinutes(),                        // 分
        "s+": date.getSeconds(),                        // 秒
        "q+": Math.floor((date.getMonth() + 3) / 3),    // 季度
        "S": date.getMilliseconds()                     // 毫秒
    };
    if (/(y+)/.test(formatString)) {
        formatString = formatString.replace(RegExp.$1, (date.getFullYear() + "").substring(4 - RegExp.$1.length));
    }
    for (const section in dateRegArray) {
        if (new RegExp("(" + section + ")").test(formatString)) {
            const value = dateRegArray[section];
            formatString = formatString.replace(RegExp.$1, (RegExp.$1.length === 1) ? value.toString() : (("00" + dateRegArray[section]).substring(("" + dateRegArray[section]).length)));
        }
    }
    return formatString;
}

/**
 * @return：输出倒计时字符串 时时:分分:秒秒
 * @param timeLeft
 **/
function formatTimeLeft(timeLeft: number): string {
    timeLeft = Math.floor(timeLeft / 1000);
    const minus = Math.floor(timeLeft / 60);
    const seconds = timeLeft % 60;
    return `${minus.toString().padStart(2, '00')}:${seconds.toString().padStart(2, '00')}`;
}

function switchDarkMode(sender: HTMLElement): void {
    const body = document.querySelector('body');
    if (body) {
        if (config.darkMode) {
            body.classList.remove('black');
            config.darkMode = false;
            sender.innerText = "暗黑";
        } else {
            body.classList.add('black');
            config.darkMode = true;
            sender.innerText = "白色";
        }
        config.save();
    }
}

// 载入文章列表选项
function loadArticles(): void {
    let optionHtml = '';
    for (const itemName in ARTICLE) {
        const article = ARTICLE[itemName];
        const tempHtml = `<option value="${itemName}">${article.name}</option>`;
        optionHtml += tempHtml;
    }
    const articleSelect = document.querySelector('#article') as HTMLSelectElement;
    if (articleSelect) articleSelect.innerHTML = optionHtml;
}

function enterFullScreenMode(): void {
    if (document.documentElement.requestFullscreen) {
        document.documentElement.requestFullscreen().catch(err => {
            console.error('Error attempting to enable full-screen mode:', err);
        });
        const fullscreenButton = document.querySelector('#fullscreen') as HTMLElement;
        const fullscreenExitButton = document.querySelector('#fullscreen-exit') as HTMLElement;
        if (fullscreenButton) fullscreenButton.classList.add('hidden');
        if (fullscreenExitButton) fullscreenExitButton.classList.remove('hidden');
    }
}

function cancelFullscreen(): void {
    if (document.exitFullscreen) {
        document.exitFullscreen().catch(err => {
            console.error('退出全屏模式失败:', err);
        });
        const fullscreenButton = document.querySelector('#fullscreen') as HTMLElement;
        const fullscreenExitButton = document.querySelector('#fullscreen-exit') as HTMLElement;
        if (fullscreenButton) fullscreenButton.classList.remove('hidden');
        if (fullscreenExitButton) fullscreenExitButton.classList.add('hidden');
    } else {
        console.log('浏览器不支持全屏 API');
    }
}
