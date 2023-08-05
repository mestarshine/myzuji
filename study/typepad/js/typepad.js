/**
 * Count 所有按键记录
 * Config 用户配置，字数、乱序与否
 * Engine 跟打器引擎，开始、结束、暂停
 * Record 每段的打字数据记录
 * Database IndexedDB 相关操作
 *
 */
const localStorageIndexName = 'type_pad_idb_index';
const untypedStringClassName = 'untyped-part';
const HEIGHT_TEMPLATE = 150; // 对照区高度

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

// 按键记录
class KeyCount {
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
    chapter;// 当前段号
    chapterTotal; // 总段数
    isShuffle; // 是否乱序模式
    count;// 单条数量
    articleName;// 文章名称
    article;// 文章内容
    darkMode// 暗黑模式
    localStorageLabel;// 文章类型

    constructor() {
        this.chapter = 1;
        this.chapterTotal = 1;
        this.isShuffle = false;
        this.count = 15;
        this.articleName = ARTICLE.top500.name;
        this.article = ARTICLE.top500.content;
        this.localStorageLabel = {
            chapter: 'type_pad_config_chapter',
            chapterTotal: 'type_pad_config_chapter_total',
            isShuffle: 'type_pad_config_is_shuffle',
            count: 'type_pad_config_count',
            articleName: 'type_pad_config_article_name',
            article: 'type_pad_config_article',
            darkMode: 'type_pad_config_dark_mode',
        }
    }

    // 判断是否存储过配置信息
    hasSavedData() {
        return Boolean(localStorage[this.localStorageLabel.articleName]);
    }

    save() {
        localStorage[this.localStorageLabel.chapter] = this.chapter;
        localStorage[this.localStorageLabel.chapterTotal] = this.chapterTotal;
        localStorage[this.localStorageLabel.isShuffle] = this.isShuffle;
        localStorage[this.localStorageLabel.count] = this.count;
        localStorage[this.localStorageLabel.articleName] = this.articleName;
        localStorage[this.localStorageLabel.article] = this.article;
        localStorage[this.localStorageLabel.darkMode] = this.darkMode;
    }

    get() {
        this.chapter = Number(localStorage[this.localStorageLabel.chapter]);
        this.chapterTotal = Number(localStorage[this.localStorageLabel.chapterTotal]);
        this.isShuffle = Boolean(localStorage[this.localStorageLabel.isShuffle] === 'true');
        this.count = Number(localStorage[this.localStorageLabel.count]);
        this.articleName = localStorage[this.localStorageLabel.articleName];
        this.article = localStorage[this.localStorageLabel.article];
        this.darkMode = Boolean(localStorage[this.localStorageLabel.darkMode] === 'true');
    }

