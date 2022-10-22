/**
 * Count 所有按键记录
 * Engine 主程序，开始结束暂停
 *
 */
const localStorageIndexName = 'TypePadIndex';
const REG = {
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
}

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
    semicolon = 0;
    quot = 0;

    reset() {
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

class Config {
    chapter = 1;
    chapterTotal = 1;
    isShuffle = false;
    count = 15;
    static localStorageLabel = {
        chapter: 'type_pad_config_chapter',
        chapterTotal: 'type_pad_config_chapter_total',
        isShuffle: 'type_pad_config_is_shuffle',
        count: 'type_pad_config_count',
        articleConfig: 'type_pad_config_article_option',

    }
    articleConfig = ARTICLE.one;

    constructor() {
        this.chapter = 1;
        this.chapterTotal = 1;
        this.isShuffle = false;
        this.count = 15;
        this.articleConfig = ARTICLE.one;
    }

    static hasSavedData() {
        return Boolean(localStorage[Config.localStorageLabel.articleConfig]);
    }

    save() {
        localStorage[Config.localStorageLabel.chapter] = this.chapter;
        localStorage[Config.localStorageLabel.chapterTotal] = this.chapterTotal;
        localStorage[Config.localStorageLabel.isShuffle] = this.isShuffle;
        localStorage[Config.localStorageLabel.count] = this.count;
        localStorage[Config.localStorageLabel.articleConfig] = this.articleConfig;

    }

    get() {
        this.chapter = localStorage[Config.localStorageLabel.chapter];
        this.chapterTotal = localStorage[Config.localStorageLabel.chapterTotal];
        this.isShuffle = Boolean(localStorage[Config.localStorageLabel.isShuffle] === 'true');
        this.count = Number(localStorage[Config.localStorageLabel.count]);
        this.articleConfig = localStorage[Config.localStorageLabel.articleConfig];

    }

    set() {
        $('#mode').checked = this.isShuffle;
        let radios = document.querySelectorAll('input[name=count]');
        for (let i = 0; i < radios.length; i++) {
            radios[i].checked = Number(radios[i].value) === this.count
        }

        let options = document.querySelectorAll('select option');
        for (let i = 0; i < options.length; i++) {
            options[i].checked = options[i].value === this.articleConfig
        }

    }
}

//跟打器引擎
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
            $('.minute').innerText = '00';
            $('.second').innerText = '00';
        }
    }

    // 暂停
    pause() {
        this.isPaused = true;
        this.stopRefresh()
    }

    // 继续
    resume() {
        this.timeStart = (new Date()).getTime() - this.duration;
        this.isPaused = false;
        this.startRefresh();

    }

    // 重置计数器
    reset() {
        record = new Records(0, 0, 0, 0, 0, 0, 0);
        content.innerHTML = currentWords
        pad.value = ''
        count.reset();
        this.updateCountInfo();
        this.isPaused = false;
        this.isStarted = false;
        this.isFinished = false;
        this.stopRefresh();
        this.showTime();
    }

    // 乱序当前段
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

    // 当前段打完
    finish() {
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
        this.updateCountInfo();
        data.insert(record);
    }

    // 更新界面信息
    updateCountInfo() {

        if (engine.isStarted && !engine.isPaused) {
            $('.time').classList.add('text-green');
        } else {
            $('.time').classList.remove('text-green');
        }

        // KEY COUNT
        for (let type in count) {
            $(`.word-${type} p`).innerText = count[type];
        }
        $('.count-total').innerText = currentWords.length;
        $('.count-current').innerText = pad.value.length;


        // speed
        if (!engine.isStarted && !engine.isFinished) {
            $('.speed').innerText = '--';
            $('.count-key-rate').innerText = '--';
            $('.count-key-length').innerText = '--';
            $('.count-key-backspace').innerText = '--';
        } else {
            record.speed = (correctWordsCount / engine.duration * 1000 * 60).toFixed(2);
            $('.speed').innerText = record.speed;

            let keyCount = count.all - count.function;
            record.hitRate = (keyCount / engine.duration * 1000).toFixed(2);
            $('.count-key-rate').innerText = record.hitRate;

            // code length
            if (correctWordsCount) {
                record.codeLength = (keyCount / correctWordsCount).toFixed(2);
            } else {
                record.codeLength = 0;
            }
            $('.count-key-length').innerText = record.codeLength;
            // backspace count
            $('.count-key-backspace').innerText = count.backspace;
        }
        $('.chapter-current').innerText = config.chapter;
        $('.chapter-total').innerText = config.chapterTotal;
    }

    changeArticle() {
        record = new Records(0, 0, 0, 0, 0, 0, 0);
        let articleConfig = $('#article').value;
        let isShuffle = $('#mode').checked;
        let radios = document.querySelectorAll('input[type=radio]');
        let perCount = 0;
        for (let i = 0; i < radios.length; i++) {
            if (radios[i].checked) {
                perCount = Number(radios[i].value);
            }
        }

        switch (articleConfig) {
            case 'one':
                currentOriginWords = isShuffle ? shuffle(ARTICLE.one.split('')) : ARTICLE.one.split('');
                currentWords = currentOriginWords.slice(0, Number(perCount)).join('');
                break;
            case 'two':
                currentOriginWords = isShuffle ? shuffle(ARTICLE.two.split('')) : ARTICLE.two.split('');
                currentWords = currentOriginWords.slice(0, Number(perCount)).join('');
                break;
            default:
                break;
        }

        config.chapter = 1;
        config.articleConfig = articleConfig;
        config.isShuffle = isShuffle;
        config.count = perCount;
        let originTol = currentOriginWords.length / config.count;
        let tempTol = Math.floor(originTol);
        config.chapterTotal = originTol > tempTol ? tempTol + 1 : tempTol;
        this.reset();
    }

    // 上一段
    prevChapter() {
        if (config.chapter !== 1) {
            currentWords = currentOriginWords.slice(config.count * (config.chapter - 2), config.count * (config.chapter - 1)).join('');
            config.chapter--;
            engine.reset();
        }
    }

    // 下一段
    nextChapter() {
        if (config.chapter !== config.chapterTotal) {
            currentWords = currentOriginWords.slice(config.count * config.chapter, config.count * (config.chapter + 1)).join('');
            config.chapter++;
            engine.reset();
        }
    }
}

