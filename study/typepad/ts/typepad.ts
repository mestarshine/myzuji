/**
 * Count 所有按键记录
 * Config 用户配置，字数、乱序与否
 * Engine 跟打器引擎，开始、结束、暂停
 * Record 每段的打字数据记录
 * Database IndexedDB 相关操作
 *
 */
import {REG} from './constants.js'
import {Config} from './config.js';
import {KeyCount} from "./keyCount.js";
import {Engine} from "./engine.js";
import {Records} from "./records.js";
import {DataBase} from "./dataBase.js";

const content:HTMLElement|null = document.querySelector('.content p');
const typingPad:HTMLTextAreaElement|null = document.querySelector('#pad');
let currentWords:string = '';
// database
let count:KeyCount = new KeyCount();
let config:Config = new Config();
let record:Records = new Records();
let dataBase:DataBase = new DataBase(config,record);
let engine:Engine = new Engine(config,count,record,dataBase);

// 初始化
window.onload = () => {
    // 载入文章选项列表
    engine.init(content);

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
