/**
 * Count 所有按键记录
 * Config 用户配置，字数、乱序与否
 * Engine 跟打器引擎，开始、结束、暂停
 * Record 每段的打字数据记录
 * Database IndexedDB 相关操作
 *
 */
import {ARTICLE,currentOriginWords} from './constants'
import {Config} from './config';
import {KeyCount} from "./keyCount";
import {Engine} from "./engine";
import {Records} from "./records";
import {DataBase} from "./dataBase";

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
