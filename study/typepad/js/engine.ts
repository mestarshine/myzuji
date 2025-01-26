import { KeyCount } from './keyCount';
import { Records } from './records';
import { Config } from './config';
import { shuffle } from './utils';
import {ARTICLE, HEIGHT_TEMPLATE, UNTYPED_STRING_CLASSNAME} from './constants'
import {DataBase} from "./dataBase";

export class Engine {
    isFinished: boolean = false;
    isStarted: boolean = false;
    isPaused: boolean = false;
    timeStart: number = Date.now(); // ms
    timeEnd: number = Date.now(); // ms
    duration: number = 0; // ms
    handleRefresh: number | undefined;
    refreshRate: number = 500; // ms
    currentWords:string="";
    currentOriginWords:string[] = [];
    correctWordsCount:number = 0;

    constructor(private config: Config, private count: KeyCount, private record: Records,private dataBase:DataBase) {}

    init(content:HTMLElement|any):void{
        this.loadArticles();
        // 最开始的时候，如果没有检测到存储的数据，初始化
        if (this.config.hasSavedData()) {
            this.config.get();
            this.config.setWithCurrentConfig();
            this.currentOriginWords = this.config.article.split('');
        } else {
            this.config.save();
            this.config.get();
            this.config.setWithCurrentConfig();``
            this.currentOriginWords = this.config.article.split('');
            this.changePerCount();
        }

        this.currentWords = this.currentOriginWords.slice(this.config.count * (this.config.chapter - 1), this.config.count * this.config.chapter).join('');
        if (content) content.innerText = this.currentWords;
        this.updateCountInfo();
    }

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

    pause(): void {
        this.isPaused = true;
        const typingPad = document.querySelector('#pad') as HTMLTextAreaElement;
        if (typingPad) typingPad.blur();
        this.stopRefresh();
    }

    resume(): void {
        this.timeStart = (new Date()).getTime() - this.duration;
        this.isPaused = false;
        this.startRefresh();
    }

    reset(): void {
        this.record = new Records();
        const content = document.querySelector('.content p') as HTMLElement;
        if (content) content.innerHTML = this.currentWords;

        const typingPad = document.querySelector('#pad') as HTMLTextAreaElement;
        if (typingPad) typingPad.value = '';

        this.count.reset();
        this.updateCountInfo();
        this.isPaused = false;
        this.isStarted = false;
        this.isFinished = false;
        this.stopRefresh();
        this.showTime();
    }

    shuffle(): void {
        const array = this.currentWords.split('');
        this.currentWords = shuffle(array).join('');
        const content = document.querySelector('.content p') as HTMLElement;
        if (content) content.innerText = this.currentWords;
        this.isFinished = false;
        this.reset();
    }