class Records {
    id;
    speed;
    codeLength;
    hitRate;
    backspace;
    wordCount;
    timeStart;
    duration;

    constructor(speed, codeLength, hitRate, backspace, wordCount, timeStart, duration) {
        let index = localStorage[localStorageIndexName];
        this.id = index ? Number(index) : 1;
        localStorage[localStorageIndexName] = this.id;
        this.speed = speed;
        this.codeLength = codeLength;
        this.hitRate = hitRate;
        this.backspace = backspace;
        this.wordCount = wordCount;
        this.timeStart = timeStart;
        this.duration = duration;
    }

    getHtml() {
        return `<tr>
              <td class="text-center">${this.id}</td>
              <td class="bold">${this.speed}</td>
              <td>${this.codeLength}</td>
              <td>${this.hitRate}</td>
              <td>${this.backspace}</td>
              <td>${this.wordCount}</td>
              <td>${dateFormatter(new Date(this.timeStart), '')}</td>
              <td class="time">${formatTimeLeft(this.duration)}</td>
              <td><button class="btn btn-danger btn-sm" onclick="data.delete(${this.id},this)" type="button">删除</button></td>
            </tr>`;
    }

    getHtmlWithCursor(cursor) {
        return `<tr>
              <td class="text-center">${cursor.key}</td>
              <td class="bold">${cursor.value.speed}</td>
              <td>${cursor.value.codeLength}</td>
              <td>${cursor.value.hitRate}</td>
              <td>${cursor.value.backspace}</td>
              <td>${cursor.value.wordCount}</td>
              <td>${dateFormatter(new Date(cursor.value.timeStart), '')}</td>
              <td class="time">${formatTimeLeft(cursor.value.duration)}</td>
              <td><button class="btn btn-danger btn-sm" onclick="data.delete(${cursor.key},this)" type="button">删除</button></td>
            </tr>`;
    }
}

