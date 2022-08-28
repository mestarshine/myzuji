class Count {
    all = 0;
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
    }
}

class Engine {
    isFinished = false;
    isStarted = false;
    isPaused = false;
    timeStart; //ms
    timeEnd; // ms
    duration = 0; // ms
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
            this.updateCountInfo();
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
        content.innerHTML = currentWords
        pad.value = ''
        count.init();
        this.updateCountInfo();
        this.isPaused = false;
        this.isStarted = false;
        this.isFinished = false;
        this.stopRefresh();
        this.showTime();
    }

    shuffle() {
        let array = currentWords.split('');
        currentWords = shuffle(array).join('');
        content.innerText = currentWords;
        this.reset();
    }

    compare() {
        correctWordsCount = 0;
        let typedWords = pad.value;
        let arrayOrigin = currentWords.split('');
        let arrayTyped = typedWords.split('');
        let html = '';
        let lastCharacterIsCorrect = false; // 上一个字符是正确的
        let wordsCorrect = '';
        let wordsWrong = '';
        arrayTyped.forEach((current, index) => {
            let origin = arrayOrigin[index];
            origin = origin ? origin : ' ';
            let currentCharacterIsCorrect = current === origin;
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
        })
        let untypedString = currentWords.substring(arrayTyped.length)
        html = html.concat(untypedString)
        content.innerHTML = html;
    }

    finish() {
        this.isStarted = false;
        this.isFinished = true;
        this.stopRefresh();
        this.timeEnd = (new Date()).getTime();
        this.updateCountInfo();
    }

    updateCountInfo() {
        for (let type in count) {
            $(`.word-${type} p`).innerText = count[type];
        }
        $('.count-total').innerText = currentWords.length;
        $('.count-current').innerText = pad.value.length ? pad.value.length : '--';

        // speed
        if (!engine.isStarted && !engine.isFinished) {
            $('.speed').innerText = '--';
            $('.count-keyrate').innerText = '--';
        } else {
            $('.speed').innerText = (correctWordsCount / engine.duration * 1000 * 60).toFixed(2);

            let keyCount = count.all - count.function;
            $('.count-keyrate').innerText = (keyCount / engine.duration * 1000).toFixed(2);
        }
    }
}

const ARTICLE = {
    one: '一地在要工上是中国同和的有人我主产不为这民了发以经',
    two: '五于天末开下理事画现麦珠表珍万玉平求来珲与击妻到互二土城霜域起进喜载南才垢协夫无裁增示赤过志地雪去盏三夺大厅左还百右奋面故原胡春克太磁耗矿达成顾碌友龙本村顶林模相查可楞贾格析棚机构术样档杰枕杨李根权楷七著其苛工牙划或苗黄攻区功共获芳蒋东蔗劳世节切芭药上歧非盯虑止旧占卤贞睡睥肯具餐眩瞳眇眯瞎卢眼皮此量时晨果暴申日蝇曙遇昨蝗明蛤晚景暗晃显晕电最归紧昆号叶顺呆呀中虽吕喂员吃听另只兄咬吖吵嘛喧叫啊啸吧哟车团因困羁四辊回田轴图斩男界罗较圈辘连思辄轨轻累峡周央岢曲由则迥崭山败刚骨内见丹赠峭赃迪岂邮峻幽生等知条长处得各备向笔稀务答物入科秒秋管乐秀很么第后质振打找年提损摆制手折摇失换护拉朱扩近气报热把指且脚须采毁用胆加舅觅胜貌月办胸脑脱膛脏边力服妥肥脂全会做体代个介保佃仙八风佣从你信位偿伙伫假他分公化印钱然钉错外旬名甸负儿铁解欠多久匀销炙锭饭迎争色锴请计诚订谋让刘就谓市放义衣六询方说诮变这记诎良充率着斗头亲并站间问单端道前准次门立冰普决闻兼痛北光法尖河江小温溃渐油少派肖没沟流洋水淡学泥池当汉涨业庄类灯度店烛燥烟庙庭煌粗府底广料应火迷断籽数序庇定守害宁宽官审宫军宙客宾农空冤社实宵灾之密字安它那导居怵展收慢避惭届必怕惟懈心习尿屡忱已敢恨怪惯卫际随阿陈耻阳职阵出降孤阴队隐及联孙耿院也子限取陛建寻姑杂媒肀旭如姻妯九婢退妗婚娘嫌录灵嫁刀好妇即姆马对参戏台观矣能难允叉巴邓艰又纯线顷缃红引费强细纲张缴组给约统弱纱继缩纪级绿经比',
}
const content = $('.content p');
const pad = $('#pad');
let count = new Count();
let engine = new Engine();
let currentWords = ARTICLE.one;
let correctWordsCount = 0;
const REG = {
    all: /.*/,
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
    engine.updateCountInfo();

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


    /**
     * ⌘ + R: 重置
     * ⌘ + L: 乱序
     */
    pad.onkeydown = (e) => {
        if (e.key === 'Tab' || ((e.metaKey || e.ctrlKey) && (/[qwfg]/.test(e.key)))) {
            e.preventDefault();
        } else if ((e.metaKey || e.ctrlKey) && e.key === 'r') {
            e.preventDefault();
            engine.reset();
        } else if ((e.metaKey || e.ctrlKey) && e.key === 's') {
            e.preventDefault();
            engine.shuffle();
        } else if (REG.az.test(e.key) && !e.ctrlKey && !e.metaKey && !e.altKey && !engine.isStarted && !engine.isFinished) {
            engine.start()
        }
    }

    pad.onkeyup = (e) => {
        e.preventDefault();
        if (!engine.isFinished) {
            countKeys(e);
            engine.compare();
            // 末字时结束的时候
            if (pad.value.length >= currentWords.length) {
                if (pad.value === currentWords) {
                    engine.finish();
                }
            }
        }
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
}

// Update infos
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

function changeArticle() {
    let article = $('#article').value;
    switch (article) {
        case 'one':
            currentWords = ARTICLE.one;
            break;
        case 'two':
            currentWords = ARTICLE.two;
            break;
        default:
            break;
    }
    engine.reset();
    engine.updateCountInfo();
}