    setWithCurrentConfig() {
        $('#mode').checked = this.isShuffle;
        let radios = document.querySelectorAll('input[name=count]');
        for (let i = 0; i < radios.length; i++) {
            radios[i]['checked'] = Number(radios[i]['value']) === this.count
        }
        $('#article').value = this.articleName;
        currentOriginWords = this.article.split('');

        let body = $('body');
        if (this.darkMode) {
            body.classList.add('black');
        } else {
            body.classList.remove('black');
        }

        let darkButton = $('#darkButton');
        darkButton.innerText = this.darkMode ? '白色' : '暗黑'
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
    refreshRate = 500; // ms

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
            $('.btn-minute').innerText = minute >= 10 ? minute : `0${minute}`;
            $('.second').innerText = second >= 10 ? second : `0${second}`;
            $('.btn-second').innerText = second >= 10 ? second : `0${second}`;
        } else {
            $('.minute').innerText = '00';
            $('.btn-minute').innerText = '00';
            $('.second').innerText = '00';
            $('.btn-second').innerText = '00';
        }
    }

    // 暂停
    pause() {
        this.isPaused = true;
        typingPad.blur();
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
        record = new Records();
        content.innerHTML = currentWords
        typingPad.value = ''
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
        this.isFinished = false;
        this.reset();
    }

    compare() {
        correctWordsCount = 0;
        let typedWords = typingPad.value;
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
        let untypedHtml = `<span class='${untypedStringClassName}'>${untypedString}</span>`;
        html = html.concat(untypedHtml)
        content.innerHTML = html;
        // 滚动对照区到当前所输入的位置
        let offsetTop = $('.' + untypedStringClassName).offsetTop;
        contentWrapper.scrollTo(0, offsetTop - HEIGHT_TEMPLATE / 2);
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
        record.articleName = config.articleName;
        this.updateCountInfo();
        dataBase.insert(record);
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
        $('.count-current').innerText = typingPad.value.length;


        // speed
        if (!engine.isStarted && !engine.isFinished) {
            $('.speed').innerText = '--';
            $('.btn-speed').innerText = '--';
            $('.count-key-rate').innerText = '--';
            $('.count-key-length').innerText = '--';
            $('.count-key-backspace').innerText = '--';
        } else {
            record.speed = (correctWordsCount / engine.duration * 1000 * 60).toFixed(2);
            $('.speed').innerText = record.speed;
            $('.btn-speed').innerText = record.speed;

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
        let articleNameValue = $('#article').value;
        let article = ARTICLE[articleNameValue];
        config.articleName = article.name;
        let content = ARTICLE[config.articleName].content;
        currentOriginWords = config.isShuffle ? shuffle(content.split('')) : content.split('');
        config.article = currentOriginWords.join('');
        this.changePerCount();
    }

    // 改变数字时
    changePerCount() {
        config.count = $('input[type=radio]:checked').value;
        if (config.count === 'ALL') {
            currentWords = currentOriginWords.join('');
        } else {
            currentWords = currentOriginWords.slice(0, Number(config.count)).join('');
        }
        config.chapter = 1;
        let originTol = currentOriginWords.length / Number(config.count);
        let tempTol = Math.floor(originTol);
        if (config.count === 'ALL') {
            config.chapterTotal = 1
        } else {
            config.chapterTotal = originTol > tempTol ? tempTol + 1 : tempTol;
        }
        config.save();
        this.reset();
    }

    // 切换乱序模式
    shuffleCurrentArticle() {
        config.isShuffle = $('#mode').checked;
        currentOriginWords = config.isShuffle ? shuffle(ARTICLE[config.articleName].content.split('')) : ARTICLE[config.articleName].content.split('');
        config.article = currentOriginWords.join('');
        currentWords = currentOriginWords.slice(0, Number(config.count)).join('');
        config.chapter = 1;
        config.save(); // save config
        this.reset();
    }

    // 上一段
    prevChapter() {
        if (config.chapter !== 1) {
            currentWords = currentOriginWords.slice(config.count * (config.chapter - 2), config.count * (config.chapter - 1)).join('');
            config.chapter--;
            engine.reset();
            config.save();
        }
    }

    // 下一段
    nextChapter() {
        if (config.chapter !== config.chapterTotal) {
            currentWords = currentOriginWords.slice(config.count * config.chapter, config.count * (config.chapter + 1)).join('');
            config.chapter++;
            engine.reset();
            config.save();
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
    articleName;
    timeStart;
    duration;

    constructor() {
        let index = localStorage[localStorageIndexName];
        this.id = index ? Number(index) : 1;
        localStorage[localStorageIndexName] = this.id;
        this.speed = 0;
        this.codeLength = 0;
        this.hitRate = 0;
        this.backspace = 0;
        this.wordCount = 0;
        this.articleName = '';
        this.timeStart = 0;
        this.duration = 0;
    }

    getHtml() {
        let level = Math.floor(this.speed / speedGap);
        level = level > 6 ? 6 : level;
        return `<tr>
              <td class="text-center">${this.id}</td>
              <td class="bold roboto-mono lv-${level}">${this.speed}</td>
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

    getHtmlWithCursor(cursor) {
        let level = Math.floor(cursor.value.speed / speedGap);
        level = level > 6 ? 6 : level;
        return `<tr>
              <td class="text-center">${cursor.key}</td>
              <td class="bold roboto-mono lv-${level}">${this.speed}</td>
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
                articleName: record.articleName,
                timeStart: record.timeStart,
                duration: record.duration,
            });
        request.onsuccess = e => {
            localStorage[localStorageIndexName] = Number(localStorage[localStorageIndexName]) + 1;
            // 插入最后的数据到顶部
            let tr = document.createElement('tr');
            tr.innerHTML = record.getHtml();
            let tbody = $('#grades');
            tbody.insertBefore(tr, tbody.firstChild);
            console.log(e);
        }

        request.onerror = e => {
            console.log(e);
        }
    }

    // 获取所有数据
    fetchAll() {
        let objectStore = DB.transaction([OBJECT_NAME], 'readwrite').objectStore(OBJECT_NAME);
        let html = '';
        objectStore.openCursor(IDBKeyRange.upperBound(record.id), "prev").onsuccess = e => {
            let cursor = e.target.result;
            if (cursor) {
                html = html + record.getHtmlWithCursor(cursor);
                $('#grades').innerHTML = html;
                cursor.continue(); // 移到下一个位置
            }
        }
    }

    // 删除一条数据
    delete(id, sender) {
        let objectStore = DB.transaction([OBJECT_NAME], 'readwrite').objectStore(OBJECT_NAME);
        objectStore.delete(id).onsuccess = e => {
            sender.parentElement.parentElement.remove();
            localStorage[localStorageIndexName] = Number(localStorage[localStorageIndexName]) - 1;
            this.fetchAll();
            console.log(e);
        }
    }

    clear(sender) {
        if (sender.innerText !== '确定清除') {
            sender.innerText = '确定清除';
            sender.classList.add('danger');
        } else {
            let objectStore = DB.transaction([OBJECT_NAME], 'readwrite').objectStore(OBJECT_NAME);
            let that = this;
            objectStore.clear().onsuccess = e => {
                localStorage[localStorageIndexName] = 1;
                that.fetchAll();
                location.reload();
                console.log(e);
            };
        }
    }

}