class DataBase {
    // 添加数据
    insert(record) {
        let request = DB.transaction([OBJECT_NAME], 'readwrite')
            .objectStore(OBJECT_NAME)
            .add({
                id: record.id,
                speed: record.speed,
                codeLength: record.codeLength,
                hitRate: record.hitRate,
                backspace: record.backspace,
                wordCount: record.wordCount,
                timeStart: record.timeStart,
                duration: record.duration,
            });
        request.onsuccess = e => {
            localStorage[localStorageIndexName] = Number(localStorage[localStorageIndexName]) + 1;
            // 插入最后的数据到顶部
            let tr = document.createElement('tr');
            tr.innerHTML = record.getHtml();
            let tbody = $('tbody');
            tbody.insertBefore(tr, tbody.firstChild);
        }

        request.onerror = e => {
            console.log(e);
        }
    }

    // 获取所有数据
    fetchAll() {
        let objectStore = DB.transaction([OBJECT_NAME], 'readwrite').objectStore(OBJECT_NAME);
        let html = '';
        let currentCursor = objectStore.openCursor(IDBKeyRange.upperBound(record.id), "prev").onsuccess = e => {
            let cursor = e.target.result;
            if (cursor) {
                html = html + record.getHtmlWithCursor(cursor);
                document.querySelector('tbody').innerHTML = html;
                cursor.continue(); // 移到下一个位置
            }
        }
    }

    // 删除一条数据
    delete(id, sender) {
        let objectStore = DB.transaction([OBJECT_NAME], 'readwrite').objectStore(OBJECT_NAME);
        objectStore.delete(id).onsuccess = e => {
            sender.parentElement.parentElement.remove();
            this.fetchAll();
        }
    }

    clear() {
        let objectStore = DB.transaction([OBJECT_NAME], 'readwrite').objectStore(OBJECT_NAME);
        objectStore.clear().onsuccess = e => {
            localStorage[localStorageIndexName] = 1;
            this.fetchAll();
        }
    }

}

// 默认文章
const ARTICLE = {
    one: '一地在要工上是中国同和的有人我主产不为这民了发以经',
    two: '五于天末开下理事画现麦珠表珍万玉平求来珲与击妻到互二土城霜域起进喜载南才垢协夫无裁增示赤过志地雪去盏三夺大厅左还百右奋面故原胡春克太磁耗矿达成顾碌友龙本村顶林模相查可楞贾格析棚机构术样档杰枕杨李根权楷七著其苛工牙划或苗黄攻区功共获芳蒋东蔗劳世节切芭药上歧非盯虑止旧占卤贞睡睥肯具餐眩瞳眇眯瞎卢眼皮此量时晨果暴申日蝇曙遇昨蝗明蛤晚景暗晃显晕电最归紧昆号叶顺呆呀中虽吕喂员吃听另只兄咬吖吵嘛喧叫啊啸吧哟车团因困羁四辊回田轴图斩男界罗较圈辘连思辄轨轻累峡周央岢曲由则迥崭山败刚骨内见丹赠峭赃迪岂邮峻幽生等知条长处得各备向笔稀务答物入科秒秋管乐秀很么第后质振打找年提损摆制手折摇失换护拉朱扩近气报热把指且脚须采毁用胆加舅觅胜貌月办胸脑脱膛脏边力服妥肥脂全会做体代个介保佃仙八风佣从你信位偿伙伫假他分公化印钱然钉错外旬名甸负儿铁解欠多久匀销炙锭饭迎争色锴请计诚订谋让刘就谓市放义衣六询方说诮变这记诎良充率着斗头亲并站间问单端道前准次门立冰普决闻兼痛北光法尖河江小温溃渐油少派肖没沟流洋水淡学泥池当汉涨业庄类灯度店烛燥烟庙庭煌粗府底广料应火迷断籽数序庇定守害宁宽官审宫军宙客宾农空冤社实宵灾之密字安它那导居怵展收慢避惭届必怕惟懈心习尿屡忱已敢恨怪惯卫际随阿陈耻阳职阵出降孤阴队隐及联孙耿院也子限取陛建寻姑杂媒肀旭如姻妯九婢退妗婚娘嫌录灵嫁刀好妇即姆马对参戏台观矣能难允叉巴邓艰又纯线顷缃红引费强细纲张缴组给约统弱纱继缩纪级绿经比',
}
const content = $('.content p');
const pad = $('#pad');
let count = new Count();
let engine = new Engine();
let config = new Config();
let currentWords = ARTICLE.one;
let correctWordsCount = 0;
let currentOriginWords = [];
let record = new Records(0, 0, 0, 0, 0, 0, 0);

