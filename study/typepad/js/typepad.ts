class Count {
    az = 0;
    number = 0;
    ctrl = 0;
    shift = 0;
    meta = 0;
    alt = 0;
    function = 0;
    space = 0;
    backspace = 0;

    init() {
        this.az = 0;
        this.number = 0;
        this.ctrl = 0;
        this.shift = 0;
        this.meta = 0;
        this.alt = 0;
        this.function = 0;
        this.space = 0;
        this.backspace = 0;
    }
}

class Engine {
    isStarted = false;
    isPaused = false;
    timeStart; //ms
    duration; // ms
    handleRefresh;
    refreshRate = 1000; // ms

    start() {
        this.isStarted = true;
        this.timeStart = (new Date()).getTime();
        this.startRefresh();
    }

    startRefresh() {
        this.handleRefresh = setInterval(() => {
            let timeNow = (new Date()).getTime()
            this.duration = timeNow - this.timeStart;
            this.showTime();
        }, this.refreshRate)
    }

    stopRefresh() {
        clearInterval(this.handleRefresh);
    }

    showTime() {
        if (this.isStarted) {
            let secondAll = this.duration / 1000;
            let minute = Math.floor(secondAll / 60);
            let second = Math.floor(secondAll % 60);
            $('.minute').innerText = minute >= 10 ? minute : `0${minute}`;
            $('.second').innerText = second >= 10 ? second : `0${second}`;
        } else {
            $('.minute').innerText = '--';
            $('.second').innerText = '--';
        }
    }

    pause() {
        this.isPaused = true;
        this.stopRefresh()
    }

    resume() {
        this.timeStart = (new Date()).getTime() - this.duration;
        this.isPaused = false;
        this.startRefresh();

    }

    reset() {
        pad.value = ''
        count.init();
        updateCountInfo()
        this.isPaused = false;
        this.isStarted = false;
        this.stopRefresh();
        this.showTime();
    }

    shuffle() {
        shuffleWords();
    }

    compare() {
        let typedWords = pad.value;
        let arrayOrigin = currentWords.split('');
        let arrayTyped = typedWords.split('');
        let html = '';
        let lastCharacterIsCorrect = false; // 上一个字符是正确的
        let wordsCorrect = '';
        let wordsWrong = '';
        arrayTyped.forEach((current, index) => {
            let origin = arrayOrigin[index];
            let currentCharacterIsCorrect = current === origin;
            if (currentCharacterIsCorrect) {
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
        })
        let untypedString = currentWords.substring(arrayTyped.length)
        html = html.concat(untypedString)
        content.innerHTML = html;
    }
}

const ARTICLE = {
    gxbtujdc: '一地在要工上是中国同和的有人我主产不为这民了发以经',
}
const content = $('.content p');
const pad = $('#pad');
let count = new Count();
let engine = new Engine();
let currentWords = ARTICLE.gxbtujdc;
const REG = {
    az: /^[a-zA-Z]$/,
    space: /^ $/,
    backspace: /^Backspace$/,
    number: /\d/,
    shift: /^Shift$/,
    function: /^(Control|Alt|Meta|Shift|Tab)$/,
    meta: /^Meta$/,
    alt: /^Alt$/,
    ctrl: /^(Control|Alt|Meta|Shift)$/,
    delete: /^Delete$/,
}

function $(selector) {
    return document.querySelector(selector)
}

window.onload = () => {
    content.innerText = currentWords;
    updateCountInfo();

    pad.onblur = () => {
        if (engine.isStarted && !engine.isPaused) {
            engine.pause();
        }
    }

    pad.onfocus = () => {
        if (engine.isStarted && engine.isPaused) {
            engine.resume();
        }
    }

    // key pressed
    pad.onkeydown = (e) => {
        if (e.key === 'Tab' || ((e.metaKey || e.ctrlKey) && e.key === 'r') || ((e.metaKey || e.ctrlKey) && e.key === 's')) {
            e.preventDefault();
        }
        if (REG.az.test(e.key) && !engine.isStarted) {
            engine.start()
        }
    }

    pad.onkeyup = (e) => {
        e.preventDefault();
        countKeys(e);
        engine.compare();
    }
}

function countKeys(e) {
    for (let type in count) {
        if (typeof (count[type]) !== 'function') {
            if (REG[type].test(e.key)) {
                count[type]++
            }
        }
    }

    updateCountInfo()
}

// Update infos
function updateCountInfo() {
    for (let type in count) {
        $(`.word-${type} p`).innerText = count[type];
    }
    $('.count-total').innerText = currentWords.length;
    $('.count-current').innerText = pad.value.length;
}

function shuffleWords() {
    let array = currentWords.split('');
    currentWords = shuffle(array).join('');
    content.innerText = currentWords;
    engine.reset();
}

/**
 * 数组乱序算法
 */
function shuffle(arr) {
    let length = arr.length, r = length, rand = 0;

    while (r) {
        rand = Math.floor(Math.random() * r--);
        [arr[r], arr[rand]] = [arr[rand], arr[r]];
    }
    return arr;
}