    compare(): void {
        this.correctWordsCount = 0;
        const typingPad = document.querySelector('#pad') as HTMLTextAreaElement;
        const typedWords = typingPad ? typingPad.value : '';
        const arrayOrigin = this.currentWords.split('');
        const arrayTyped = typedWords.split('');
        let html = '';
        let lastCharacterIsCorrect = false; // 上一个字符是正确的
        let wordsCorrect = '';
        let wordsWrong = '';

        arrayTyped.forEach((current, index) => {
            const origin = arrayOrigin[index] || ' ';
            const currentCharacterIsCorrect = current === origin;
            if (currentCharacterIsCorrect) {
                this.correctWordsCount++;
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

        const untypedString = this.currentWords.substring(arrayTyped.length);
        const untypedHtml = `<span class='${UNTYPED_STRING_CLASSNAME}'>${untypedString}</span>`;
        html = html.concat(untypedHtml);
        const content = document.querySelector('.content p') as HTMLElement;
        if (content) content.innerHTML = html;

        // 滚动对照区到当前所输入的位置
        const untypedElement = document.querySelector(`.${UNTYPED_STRING_CLASSNAME}`) as HTMLElement;
        const contentWrapper = document.querySelector('.content') as HTMLElement;
        if (untypedElement && contentWrapper) {
            const offsetTop = untypedElement.offsetTop;
            contentWrapper.scrollTo(0, offsetTop - HEIGHT_TEMPLATE / 2);
        }
    }

    finish(): void {
        this.isStarted = false;
        this.isFinished = true;
        this.stopRefresh();
        this.timeEnd = (new Date()).getTime();
        this.duration = this.timeEnd - this.timeStart;

        // update record
        this.record.backspace = this.count.backspace;
        this.record.timeStart = this.timeStart;
        this.record.duration = this.duration;
        this.record.wordCount = this.currentWords.length;
        this.record.articleName = this.config.articleName;
        this.updateCountInfo();
        this.dataBase.insert(this.record);

        if (this.config.isAutoNext) {
            this.nextChapter();
        }
    }

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
        for (const type in this.count) {
            const element = document.querySelector(`.word-${type} p`) as HTMLElement;
            if (element) element.innerText = this.count[type as keyof KeyCount].toString();
        }

        const countTotalElement = document.querySelector('.count-total') as HTMLElement;
        if (countTotalElement) countTotalElement.innerText = this.currentWords.length.toString();

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
            this.record.speed = (this.correctWordsCount / this.duration * 1000 * 60).toFixed(2);
            if (speedElement) speedElement.innerText = this.record.speed;
            if (btnSpeedElement) btnSpeedElement.innerText = this.record.speed;

            const keyCount = this.count.all - this.count.function;
            this.record.hitRate = (keyCount / this.duration * 1000).toFixed(2);
            if (countKeyRateElement) countKeyRateElement.innerText = this.record.hitRate;

            // code length
            if (this.correctWordsCount) {
                this.record.codeLength = (keyCount / this.correctWordsCount).toFixed(2);
            } else {
                this.record.codeLength = '0';
            }
            if (countKeyLengthElement) countKeyLengthElement.innerText = this.record.codeLength;

            // backspace count
            if (countKeyBackspaceElement) countKeyBackspaceElement.innerText = this.count.backspace.toString();
        }

        const chapterCurrentElement = document.querySelector('.chapter-current') as HTMLElement;
        if (chapterCurrentElement) chapterCurrentElement.innerText = this.config.chapter.toString();

        const chapterTotalElement = document.querySelector('.chapter-total') as HTMLElement;
        if (chapterTotalElement) chapterTotalElement.innerText = this.config.chapterTotal.toString();
    }

    changeArticle(): void {
        const articleSelect = document.querySelector('#article') as HTMLSelectElement;
        if (!articleSelect) return;

        const articleNameValue = articleSelect.value;
        const article = ARTICLE[articleNameValue];
        if (!article) return;

        this.config.articleName = article.name;
        this.config.articleNameValue = articleNameValue;
        const content = ARTICLE[this.config.articleNameValue].content;
        this.currentOriginWords = this.config.isShuffle ? shuffle(content.split('')) : content.split('');
        this.config.article = this.currentOriginWords.join('');
        this.changePerCount();
    }

    changePerCount(): void {
        const countRadio = document.querySelector('input[type=radio]:checked') as HTMLInputElement;
        if (!countRadio) return;

        this.config.count = Number(countRadio.value);
        if (this.config.count === Number.MAX_SAFE_INTEGER) {
            this.currentWords = this.currentOriginWords.join('');
        } else {
            this.currentWords = this.currentOriginWords.slice(0, this.config.count).join('');
        }
        this.config.chapter = 1;
        const originTol = this.currentOriginWords.length / this.config.count;
        const tempTol = Math.floor(originTol);
        if (this.config.count === Number.MAX_SAFE_INTEGER) {
            this.config.chapterTotal = 1;
        } else {
            this.config.chapterTotal = originTol > tempTol ? tempTol + 1 : tempTol;
        }
        this.config.save();
        this.reset();
    }

    autoNext(): void {
        const autoNextCheckbox = document.querySelector('#autoNext') as HTMLInputElement;
        if (!autoNextCheckbox) return;

        this.config.isAutoNext = autoNextCheckbox.checked;
        this.config.save();
    }

    shuffleCurrentArticle(): void {
        const shuffleModeCheckbox = document.querySelector('#shuffleMode') as HTMLInputElement;
        if (!shuffleModeCheckbox) return;

        this.config.isShuffle = shuffleModeCheckbox.checked;
        this.currentOriginWords = this.config.isShuffle ? shuffle(ARTICLE[this.config.articleNameValue].content.split('')) : ARTICLE[this.config.articleNameValue].content.split('');
        this.config.article = this.currentOriginWords.join('');
        this.currentWords = this.currentOriginWords.slice(0, this.config.count).join('');
        this.config.chapter = 1;
        this.config.save(); // save config
        this.reset();
    }

    prevChapter(): void {
        if (this.config.chapter !== 1) {
            this.currentWords = this.currentOriginWords.slice(this.config.count * (this.config.chapter - 2), this.config.count * (this.config.chapter - 1)).join('');
            this.config.chapter--;
            this.reset();
            this.config.save();
        }
    }

    nextChapter(): void {
        if (this.config.chapter !== this.config.chapterTotal) {
            this.currentWords = this.currentOriginWords.slice(this.config.count * this.config.chapter, this.config.count * (this.config.chapter + 1)).join('');
            this.config.chapter++;
            this.reset();
            this.config.save();
        }
    }

    loadArticles(): void {
        let optionHtml = '';
        for (const itemName in ARTICLE) {
            const article = ARTICLE[itemName];
            const tempHtml = `<option value="${itemName}">${article.name}</option>`;
            optionHtml += tempHtml;
        }
        const articleSelect = document.querySelector('#article') as HTMLSelectElement;
        if (articleSelect) articleSelect.innerHTML = optionHtml;
    }
}
