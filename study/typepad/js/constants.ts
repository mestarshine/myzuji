export let currentOriginWords:string[] = [];
// 默认文章
export const ARTICLE: { [key: string]: { name: string; value: string; content: string }}= {
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