// 默认文章
const ARTICLE = {
    one: {
        name: '一级简码',
        value: 'one',
        content: '一地在要工上是中国同和的有人我主产不为这民了发以经'
    },
    two: {
        name: '二级简码',
        value: 'two',
        content: '五于天末开下理事画现麦珠表珍万玉平求来珲与击妻到互二土城霜域起进喜载南才垢协夫无裁增示赤过志地雪去盏三夺大厅左还百右奋面故原胡春克太磁耗矿达成顾碌友龙本村顶林模相查可楞贾格析棚机构术样档杰枕杨李根权楷七著其苛工牙划或苗黄攻区功共获芳蒋东蔗劳世节切芭药上歧非盯虑止旧占卤贞睡睥肯具餐眩瞳眇眯瞎卢眼皮此量时晨果暴申日蝇曙遇昨蝗明蛤晚景暗晃显晕电最归紧昆号叶顺呆呀中虽吕喂员吃听另只兄咬吖吵嘛喧叫啊啸吧哟车团因困羁四辊回田轴图斩男界罗较圈辘连思辄轨轻累峡周央岢曲由则迥崭山败刚骨内见丹赠峭赃迪岂邮峻幽生等知条长处得各备向笔稀务答物入科秒秋管乐秀很么第后质振打找年提损摆制手折摇失换护拉朱扩近气报热把指且脚须采毁用胆加舅觅胜貌月办胸脑脱膛脏边力服妥肥脂全会做体代个介保佃仙八风佣从你信位偿伙伫假他分公化印钱然钉错外旬名甸负儿铁解欠多久匀销炙锭饭迎争色锴请计诚订谋让刘就谓市放义衣六询方说诮变这记诎良充率着斗头亲并站间问单端道前准次门立冰普决闻兼痛北光法尖河江小温溃渐油少派肖没沟流洋水淡学泥池当汉涨业庄类灯度店烛燥烟庙庭煌粗府底广料应火迷断籽数序庇定守害宁宽官审宫军宙客宾农空冤社实宵灾之密字安它那导居怵展收慢避惭届必怕惟懈心习尿屡忱已敢恨怪惯卫际随阿陈耻阳职阵出降孤阴队隐及联孙耿院也子限取陛建寻姑杂媒肀旭如姻妯九婢退妗婚娘嫌录灵嫁刀好妇即姆马对参戏台观矣能难允叉巴邓艰又纯线顷缃红引费强细纲张缴组给约统弱纱继缩纪级绿经比'
    },
    top500: {
        name: '常用前500',
        value: 'top500',
        content: '的一是了不在有个人这上中大为来我到出要以时和地们得可下对生也子就过能他会多发说而于自之用年行家方后作成开面事好小心前所道法如进着同经分定都然与本还其当起动已两点从问里主实天高去现长此三将无国全文理明日些看只公等十意正外想间把情者没重相那向知因样学应又手但信关使种见力名二处门并口么先位头回话很再由身入内第平被给次别几月真立新通少机打水果最部何安接报声才体今合性西你放表目加常做己老四件解路更走比总金管光工结提任东原便美及教难世至气神山数利书代直色场变记张必受交非服化求风度太万各算边王什快许连五活思该步海指物则女或完马强言条特命感清带认保望转传儿制干计民白住字它义车像反象题却流且即深近形取往系量论告息让决未花收满每华业南觉电空眼听远师元请容她军士百办语期北林识半夫客战院城候单音台死视领失司亲始极双令改功程爱德复切随李员离轻观青足落叫根怎持精送众影八首包准兴红达早尽故房引火站似找备调断设格消拉照布友整术石展紧据终周式举飞片虽易运笑云建谈界务写钱商乐推注越千微若约英集示呢待坐议乎留称品志黑存六造低江念产刻节尔吃势依图共曾响底装具喜严九况跟罗须显热病证刚治绝群市阳确究久除闻答段官政类黄武七支费父统'
    },
    mid500: {
        name: '常用中500',
        value: 'mid500',
        content: '查般斯倒突号树拿克初广奇愿欢希母香破谁致线急古既句京甚仍晚争游龙余护另器细木权星哪苦孩试朝阿队居害独讲错局男差参社换选止际假汉够诉资密案史较环投静宝专修室区料帮衣竟模脸善兵考规联团冷玉施派纪采历顾春责夜画惊银负续吗简章左块索酒值态按陈河巴冲阵境助角户乱呼灵脚继楼景怕停铁异谢否伤兰置医良承福科属围需退基右速适药怀击买素背岁土忙充排价质遇端列印贵疑露哥杀标招血礼弟亮齐穿脑委州某顺省讨尚维板散项状追笔副层沙养读习永草胡济执察归富座雨堂威忽苏船罪敢妇村著食导免温莫掌激慢托胜险寻守波雷沉秀职验靠楚略族藏丽渐刘仅肯担扬盘唐钟级毛营坚松皮供店饭范哈赶吧雪斗效临农味恶烟园烈配杂短卫跳孙曲封抓移顿律卖艺旧朋救防脱翻划迎痛校窗宣乡杨叶警限湖软掉财词压挥超屋秋跑忘馆暗班党宗坏技困登姐预耳席梦朱组旁份禁套亚益探康增诗戏伯晓含劳恩顶君庄谓付田毕纸研虚怪宁替犯灯优您姓例丝盖误架幸隐股毒娘占智佛床米凡介征彩演射祖欲束获舞圣伙梅普借私源镇睡缓升纳织歌宫概野醒夏互积街牌休摇洋败监骨批兄刀网率庭熟创访硬仁菜丁绿牛避阴拍雄秘缺卷姑尼油恐玩释遍握球降虑荣策肉妈迷检伸欧攻练育危厅啊睛摆茶勇判材抱亦妻吸喝趣嘴逐操午吉浪轮默毫冰珠'
    },
    tail500: {
        name: '常用后500',
        value: 'tail500',
        content: '鼓阶孔徐固偏陆诸遗爷述帝闭补编巨透弄尤鲁拥录吴墙货弱敌挑宽迹抽忍折输稳皇桌献蒙纷麻洗评挂童尊舍唯博剧乃混弹附迟敬杯鱼控塞剑厚佳测训牙洞淡盛县芳雅革款横累择乘刺载猛逃构赵杜庆途奔虎巧抗针徒圆闪谷绍聚额健诚鲜泪闲均序震仿缘戴婚篇亡奶忠烦赛闹协杰残懂丹柳妹映桥叹愈旅授享暴偷蓝氏寒宜弃丰延辈抢颜赞典冒眉烧厂唱径库川辞伴怒型纯贝票隔穷拜审伦悲柔启减页纵扫伟迫振瑞丈梁洲枪央触予孤缩洛损促番罢宋奋销幕犹锁珍抬陪妙摸峰劲镜沈夺昨哭讯貌谋泰侧贫扶阻贴申岸彼赏版抵泽插迅凭伊潮咱仙符宇肩尝递燕洁拒郎凝净遭仪薄卡末勒乌森诺呀壮忧沿惯丢季企壁惜婆袋朗零辛忆努舒枝凤灭韩胆灰旦孟陷俗绕疾瞧洪甲帐糊泛皆碰吹码奉箱倾胸堆狂仲圈冬餐厉腿尖括佩鬼欣垂跃港骗融撞塔紫荡敏郑赖滑允鸟课暂瓦祥染滚浮粗刑辆狗扑稍秦扎魂岛腾臣琴悉络摩措域冠竹殊豪呆萨旋喊寄悄倍祝剩督旗返召彻宾甘吐乔腰拔幅违详臂尺饮颗涉逼竞培惠亏叔伏唤鸡邻池怨奥侯骑漫拖俊尾恨贯凌兼询碎晨罚铺浓伍宿泉井繁粉绪筑恢匹尘辉魔仰董描距盗渡勤劝莲坦搭挺踪幽截荒恰慧邦颇焦醉废掩签丧灾鼻侵陶肃裁俱磨析奖匆瓶泥拾凉麦钢涌潜隆津搞蛋奈扰耐傅锦播墨偶捕惑飘屈鸣挤毁斜啦污赤慰饰锋覆汤寿跨羊航'
    },
}
const speedGap = 30;// 速度阶梯，每增30新增一个颜色
const content = $('.content p');
const contentWrapper = $('.content');
const typingPad = $('#pad');
let currentWords = '';
let correctWordsCount = 0;
let currentOriginWords = [];
let count = new KeyCount();
let engine = new Engine();
let config = new Config();
let record = new Records();