// database
let DB;
const DBName = "TypePad";
let data;
const OBJECT_NAME = 'TypingRecord';

function $(selector) {
    return document.querySelector(selector)
}

if (Config.hasSavedData()) {
    config.get();
    config.set();
    engine.updateCountInfo();
}
// 初始化
window.onload = () => {
    engine.changeArticle();
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


    // INDEX DB
    data = new DataBase();
    let request = window.indexedDB.open(DBName);
    request.onsuccess = e => {
        if (e.returnValue) {
            DB = request.result;
            data.fetchAll();
        } else {
        }
    }

    request.onerror = e => {
        console.log(e);
    }

    request.onupgradeneeded = e => {
        if (DB) {
        } else {
            DB = request.result;
        }
        let objectStore = DB.createObjectStore(OBJECT_NAME, {keyPath: 'id'});
    }

    /**
     * 按键过滤器
     * ⌘ + r: 重置
     * ⌘ + l: 乱序
     * ⌘ + u: 上一段
     * ⌘ + j: 下一段
     */
    pad.onkeydown = (e) => {
        if (e.key === 'Tab' || ((e.metaKey || e.ctrlKey) && (/[nqewfgyplt]/.test(e.key)))) {
            e.preventDefault();
        } else if ((e.metaKey || e.ctrlKey) && e.key === 'r') {
            e.preventDefault();
            engine.reset();
        } else if ((e.metaKey || e.ctrlKey) && e.key === 'l') {
            e.preventDefault();
            engine.shuffle();
        } else if ((e.metaKey || e.ctrlKey) && e.key === 'u') {
            engine.prevChapter();
            e.preventDefault();
        } else if ((e.metaKey || e.ctrlKey) && e.key === 'j') {
            engine.nextChapter();
            e.preventDefault();
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

/**
 * 格式化时间，输出字符串
 *
 * @param   date    要格式化的时间
 * @param   formatString    返回时间的格式：
 * @return  格式化后的时间字符串
 * */
function dateFormatter(date, formatString) {
    formatString = formatString ? formatString : 'yyyy-MM-dd hh:mm:ss';
    let dateRegArray = {
        "M+": date.getMonth() + 1,                      // 月份
        "d+": date.getDate(),                           // 日
        "h+": date.getHours(),                          // 小时
        "m+": date.getMinutes(),                        // 分
        "s+": date.getSeconds(),                        // 秒
        "q+": Math.floor((date.getMonth() + 3) / 3),    // 季度
        "S": date.getMilliseconds()                     // 毫秒
    };
    if (/(y+)/.test(formatString)) {
        formatString = formatString.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (let section in dateRegArray) {
        if (new RegExp("(" + section + ")").test(formatString)) {
            formatString = formatString.replace(RegExp.$1, (RegExp.$1.length === 1) ? (dateRegArray[section]) : (("00" + dateRegArray[section]).substr(("" + dateRegArray[section]).length)));
        }
    }
    return formatString;
}


/**
 * @param：timeLeft 倒计时秒数
 * @return：输出倒计时字符串 时时:分分:秒秒
 **/
function formatTimeLeft(timeLeft) {
    timeLeft = Math.floor(timeLeft / 1000);
    let mins = Math.floor(timeLeft / 60);
    let seconds = timeLeft % 60;
    // util.toast(`时分秒：${hours}:${mins}:${seconds}`);
    return `${mins.toString().padStart(2, '00')}:${seconds.toString().padStart(2, '00')}`;
}
