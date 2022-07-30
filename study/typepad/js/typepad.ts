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
        this.backspace = 0;
        this.space = 0;
    }
}

function $(selector) {
    return document.querySelector(selector)
}

const ARTICLE = {
    gxbtujdc: '一地在要工上是中国同和的有人我主产不为这民了发以经',
}
const content = $('.content p');
const pad = $('#pad');
let count = new Count();
let currentWords = ARTICLE.gxbtujdc;

window.onload = () => {
    content.innerText = currentWords;
    pad.onkeydown = (e) => {
        console.log(e)
        if (e.key === 'Tab' || (e.metaKey && e.key === 'r')) {
            e.preventDefault();
        }
        countKeys(e);
    }
    pad.oninput = () => {
        console.log(pad.value)
    }
}
// REG
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
}

function resetCount() {
    pad.value = ''
    count.init();
    updateCountInfo()
}

function shuffleWords() {
    let array = currentWords.split('');
    currentWords = shuffle(array).join('');
    content.innerText = currentWords;
    resetCount();
}

/**
 * 数组乱序算法
 */
function shuffle(arr) {
    let length = arr.length,
        r = length,
        rand = 0;

    while (r) {
        rand = Math.floor(Math.random() * r--);
        [arr[r], arr[rand]] = [arr[rand], arr[r]];
    }
    return arr;
}