// database
let DB;
const DBName = "TypePad";
let dataBase = new DataBase();
const OBJECT_NAME = 'TypingRecord';

function $(selector) {
    return document.querySelector(selector)
}

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

    currentWords = currentOriginWords.slice(config.count * (config.chapter - 1), config.count * (config.chapter)).join('');
    content.innerText = currentWords;
    engine.updateCountInfo();

    typingPad.onblur = () => {
        if (engine.isStarted && !engine.isPaused) {
            engine.pause();
        }
    }

    typingPad.onfocus = () => {
        if (engine.isStarted && engine.isPaused) {
            engine.resume();
        }
    }

    // INDEX DB
    let request = window.indexedDB.open(DBName);
    request.onsuccess = e => {
        if (e.returnValue) {
            DB = request.result;
            dataBase.fetchAll();
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
        DB.createObjectStore(OBJECT_NAME, {keyPath: 'id'});
        console.log(e);
    }

    /**
     * 按键过滤器
     * ⌘ + g: 重置
     * ⌘ + k: 乱序
     * ⌘ + u: 上一段
     * ⌘ + j: 下一段
     */
    typingPad.onkeydown = (e) => {
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
            engine.start()
        }
    }

    typingPad.onkeyup = (e) => {
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
    }
    typingPad.oninput = e => {
        if (!engine.isFinished && engine.isStarted) {
            engine.compare();
            // 末字时结束的时候
            if (typingPad.value.length >= currentWords.length) {
                if (typingPad.value === currentWords) {
                    engine.finish();
                }
            }
        } else if (!engine.isFinished) {
            engine.start()
        }
        console.log(e);
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

function switchDarkMode(sender) {
    let body = $('body');
    if (config.darkMode) {
        body.classList.remove('black');
        config.darkMode = false;
        sender.innerText = "暗黑"
        config.save();
    } else {
        body.classList.add('black');
        config.darkMode = true;
        sender.innerText = "白色"
        config.save();
    }
}

// 载入文章列表选项
function loadArticles() {
    let optionHtml = '';
    for (let itemName in ARTICLE) {
        let article = ARTICLE[itemName];
        let tempHtml = `<option value="${itemName}">${article.name}</option>`;
        optionHtml += tempHtml;
    }
    $('#article').innerHTML = optionHtml;
}
