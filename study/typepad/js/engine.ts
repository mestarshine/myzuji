// 跟打器引擎
import {KeyCount} from "./keyCount";
import {ARTICLE, currentOriginWords} from "./constants";

export class Engine {
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
