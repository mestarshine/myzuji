//package com.myzuji.breadth.repository;
//
//import com.gargoylesoftware.htmlunit.WebClient;
//import com.gargoylesoftware.htmlunit.html.HtmlPage;
//import com.gargoylesoftware.htmlunit.util.Cookie;
//import com.sun.org.apache.xml.internal.dtm.ref.DTMNodeList;
//import lombok.extern.slf4j.Slf4j;
//import org.htmlcleaner.CleanerProperties;
//import org.htmlcleaner.DomSerializer;
//import org.htmlcleaner.HtmlCleaner;
//import org.htmlcleaner.TagNode;
//import org.junit.jupiter.api.Test;
//import org.w3c.dom.Document;
//
//import javax.xml.xpath.XPath;
//import javax.xml.xpath.XPathConstants;
//import javax.xml.xpath.XPathFactory;
//import java.util.List;
//
//@Slf4j
//public class HttpTest {
//
//    static String xNode = " \n" +
//        "/**/\n" +
//        "\n" +
//        "tableData['tableData_1258'] ={\n" +
//        "staticDate:\"2021-04-09 19:00:01\",\n" +
//        "   isPageing:false,\n" +
//        "   header:[\n" +
//        "    [\"INDUSTRY\",\"行业名称\"],\n" +
//        "    [\"INDUSTRY_CODE\",\"行业代码\"],\n" +
//        "    [\"TX_NUM\",\"交易股票数<br/>(只)\"],\n" +
//        "    [\"MKT_VALUE\",\"市价总值<br/>(元)\"],\n" +
//        "    [\"AVG_PROFIT_RATE\",\"平均市盈率\"],\n" +
//        "    [\"AVG_PRICE\",\"平均价格<br/>(元)\"]\n" +
//        "   ],\n" +
//        "   list:[\n" +
//        "   '  ',\n" +
//        "    [\n" +
//        "    '<a href=\"/assortment/stock/areatrade/trade/detail.shtml?csrcCode=A\">农、林、牧、渔业</a>',\n" +
//        "    'A',\n" +
//        "    '15 ',\n" +
//        "    '97952374585',\n" +
//        "    '60.79',\n" +
//        "    '6.83'\n" +
//        "    ],\n" +
//        "   '',\n" +
//        "    [\n" +
//        "    '<a href=\"/assortment/stock/areatrade/trade/detail.shtml?csrcCode=B\">采矿业</a>',\n" +
//        "    'B',\n" +
//        "    '51 ',\n" +
//        "    '2590191925813',\n" +
//        "    '14.63',\n" +
//        "    '5.83'\n" +
//        "    ],\n" +
//        "   '',\n" +
//        "    [\n" +
//        "    '<a href=\"/assortment/stock/areatrade/trade/detail.shtml?csrcCode=C\">制造业</a>',\n" +
//        "    'C',\n" +
//        "    '1146 ',\n" +
//        "    '20699436125112',\n" +
//        "    '37.32',\n" +
//        "    '18.5'\n" +
//        "    ],\n" +
//        "   '',\n" +
//        "    [\n" +
//        "    '<a href=\"/assortment/stock/areatrade/trade/detail.shtml?csrcCode=D\">电力、热力、燃气及水生产和供应业</a>',\n" +
//        "    'D',\n" +
//        "    '76 ',\n" +
//        "    '1691256712202',\n" +
//        "    '21.18',\n" +
//        "    '6.39'\n" +
//        "    ],\n" +
//        "   '',\n" +
//        "    [\n" +
//        "    '<a href=\"/assortment/stock/areatrade/trade/detail.shtml?csrcCode=E\">建筑业</a>',\n" +
//        "    'E',\n" +
//        "    '48 ',\n" +
//        "    '982983656025',\n" +
//        "    '7.38',\n" +
//        "    '5.38'\n" +
//        "    ],\n" +
//        "   '',\n" +
//        "    [\n" +
//        "    '<a href=\"/assortment/stock/areatrade/trade/detail.shtml?csrcCode=F\">批发和零售业</a>',\n" +
//        "    'F',\n" +
//        "    '102 ',\n" +
//        "    '924118126976',\n" +
//        "    '19.05',\n" +
//        "    '8.52'\n" +
//        "    ],\n" +
//        "   '',\n" +
//        "    [\n" +
//        "    '<a href=\"/assortment/stock/areatrade/trade/detail.shtml?csrcCode=G\">交通运输、仓储和邮政业</a>',\n" +
//        "    'G',\n" +
//        "    '77 ',\n" +
//        "    '1879982107472',\n" +
//        "    '18.39',\n" +
//        "    '6.03'\n" +
//        "    ],\n" +
//        "   '',\n" +
//        "    [\n" +
//        "    '<a href=\"/assortment/stock/areatrade/trade/detail.shtml?csrcCode=H\">住宿和餐饮业</a>',\n" +
//        "    'H',\n" +
//        "    '5 ',\n" +
//        "    '85149054203',\n" +
//        "    '41.68',\n" +
//        "    '33.29'\n" +
//        "    ],\n" +
//        "   '',\n" +
//        "    [\n" +
//        "    '<a href=\"/assortment/stock/areatrade/trade/detail.shtml?csrcCode=I\">信息传输、软件和信息技术服务业</a>',\n" +
//        "    'I',\n" +
//        "    '112 ',\n" +
//        "    '1875660524478',\n" +
//        "    '46.84',\n" +
//        "    '16.99'\n" +
//        "    ],\n" +
//        "   '',\n" +
//        "    [\n" +
//        "    '<a href=\"/assortment/stock/areatrade/trade/detail.shtml?csrcCode=J\">金融业</a>',\n" +
//        "    'J',\n" +
//        "    '84 ',\n" +
//        "    '11557644929690',\n" +
//        "    '8.4',\n" +
//        "    '7.64'\n" +
//        "    ],\n" +
//        "   '',\n" +
//        "    [\n" +
//        "    '<a href=\"/assortment/stock/areatrade/trade/detail.shtml?csrcCode=K\">房地产业</a>',\n" +
//        "    'K',\n" +
//        "    '71 ',\n" +
//        "    '984565574475',\n" +
//        "    '7.44',\n" +
//        "    '7.04'\n" +
//        "    ],\n" +
//        "   '',\n" +
//        "    [\n" +
//        "    '<a href=\"/assortment/stock/areatrade/trade/detail.shtml?csrcCode=L\">租赁和商务服务业</a>',\n" +
//        "    'L',\n" +
//        "    '20 ',\n" +
//        "    '719245943278',\n" +
//        "    '52.74',\n" +
//        "    '33.45'\n" +
//        "    ],\n" +
//        "   '',\n" +
//        "    [\n" +
//        "    '<a href=\"/assortment/stock/areatrade/trade/detail.shtml?csrcCode=M\">科学研究和技术服务业</a>',\n" +
//        "    'M',\n" +
//        "    '23 ',\n" +
//        "    '480278450193',\n" +
//        "    '82.84',\n" +
//        "    '45.89'\n" +
//        "    ],\n" +
//        "   '',\n" +
//        "    [\n" +
//        "    '<a href=\"/assortment/stock/areatrade/trade/detail.shtml?csrcCode=N\">水利、环境和公共设施管理业</a>',\n" +
//        "    'N',\n" +
//        "    '29 ',\n" +
//        "    '175190433701',\n" +
//        "    '26.72',\n" +
//        "    '14.26'\n" +
//        "    ],\n" +
//        "   '',\n" +
//        "    [\n" +
//        "    '<a href=\"/assortment/stock/areatrade/trade/detail.shtml?csrcCode=O\">居民服务、修理和其他服务业</a>',\n" +
//        "    'O',\n" +
//        "    '0 ',\n" +
//        "    '0',\n" +
//        "    '-',\n" +
//        "    '-'\n" +
//        "    ],\n" +
//        "   '',\n" +
//        "    [\n" +
//        "    '<a href=\"/assortment/stock/areatrade/trade/detail.shtml?csrcCode=P\">教育</a>',\n" +
//        "    'P',\n" +
//        "    '3 ',\n" +
//        "    '16180654757',\n" +
//        "    '45.53',\n" +
//        "    '10.91'\n" +
//        "    ],\n" +
//        "   '',\n" +
//        "    [\n" +
//        "    '<a href=\"/assortment/stock/areatrade/trade/detail.shtml?csrcCode=Q\">卫生和社会工作</a>',\n" +
//        "    'Q',\n" +
//        "    '3 ',\n" +
//        "    '154685685747',\n" +
//        "    '170.43',\n" +
//        "    '85.7'\n" +
//        "    ],\n" +
//        "   '',\n" +
//        "    [\n" +
//        "    '<a href=\"/assortment/stock/areatrade/trade/detail.shtml?csrcCode=R\">文化、体育和娱乐业</a>',\n" +
//        "    'R',\n" +
//        "    '27 ',\n" +
//        "    '212618969421',\n" +
//        "    '14.72',\n" +
//        "    '7.71'\n" +
//        "    ],\n" +
//        "   '',\n" +
//        "    [\n" +
//        "    '<a href=\"/assortment/stock/areatrade/trade/detail.shtml?csrcCode=S\">综合</a>',\n" +
//        "    'S',\n" +
//        "    '9 ',\n" +
//        "    '46936933981',\n" +
//        "    '19.55',\n" +
//        "    '5.17'\n" +
//        "    ],\n" +
//        "   ''\n" +
//        "   ]\n" +
//        "  };\n" +
//        "/**/\n" +
//        "   ";
//    static String html = "<!DOCTYPE html>\n" +
//        "<html>\n" +
//        "<head>\n" +
//        "\t<meta charset=\"utf-8\">\n" +
//        "\t<title>上市公司地区/行业分类列表  |  上海证券交易所</title>\n" +
//        "\t<meta name=\"description\" content=\"\">\n" +
//        "\t<meta name=\"author\" content=\"上海证券交易所\">\n" +
//        "\t<meta name=\"renderer\" content=\"webkit\">\n" +
//        "\t<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\">\n" +
//        "\t<meta name=\"apple-mobile-web-app-capable\" content=\"yes\">\n" +
//        "\t<meta name=\"mobile-web-app-capable\" content=\"yes\">\n" +
//        "\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1, user-scalable=0\">\n" +
//        "\t<meta name=\"apple-mobile-web-app-title\" content=\"上海证券交易所\">\n" +
//        "\n" +
//        "            <script>var col_id=8536 ; var col_old_id=\"http://www.sse.com.cn/assortment/stock/statistic/areatrade/trade/\";  </script>\n" +
//        "\n" +
//        "\n" +
//        "\t<!-- CSS -->\n" +
//        "    <link href=\"/css/sse_allcss.min.css\" rel=\"stylesheet\" type=\"text/css\" charset=\"utf-8\" >\n" +
//        "<link href=\"/css/sse_allcss2.min.css\" rel=\"stylesheet\" type=\"text/css\" charset=\"utf-8\" >\n" +
//        "<link href=\"/css/sse_allcss_20181218.css?v=3.3.5z\" rel=\"stylesheet\" type=\"text/css\" charset=\"utf-8\" >\n" +
//        "\n" +
//        "\t<!--[if IE 7]>\n" +
//        "\t\t<link href=\"/css/ie7fix.min.css\" rel=\"stylesheet\" type=\"text/css\">\n" +
//        "\t<![endif]-->\n" +
//        "\t<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->\n" +
//        "\t<!-- WARNING: Respond.js 无法工作在 以文件目录形式打开方式，例如 file:// -->\n" +
//        "\t<!--[if lt IE 9]>\n" +
//        "\t\t<script src=\"/js/lib/html5shiv.min.js\"></script>\n" +
//        "\t\t<script src=\"/js/lib/respond.js\"></script>\n" +
//        "\t<![endif]-->\n" +
//        "\n" +
//        "\t<!-- Favicons -->\n" +
//        "    \t <link rel=\"shortcut icon\" href=\"/favicon.ico\">\n" +
//        "\t<!-- IOS / Android 主屏图标 -->\n" +
//        "\t<link href=\"/images/webappicon/apple-touch-icon.png\" rel=\"apple-touch-icon\" />\n" +
//        "\t<link href=\"/images/webappicon/apple-touch-icon-76x76.png\" rel=\"apple-touch-icon\" sizes=\"76x76\" />\n" +
//        "\t<link href=\"/images/webappicon/apple-touch-icon-120x120.png\" rel=\"apple-touch-icon\" sizes=\"120x120\" />\n" +
//        "\t<link href=\"/images/webappicon/apple-touch-icon-152x152.png\" rel=\"apple-touch-icon\" sizes=\"152x152\" />\n" +
//        "\t<link href=\"/images/webappicon/apple-touch-icon-180x180.png\" rel=\"apple-touch-icon\" sizes=\"180x180\" />\n" +
//        "\t<link href=\"/images/webappicon/icon-hires.png\" rel=\"icon\" sizes=\"192x192\" />\n" +
//        "\t<link href=\"/images/webappicon/icon-normal.png\" rel=\"icon\" sizes=\"128x128\" />\n" +
//        "\t\n" +
//        "\n" +
//        "\t<!-- Tile icon for Win8 (144x144 + tile color) -->\n" +
//        "\t<!-- win 8 磁贴标题 -->\n" +
//        "\t<meta name=\"application-name\" content=\"上海证券交易所\">\n" +
//        "\t<!-- win 8 磁贴颜色 -->\n" +
//        "\t<meta name=\"msapplication-TileColor\" content=\"#ffffff\">\n" +
//        "\t<!-- win 8 磁贴图标 -->\n" +
//        "\t<meta name=\"msapplication-TileImage\" content=\"/images/webappicon/apple-touch-icon-120x120.png\">\n" +
//        "\t<meta name=\"msapplication-tooltip\" content=\"Tooltip\">\n" +
//        "\n" +
//        "\t<meta http-equiv=\"Cache-Control\" content=\"no-siteapp\">\n" +
//        "\n" +
//        "\n" +
//        "\t<!--调试阶段禁止缓存,例如微信，QQ浏览器缓存-->\n" +
//        "    <meta http-equiv=\"Cache-Control\" content=\"no-cache, no-store, must-revalidate\">\n" +
//        "<meta http-equiv=\"Pragma\" content=\"no-cache\">\n" +
//        "<meta http-equiv=\"Expires\" content=\"0\">\n" +
//        "<meta name=\"format-detection\" content=\"telephone=no\" />\n" +
//        "\n" +
//        "<script type=\"text/javascript\">\n" +
//        "        var _date_ = new Date();\n" +
//        "        var _y_ = _date_.getFullYear();\n" +
//        "        var _m_ = (_date_.getMonth() + 1);\n" +
//        "        var _d_ = _date_.getDate();\n" +
//        "        var _h_ = _date_.getHours();\n" +
//        "        var _v_ = _y_ + '' + _m_ + '' + _d_ + '' + _h_;\n" +
//        "        document.write('<script src=\"/js/common/ssesuggestdata.js?v=' + _v_ + '\"><\\/script>');\n" +
//        "        document.write('<script src=\"/js/common/ssesuggestfunddata.js?v=' + _v_ + '\"><\\/script>');\n" +
//        "        document.write('<script src=\"/js/common/ssesuggestEbonddata.js?v=' + _v_ + '\"><\\/script>');\n" +
//        "        document.write('<script src=\"/js/common/ssesuggestTbonddata.js?v=' + _v_ + '\"><\\/script>');\n" +
//        "        document.write('<script src=\"/js/common/ssesuggestdataAll.js?v=' + _v_ + '\"><\\/script>');\n" +
//        "        document.write('<script src=\"/js/common/systemDate_global.js?v=' + _v_ + '\"><\\/script>');\n" +
//        "</script>\n" +
//        "<script type=\"text/javascript\">\n" +
//        "        var siteId = 28;\n" +
//        "        var tableFun = {};\n" +
//        "        var tableData = {};\n" +
//        "        var searchUrl = \"/home/search/\";\n" +
//        "\n" +
//        "        var bizUrl = \"//biz.sse.com.cn/\";\n" +
//        "        var staticUrl = \"//static.sse.com.cn/\";\n" +
//        "        var staticBulletinUrl = \"//static.sse.com.cn\";\n" +
//        "        var sseQueryURL = \"//query.sse.com.cn/\";\n" +
//        "\n" +
//        "        var hq_queryUrl;\n" +
//        "        if (location.protocol == \"http:\") {\n" +
//        "                hq_queryUrl = \"//yunhq.sse.com.cn:32041/\";\n" +
//        "        } else {\n" +
//        "                hq_queryUrl = \"//yunhq.sse.com.cn:32042/\";\n" +
//        "        }\n" +
//        "        var bondUrl = \"//bond.sse.com.cn/\";\n" +
//        "        var myUrl = \"//my.sse.com.cn/\";\n" +
//        "        var infoUrl = \"//www.sseinfo.com/\";\n" +
//        "\n" +
//        "        var siteUrl = \"//www.sse.com.cn/\";\n" +
//        "</script>\n" +
//        "\n" +
//        "</head>\n" +
//        "<body>\n" +
//        "<link href=\"/css/sse_insideNew.min.css\" rel=\"stylesheet\" type=\"text/css\" charset=\"utf-8\" > \n" +
//        "\n" +
//        "<div class=\"sse_top_bar mobile_n\">\n" +
//        "  <div class=\"container\">\n" +
//        "    <div class=\"sse_server \">\n" +
//        "        <a class=\"bar_word\" href=\"http://my.sse.com.cn/uc/view/index.shtml\" target=\"_blank\">\n" +
//        "            <!--<i class=\"investor_ser\"></i>-->投资者服务中心\n" +
//        "        </a>\n" +
//        "        <span class=\"bar_word\">投资者服务热线：<b class=\"word_phone\">400-8888-400</b> </span>\n" +
//        "        <a href=\"/home/app/update/\" class=\"bar_word\" target=\"_blank\">APP下载</a>\n" +
//        "        <a href=\"/home/weixin/\" class=\"bar_word\" target=\"_blank\">微博微信</a>\n" +
//        "        <div class=\"sse_mousemenu\">\n" +
//        "            <span class=\"sse_busi bar_word\">业务办理专区</span>\n" +
//        "            <ul  class=\"overmenu_ul\">\n" +
//        "                <li><a target=\"_blank\" href=\"http://biz.sse.com.cn/list/\">上市公司专区</a></li>\n" +
//        "                <li><a target=\"_blank\" href=\"http://biz.sse.com.cn/member/\">会员/证券机构专区</a></li>\n" +
//        "                <li><a target=\"_blank\" href=\"http://biz.sse.com.cn/fund/\">衍生品与基金专区</a></li>\n" +
//        "                <li><a target=\"_blank\" href=\"http://biz.sse.com.cn/bond/\">债券专区</a></li>\n" +
//        "                <li><a target=\"_blank\" href=\"http://www.sse.com.cn/home/biz/cnsca/\">CA服务专区</a></li>\n" +
//        "                <li><a target=\"_blank\" href=\"http://biz.sse.com.cn/media/\">媒体专区</a></li>\n" +
//        "                <li><a target=\"_blank\" href=\"http://csrc.sse.com.cn\">证监局专区</a></li>\n" +
//        "            </ul>\n" +
//        "        </div>\n" +
//        "        <a class=\"bar_word word_en\" href=\"http://english.sse.com.cn/\" target=\"_blank\">English</a><a class=\"bar_word word_ft\" id=\"js_link_change\" href=\"http://big5.sse.com.cn/site/cht/www.sse.com.cn/\" target=\"_blank\">繁</a>\n" +
//        "    </div>\n" +
//        "  </div>\n" +
//        "</div>\n" +
//        "\n" +
//        "<script src=\"/js/28full_2020.js\"></script>\n" +
//        "<script src=\"/js/app/sse_menu_fun_2020.js\"></script>\n" +
//        "\n" +
//        "    <div class=\"sse_header clearfix\">\n" +
//        "        <div class=\"container\">\n" +
//        "            <div class=\"row mobile_flex\">\n" +
//        "                <!-- logo -->\n" +
//        "                <div class=\"col-sm-3 col-xs-5 mobile_logo\">\n" +
//        "                    <a href=\"/\"><img class=\"sse_logo\" src=\"/images/logo.png\" alt=\"\"></a>\n" +
//        "                </div>\n" +
//        "                <div class=\"col-sm-8 col-xs-7 col-sm-offset-1 mobile_user\">\n" +
//        "\n" +
//        "                    <!-- 顶部导航 -->\n" +
//        "                    <div class=\"sse_top_nav row\">\n" +
//        "                        <div class=\"col-sm-10 mobile_n\">\n" +
//        "                            <!--\n" +
//        "                            <ul class=\"sse_top_nav_ul\">\n" +
//        "                                <li><a href=\"\">首页</a></li>\n" +
//        "                                <li><a href=\"\">党建</a></li>\n" +
//        "                                <li class=\"sse_active\"><a href=\"\">披露</a></li>\n" +
//        "                                <li><a href=\"\">数据</a></li>\n" +
//        "                                <li><a href=\"\">产品</a></li>\n" +
//        "                                <li><a href=\"\">服务</a></li>\n" +
//        "                                <li><a href=\"\">规则</a></li>\n" +
//        "                                <li><a href=\"\">关于</a></li>\n" +
//        "                            </ul>\n" +
//        "-->\n" +
//        "                            <div class=\"navbar navbar-collapse collapse\">\n" +
//        "                                <script>document.write(sseMenuObj.menu_tab_text);</script>\n" +
//        "                            </div>\n" +
//        "\n" +
//        "                        </div>\n" +
//        "                        <div class=\"col-sm-2 user_enter\">\n" +
//        "                            <!--<i></i>-->\n" +
//        "    <a href=\"javascript:;\">\n" +
//        "        <svg  viewBox=\"0 0 16 16\" focusable=\"false\" aria-hidden=\"true\" role=\"img\"\n" +
//        "            class=\"bi bi-people\" fill=\"#b20909\" xmlns=\"http://www.w3.org/2000/svg\">\n" +
//        "            <path\n" +
//        "                d=\"M13.468 12.37C12.758 11.226 11.195 10 8 10s-4.757 1.225-5.468 2.37A6.987 6.987 0 0 0 8 15a6.987 6.987 0 0 0 5.468-2.63z\" />\n" +
//        "            <path fill-rule=\"evenodd\" d=\"M8 9a3 3 0 1 0 0-6 3 3 0 0 0 0 6z\" />\n" +
//        "            <path fill-rule=\"evenodd\" d=\"M8 1a7 7 0 1 0 0 14A7 7 0 0 0 8 1zM0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8z\" />\n" +
//        "        </svg>\n" +
//        "        用户入口\n" +
//        "    </a>\n" +
//        "                        </div>\n" +
//        "                        <div class=\"mobile_sel_menu\">\n" +
//        "                            <span></span>\n" +
//        "                            <span></span>\n" +
//        "                            <span></span>\n" +
//        "                        </div>\n" +
//        "                    </div>\n" +
//        "\n" +
//        "                </div>\n" +
//        "            </div>\n" +
//        "        </div>\n" +
//        "\n" +
//        "    </div>\n" +
//        "\n" +
//        "    <!-- pc菜单 -->\n" +
//        "    <div class=\"nav-side-show mobile_hide\" sidshowclass=\"nsshow\">\n" +
//        "        <div class=\"inner menu_con\" id=\"menu_con\">\n" +
//        "            <script>document.write(sseMenuObj.menu_con_text);</script>\n" +
//        "        </div>\n" +
//        "    </div>\n" +
//        "\n" +
//        "    <!-- mobile菜单 -->\n" +
//        "    <div class=\"dl-menuwrapper\" id=\"dl-menu\">\n" +
//        "        <button id=\"dl-menu-button\" class=\"btn\"></button>\n" +
//        "        <ul class=\"dl-menu\" id=\"mobile_menu\">\n" +
//        "            <script type=\"text/javascript\">document.write(sseMenuObj.mobile_menu_text);</script>\n" +
//        "        </ul>\n" +
//        "    </div>\n" +
//        "\n" +
//        "    <div class=\"user_enter_block\">\n" +
//        "        <div class=\"container\">\n" +
//        "            <div class=\"user_block\">\n" +
//        "                <div class=\"block\">\n" +
//        "                    <a href=\"/home/enter/investors/\" target=\"_blank\">\n" +
//        "                        <img src=\"/images/custom-img01.png\" alt=\"\">\n" +
//        "                        <span>投资者</span>\n" +
//        "                    </a>                  \n" +
//        "                </div>\n" +
//        "                <div class=\"block\">\n" +
//        "                    <a href=\"/home/enter/bond/\" target=\"_blank\">\n" +
//        "                        <img src=\"/images/custom-img02.png\" alt=\"\">\n" +
//        "                        <span>债券承销机构</span>\n" +
//        "                    </a>\n" +
//        "                </div>\n" +
//        "                <div class=\"block\">\n" +
//        "                    <a href=\"/home/enter/company/\" target=\"_blank\">\n" +
//        "                        <img src=\"/images/custom-img03.png\" alt=\"\">\n" +
//        "                        <span>上市公司</span>\n" +
//        "                    </a>\n" +
//        "                   \n" +
//        "                </div>\n" +
//        "                <div class=\"block\">\n" +
//        "                    <a href=\"/home/enter/media/\" target=\"_blank\">\n" +
//        "                        <img src=\"/images/custom-img04.png\" alt=\"\">\n" +
//        "                        <span>媒体单位</span>\n" +
//        "                    </a>\n" +
//        "                </div>\n" +
//        "                <div class=\"block\">\n" +
//        "                    <a href=\"/services/list/\" target=\"_blank\">\n" +
//        "                        <img src=\"/images/custom-img05.png\" alt=\"\">\n" +
//        "                        <span>拟上市公司</span>\n" +
//        "                    </a>\n" +
//        "                </div>\n" +
//        "                <div class=\"block\">\n" +
//        "                    <a href=\"/home/enter/member/\" target=\"_blank\">\n" +
//        "                        <img src=\"/images/custom-img06.png\" alt=\"\">\n" +
//        "                        <span>会员/证券机构</span>\n" +
//        "                    </a>\n" +
//        "                </div>\n" +
//        "                <div class=\"block\">\n" +
//        "                    <a href=\"/home/enter/fund/\" target=\"_blank\">\n" +
//        "                        <img src=\"/images/custom-img07.png\" alt=\"\">\n" +
//        "                        <span>基金管理公司</span>\n" +
//        "                    </a>\n" +
//        "                </div>\n" +
//        "            </div>\n" +
//        "        </div>\n" +
//        "    </div>\n" +
//        "\n" +
//        "<div class=\"sse_inside_banner\">\n" +
//        "<div class=\"breadcrumb_wrap_2020\">\n" +
//        "\t\t<div class=\"container\">\n" +
//        "\t\t\t<div class=\"row\">\n" +
//        "\t\t\t\t<div class=\"col-sm-12\">\n" +
//        "\t\t\t\t\t<ol class=\"breadcrumb mobile_hide\" id=\"bread_menu\">\n" +
//        "\t\t\t\t\t\t<script type=\"text/javascript\">document.write(sseMenuObj.initBreadMenu());</script>\n" +
//        "\t\t\t\t\t</ol>\n" +
//        "                    <h1 class=\"page_big_title_2020 js_page_big_title\"></h1>\n" +
//        "\t\t\t\t</div>\n" +
//        "\t\t\t</div>\n" +
//        "\t\t</div>\n" +
//        "\t</div>\n" +
//        "\n" +
//        "</div> \n" +
//        "\n" +
//        "\n" +
//        "<div class=\"sse_search_box search_animation home_search_box js_sse_search_box\">\n" +
//        "  <div class=\"container sse_search_input_con\">\n" +
//        "    <img class=\"sse_search_logo\" src=\"/images/logo.png\" alt=\"\">\n" +
//        "  <div class=\"sse_desk_search_main\">\n" +
//        "    <div class=\"sse_search_main\">\n" +
//        "      <input class=\"sse_search_input\" id=\"serinpt\" type=\"search\" maxlength=\"36\" placeholder=\"请输入关键字\">\n" +
//        "      <input class=\"sse_search_con_btn search_btn\" type=\"button\" onclick=\"searchinpt();\">\n" +
//        "    </div>\n" +
//        "    <div class=\"bdsug\" id=\"desksearhInput\" style=\"width:100%;overflow-x:hidden;overflow-y:hidden;\">\n" +
//        "      <ul class=\"search-history\" id=\"desksearchHist\"></ul>\n" +
//        "      <ul id=\"desksearhTable\"></ul>\n" +
//        "    </div>\n" +
//        "  </div>\n" +
//        "\n" +
//        "  </div>\n" +
//        "</div>\n" +
//        "\n" +
//        "\n" +
//        "\n" +
//        "<div class=\"page_content bgimg1\">\n" +
//        "\t<!-- 面包屑 -->\n" +
//        "\t<div class=\"breadcrumb_wrap mobile_hide\">\n" +
//        "\t\t<div class=\"container\">\n" +
//        "\t\t\t<div class=\"row\">\n" +
//        "\t\t\t\t<div class=\"col-sm-12\">\n" +
//        "\t\t\t\t\t<ol class=\"breadcrumb\" id=\"bread_menu\">\n" +
//        "\t\t\t\t\t\t<script type=\"text/javascript\">document.write(sseMenuObj.initBreadMenu());</script>\n" +
//        "\t\t\t\t\t</ol>\n" +
//        "\t\t\t\t</div>\n" +
//        "\t\t\t</div>\n" +
//        "\t\t</div>\n" +
//        "\t</div>\n" +
//        "\n" +
//        "\t<!-- 面包屑 end -->\n" +
//        "\t<div class=\"container\">\n" +
//        "\t\t<!--页面大标题-->\n" +
//        "\t\t<div class=\"row\">\n" +
//        "\t\t\t<div class=\"col-sm-12\">\n" +
//        "\t\t\t\t<h1 class=\"page_big_title\"> 上市公司地区/行业分类列表</h1>\n" +
//        "\t\t\t</div>\n" +
//        "\t\t</div>\n" +
//        "\t\t\n" +
//        "\n" +
//        "\t\t<div class=\"row\">\n" +
//        "\t\t\t<div class=\"col-sm-3 mobile_hide\">\n" +
//        "\t\t\t\t<div class=\"left_side\">\n" +
//        "\t<ul class=\"left_menu_con\" id=\"left_menu\">\n" +
//        "\t\t<script type=\"text/javascript\">document.write(sseMenuObj.initLeftMenu());</script>\n" +
//        "\t</ul>\n" +
//        "\t<div class=\"left_ewm_con\">\n" +
//        "\t\t<div class=\"ewm_img\"><img src=\"/images/layout/ewm_2016sse.png\"></div>\n" +
//        "\t\t<span class=\"ewm_text\">扫一扫下载 <br>\"上交所移动App\"</span>\n" +
//        "\t</div>\n" +
//        "\t<div class=\"left_ewm_con js_left_ewm_1\">\n" +
//        "\t\t\n" +
//        "\t</div>\n" +
//        "</div>\n" +
//        "\n" +
//        "\t\t\t</div>\n" +
//        "\t\t\t<div class=\"col-sm-9\">\n" +
//        "\t\t\t\t\n" +
//        "<div class=\"row\">\n" +
//        "  <div class=\"col-sm-12\">\n" +
//        "     <div class=\"con_block\">\n" +
//        "      <!--一级区块-->\n" +
//        "<div class=\"sse_common_wrap_cn\">\n" +
//        "<!--一级标题 -->\n" +
//        "     <div class=\"sse_title_common\">\n" +
//        "     <h2 >CSRC行业分类\n" +
//        "</h2>\n" +
//        "    </div>\n" +
//        "<!--一级内容 -->\n" +
//        "        <div class=\"sse_wrap_cn_con\"><div class=\"tab-pane active js_tableT01\"  id=\"tableData_1258\">\n" +
//        "<div class=\"sse_table_title2\" style=\"display:none\"><p></p></div>\n" +
//        "<div class=\"table-responsive sse_table_T01 tdclickable\"  >\n" +
//        "<table class=\"table search_hyfl searchL2\">\t\n" +
//        "<script type=\"text/javascript\">\n" +
//        "\n" +
//        "tableData['tableData_1258'] ={\n" +
//        "staticDate:\"2021-04-09 19:00:01\",\n" +
//        "\t\t\tisPageing:false,\n" +
//        "\t\t\theader:[\n" +
//        "\t\t\t\t[\"INDUSTRY\",\"行业名称\"],\n" +
//        "\t\t\t\t[\"INDUSTRY_CODE\",\"行业代码\"],\n" +
//        "\t\t\t\t[\"TX_NUM\",\"交易股票数<br/>(只)\"],\n" +
//        "\t\t\t\t[\"MKT_VALUE\",\"市价总值<br/>(元)\"],\n" +
//        "\t\t\t\t[\"AVG_PROFIT_RATE\",\"平均市盈率\"],\n" +
//        "\t\t\t\t[\"AVG_PRICE\",\"平均价格<br/>(元)\"]\n" +
//        "\t\t\t],\n" +
//        "\t\t\tlist:[\n" +
//        "\t\t\t'  ',\n" +
//        "\t\t\t\t[\n" +
//        "\t\t\t\t'<a href=\"/assortment/stock/areatrade/trade/detail.shtml?csrcCode=A\">农、林、牧、渔业</a>',\n" +
//        "\t\t\t\t'A',\n" +
//        "\t\t\t\t'15 ',\n" +
//        "\t\t\t\t'97952374585',\n" +
//        "\t\t\t\t'60.79',\n" +
//        "\t\t\t\t'6.83'\n" +
//        "\t\t\t\t],\n" +
//        "\t\t\t'',\n" +
//        "\t\t\t\t[\n" +
//        "\t\t\t\t'<a href=\"/assortment/stock/areatrade/trade/detail.shtml?csrcCode=B\">采矿业</a>',\n" +
//        "\t\t\t\t'B',\n" +
//        "\t\t\t\t'51 ',\n" +
//        "\t\t\t\t'2590191925813',\n" +
//        "\t\t\t\t'14.63',\n" +
//        "\t\t\t\t'5.83'\n" +
//        "\t\t\t\t],\n" +
//        "\t\t\t'',\n" +
//        "\t\t\t\t[\n" +
//        "\t\t\t\t'<a href=\"/assortment/stock/areatrade/trade/detail.shtml?csrcCode=C\">制造业</a>',\n" +
//        "\t\t\t\t'C',\n" +
//        "\t\t\t\t'1146 ',\n" +
//        "\t\t\t\t'20699436125112',\n" +
//        "\t\t\t\t'37.32',\n" +
//        "\t\t\t\t'18.5'\n" +
//        "\t\t\t\t],\n" +
//        "\t\t\t'',\n" +
//        "\t\t\t\t[\n" +
//        "\t\t\t\t'<a href=\"/assortment/stock/areatrade/trade/detail.shtml?csrcCode=D\">电力、热力、燃气及水生产和供应业</a>',\n" +
//        "\t\t\t\t'D',\n" +
//        "\t\t\t\t'76 ',\n" +
//        "\t\t\t\t'1691256712202',\n" +
//        "\t\t\t\t'21.18',\n" +
//        "\t\t\t\t'6.39'\n" +
//        "\t\t\t\t],\n" +
//        "\t\t\t'',\n" +
//        "\t\t\t\t[\n" +
//        "\t\t\t\t'<a href=\"/assortment/stock/areatrade/trade/detail.shtml?csrcCode=E\">建筑业</a>',\n" +
//        "\t\t\t\t'E',\n" +
//        "\t\t\t\t'48 ',\n" +
//        "\t\t\t\t'982983656025',\n" +
//        "\t\t\t\t'7.38',\n" +
//        "\t\t\t\t'5.38'\n" +
//        "\t\t\t\t],\n" +
//        "\t\t\t'',\n" +
//        "\t\t\t\t[\n" +
//        "\t\t\t\t'<a href=\"/assortment/stock/areatrade/trade/detail.shtml?csrcCode=F\">批发和零售业</a>',\n" +
//        "\t\t\t\t'F',\n" +
//        "\t\t\t\t'102 ',\n" +
//        "\t\t\t\t'924118126976',\n" +
//        "\t\t\t\t'19.05',\n" +
//        "\t\t\t\t'8.52'\n" +
//        "\t\t\t\t],\n" +
//        "\t\t\t'',\n" +
//        "\t\t\t\t[\n" +
//        "\t\t\t\t'<a href=\"/assortment/stock/areatrade/trade/detail.shtml?csrcCode=G\">交通运输、仓储和邮政业</a>',\n" +
//        "\t\t\t\t'G',\n" +
//        "\t\t\t\t'77 ',\n" +
//        "\t\t\t\t'1879982107472',\n" +
//        "\t\t\t\t'18.39',\n" +
//        "\t\t\t\t'6.03'\n" +
//        "\t\t\t\t],\n" +
//        "\t\t\t'',\n" +
//        "\t\t\t\t[\n" +
//        "\t\t\t\t'<a href=\"/assortment/stock/areatrade/trade/detail.shtml?csrcCode=H\">住宿和餐饮业</a>',\n" +
//        "\t\t\t\t'H',\n" +
//        "\t\t\t\t'5 ',\n" +
//        "\t\t\t\t'85149054203',\n" +
//        "\t\t\t\t'41.68',\n" +
//        "\t\t\t\t'33.29'\n" +
//        "\t\t\t\t],\n" +
//        "\t\t\t'',\n" +
//        "\t\t\t\t[\n" +
//        "\t\t\t\t'<a href=\"/assortment/stock/areatrade/trade/detail.shtml?csrcCode=I\">信息传输、软件和信息技术服务业</a>',\n" +
//        "\t\t\t\t'I',\n" +
//        "\t\t\t\t'112 ',\n" +
//        "\t\t\t\t'1875660524478',\n" +
//        "\t\t\t\t'46.84',\n" +
//        "\t\t\t\t'16.99'\n" +
//        "\t\t\t\t],\n" +
//        "\t\t\t'',\n" +
//        "\t\t\t\t[\n" +
//        "\t\t\t\t'<a href=\"/assortment/stock/areatrade/trade/detail.shtml?csrcCode=J\">金融业</a>',\n" +
//        "\t\t\t\t'J',\n" +
//        "\t\t\t\t'84 ',\n" +
//        "\t\t\t\t'11557644929690',\n" +
//        "\t\t\t\t'8.4',\n" +
//        "\t\t\t\t'7.64'\n" +
//        "\t\t\t\t],\n" +
//        "\t\t\t'',\n" +
//        "\t\t\t\t[\n" +
//        "\t\t\t\t'<a href=\"/assortment/stock/areatrade/trade/detail.shtml?csrcCode=K\">房地产业</a>',\n" +
//        "\t\t\t\t'K',\n" +
//        "\t\t\t\t'71 ',\n" +
//        "\t\t\t\t'984565574475',\n" +
//        "\t\t\t\t'7.44',\n" +
//        "\t\t\t\t'7.04'\n" +
//        "\t\t\t\t],\n" +
//        "\t\t\t'',\n" +
//        "\t\t\t\t[\n" +
//        "\t\t\t\t'<a href=\"/assortment/stock/areatrade/trade/detail.shtml?csrcCode=L\">租赁和商务服务业</a>',\n" +
//        "\t\t\t\t'L',\n" +
//        "\t\t\t\t'20 ',\n" +
//        "\t\t\t\t'719245943278',\n" +
//        "\t\t\t\t'52.74',\n" +
//        "\t\t\t\t'33.45'\n" +
//        "\t\t\t\t],\n" +
//        "\t\t\t'',\n" +
//        "\t\t\t\t[\n" +
//        "\t\t\t\t'<a href=\"/assortment/stock/areatrade/trade/detail.shtml?csrcCode=M\">科学研究和技术服务业</a>',\n" +
//        "\t\t\t\t'M',\n" +
//        "\t\t\t\t'23 ',\n" +
//        "\t\t\t\t'480278450193',\n" +
//        "\t\t\t\t'82.84',\n" +
//        "\t\t\t\t'45.89'\n" +
//        "\t\t\t\t],\n" +
//        "\t\t\t'',\n" +
//        "\t\t\t\t[\n" +
//        "\t\t\t\t'<a href=\"/assortment/stock/areatrade/trade/detail.shtml?csrcCode=N\">水利、环境和公共设施管理业</a>',\n" +
//        "\t\t\t\t'N',\n" +
//        "\t\t\t\t'29 ',\n" +
//        "\t\t\t\t'175190433701',\n" +
//        "\t\t\t\t'26.72',\n" +
//        "\t\t\t\t'14.26'\n" +
//        "\t\t\t\t],\n" +
//        "\t\t\t'',\n" +
//        "\t\t\t\t[\n" +
//        "\t\t\t\t'<a href=\"/assortment/stock/areatrade/trade/detail.shtml?csrcCode=O\">居民服务、修理和其他服务业</a>',\n" +
//        "\t\t\t\t'O',\n" +
//        "\t\t\t\t'0 ',\n" +
//        "\t\t\t\t'0',\n" +
//        "\t\t\t\t'-',\n" +
//        "\t\t\t\t'-'\n" +
//        "\t\t\t\t],\n" +
//        "\t\t\t'',\n" +
//        "\t\t\t\t[\n" +
//        "\t\t\t\t'<a href=\"/assortment/stock/areatrade/trade/detail.shtml?csrcCode=P\">教育</a>',\n" +
//        "\t\t\t\t'P',\n" +
//        "\t\t\t\t'3 ',\n" +
//        "\t\t\t\t'16180654757',\n" +
//        "\t\t\t\t'45.53',\n" +
//        "\t\t\t\t'10.91'\n" +
//        "\t\t\t\t],\n" +
//        "\t\t\t'',\n" +
//        "\t\t\t\t[\n" +
//        "\t\t\t\t'<a href=\"/assortment/stock/areatrade/trade/detail.shtml?csrcCode=Q\">卫生和社会工作</a>',\n" +
//        "\t\t\t\t'Q',\n" +
//        "\t\t\t\t'3 ',\n" +
//        "\t\t\t\t'154685685747',\n" +
//        "\t\t\t\t'170.43',\n" +
//        "\t\t\t\t'85.7'\n" +
//        "\t\t\t\t],\n" +
//        "\t\t\t'',\n" +
//        "\t\t\t\t[\n" +
//        "\t\t\t\t'<a href=\"/assortment/stock/areatrade/trade/detail.shtml?csrcCode=R\">文化、体育和娱乐业</a>',\n" +
//        "\t\t\t\t'R',\n" +
//        "\t\t\t\t'27 ',\n" +
//        "\t\t\t\t'212618969421',\n" +
//        "\t\t\t\t'14.72',\n" +
//        "\t\t\t\t'7.71'\n" +
//        "\t\t\t\t],\n" +
//        "\t\t\t'',\n" +
//        "\t\t\t\t[\n" +
//        "\t\t\t\t'<a href=\"/assortment/stock/areatrade/trade/detail.shtml?csrcCode=S\">综合</a>',\n" +
//        "\t\t\t\t'S',\n" +
//        "\t\t\t\t'9 ',\n" +
//        "\t\t\t\t'46936933981',\n" +
//        "\t\t\t\t'19.55',\n" +
//        "\t\t\t\t'5.17'\n" +
//        "\t\t\t\t],\n" +
//        "\t\t\t''\n" +
//        "\t\t\t]\n" +
//        "\t\t};\n" +
//        "</script>\n" +
//        "\t\t\t</table></div>\n" +
//        "\n" +
//        "\n" +
//        "\n" +
//        "<!--D01换页，跳转页-->\n" +
//        "<div class=\"page-con-table\" style=\"display:none;\">\n" +
//        "\t<nav class=\"table-page page-con hidden-xs\">\n" +
//        "\t\t<ul class=\"pagination\">\n" +
//        "\t\t\t\n" +
//        "\t\t</ul>\n" +
//        "\t</nav>\n" +
//        "\t<!--手机端分页-->\n" +
//        "\t<div class=\"visible-xs mobile-page\"  >\n" +
//        "\t\t<button type=\"button\" class=\"btn btn-default navbar-btn\">上一页</button>\n" +
//        "\t\t<button type=\"button\" class=\"btn btn-default navbar-btn next-page\">下一页</button>\n" +
//        "\t</div>\n" +
//        "\t<!--手机端分页 end-->\n" +
//        "</div>\n" +
//        "<!--D01换页，跳转页 end-->\n" +
//        "<div class=\"sse_table_conment\" style=\"display:none\"><p></p></div>\n" +
//        "</div></div>\n" +
//        "<!--一级内容结束-->\n" +
//        "</div>\n" +
//        "<!--一级区块结束--><div class=\"sse_dl_2\">\n" +
//        "\n" +
//        "\n" +
//        "点击行业分类的名称，可以查看每个行业的股票列表\n" +
//        "\n" +
//        "\n" +
//        "\n" +
//        "\n" +
//        "</div>\n" +
//        "    </div>\n" +
//        "  </div>\n" +
//        "</div>\n" +
//        " \t\n" +
//        "\t\t\t</div>\n" +
//        "\t\t</div>\n" +
//        "\t</div>\n" +
//        "</div>\n" +
//        "<div class=\"sse_footer\">\n" +
//        "    <div class=\"container row rele_link\">\n" +
//        "        <div class=\"col-sm-7  sse_bot_link\">\n" +
//        "            <a href=\"/aboutus/contactus/\" target=\"_blank\" title=\"\">联系我们</a>\n" +
//        "            <a href=\"/home/feedback/\" target=\"_blank\" title=\"\">意见反馈</a>\n" +
//        "            <a href=\"/home/map/\" target=\"_blank\" title=\"\">网站地图</a>\n" +
//        "            <a href=\"/home/links/\" target=\"_blank\" title=\"\">相关链接</a>\n" +
//        "            <a href=\"/home/legal/\" target=\"_blank\" title=\"\">法律声明</a>\n" +
//        "            <a href=\"/aboutus/sseintroduction/trademark/\" target=\"_blank\" title=\"\">注册商标</a>\n" +
//        "        </div>\n" +
//        "        <div class=\"col-sm-5 sm_icon\">\n" +
//        "            <p class=\"icon_wb\"><img src=\"/images/ewm_wb.png\"><span>官方微博</span></p>\n" +
//        "            <p class=\"icon_wx\"><img src=\"/images/ewm_wx.png\"><span>官方微信</span></p>\n" +
//        "            <p class=\"icon_app\"><img src=\"/images/ewm_app.png\"><span>APP下载</span></p>\n" +
//        "\n" +
//        "        </div>\n" +
//        "    </div>\n" +
//        "    <div class=\"sse_footer_bg\">\n" +
//        "        \n" +
//        "\n" +
//        "\n" +
//        "<div class=\"container\"><div class=\"foot_nav\"><p class=\"foot_nav_t\"><a target=\"_blank\" href=\"/cpc/overview/\" title=\"\">党建</a></p><ul>    <li><a target=\"_blank\" href=\"/cpc/news/\" title=\"\">党建动态</a></li>    <li><a target=\"_blank\" href=\"/cpc/study/\" title=\"\">学习园地</a></li>    <li><a target=\"_blank\" href=\"/cpc/pic/\" title=\"\">图文播报</a></li>    <li><a target=\"_blank\" href=\"/cpc/praise/\" title=\"\">礼赞祖国</a></li>    <li><a target=\"_blank\" href=\"/cpc/basic/\" title=\"\">基层动态</a></li></ul></div><div class=\"foot_nav\"><p class=\"foot_nav_t\"><a target=\"_blank\" href=\"/disclosure/overview/\" title=\"\">披露</a></p><ul>    <li><a target=\"_blank\" href=\"/disclosure/dealinstruc/suspension/\" title=\"\">交易提示</a></li>    <li><a target=\"_blank\" href=\"/disclosure/announcement/general/\" title=\"\">上交所公告</a></li>    <li><a target=\"_blank\" href=\"/disclosure/credibility/supervision/measures/\" title=\"\">监管信息公开</a></li>    <li><a target=\"_blank\" href=\"/disclosure/listedinfo/announcement/\" title=\"\">上市公司信息</a></li>    <li><a target=\"_blank\" href=\"/disclosure/magin/announcement/\" title=\"\">融资融券信息</a></li>    <li><a target=\"_blank\" href=\"/disclosure/fund/announcement/\" title=\"\">基金信息</a></li>    <li><a target=\"_blank\" href=\"/disclosure/diclosure/public/dailydata/\" title=\"\">交易信息披露</a></li>    <li><a target=\"_blank\" href=\"/disclosure/bond/announcement/bookentry/\" title=\"\">债券信息</a></li>    <li><a target=\"_blank\" href=\"/disclosure/optioninfo/update/\" title=\"\">股票期权信息</a></li></ul></div><div class=\"foot_nav\"><p class=\"foot_nav_t\"><a target=\"_blank\" href=\"/market/overview/\" title=\"\">数据</a></p><ul>    <li><a target=\"_blank\" href=\"/market/stockdata/statistic/\" title=\"\">股票数据</a></li>    <li><a target=\"_blank\" href=\"/market/bonddata/overview/day/\" title=\"\">债券数据</a></li>    <li><a target=\"_blank\" href=\"/market/funddata/overview/day/\" title=\"\">基金数据</a></li>    <li><a target=\"_blank\" href=\"/market/othersdata/margin/sum/\" title=\"\">其他数据</a></li>    <li><a target=\"_blank\" href=\"/market/price/trends/\" title=\"\">行情信息</a></li>    <li><a target=\"_blank\" href=\"/market/sseindex/overview/\" title=\"\">上证系列指数</a></li>    <li><a target=\"_blank\" href=\"http://www.csindex.com.cn/zh-CN/indices/index\" title=\"\">中证系列指数</a></li>    <li><a target=\"_blank\" href=\"https://www.cesc.com/sc/index.html\" title=\"\">中华系列指数</a></li></ul></div><div class=\"foot_nav\"><p class=\"foot_nav_t\"><a target=\"_blank\" href=\"/assortment/overview/\" title=\"\">产品</a></p><ul>    <li><a target=\"_blank\" href=\"/assortment/stock/home/\" title=\"\">股票与存托凭证</a></li>    <li><a target=\"_blank\" href=\"/assortment/fund/home/\" title=\"\">基金</a></li>    <li><a target=\"_blank\" href=\"/assortment/bonds/home/\" title=\"\">债券</a></li>    <li><a target=\"_blank\" href=\"/assortment/options/home/\" title=\"\">股票期权</a></li>    <li><a target=\"_blank\" href=\"/reits/home/\" title=\"\">基础设施REITs</a></li></ul></div><div class=\"foot_nav\"><p class=\"foot_nav_t\"><a target=\"_blank\" href=\"/services/overview/\" title=\"\">服务</a></p><ul>    <li><a target=\"_blank\" href=\"/services/list/home/\" title=\"\">企业上市服务专栏</a></li>    <li><a target=\"_blank\" href=\"/services/ipo/home/\" title=\"\">IPO业务专栏</a></li>    <li><a target=\"_blank\" href=\"/services/hkexsc/home/\" title=\"\">沪港通</a></li>    <li><a target=\"_blank\" href=\"/services/greensecurities/home/\" title=\"\">绿色证券专栏</a></li>    <li><a target=\"_blank\" href=\"/services/tradingservice/process/\" title=\"\">交易服务</a></li>    <li><a target=\"_blank\" href=\"/services/tradingservice/charge/ssecharge/\" title=\"\">收费及代收税费</a></li>    <li><a target=\"_blank\" href=\"/services/investors/designatedtxn/\" title=\"\">投资者服务</a></li>    <li><a target=\"_blank\" href=\"/services/listing/equitydivision/home/\" title=\"\">上市公司服务</a></li>    <li><a target=\"_blank\" href=\"/services/information/vendors/\" title=\"\">信息服务</a></li>    <li><a target=\"_blank\" href=\"//training.sse.com.cn\" title=\"\">培训服务</a></li>    <li><a target=\"_blank\" href=\"/services/judicia/\" title=\"\">司法服务</a></li></ul></div><div class=\"foot_nav\"><p class=\"foot_nav_t\"><a target=\"_blank\" href=\"/lawandrules/overview/\" title=\"\">规则</a></p><ul>    <li><a target=\"_blank\" href=\"/lawandrules/rules/law/securities/\" title=\"\">法律法规</a></li>    <li><a target=\"_blank\" href=\"/lawandrules/regulations/csrcorder/\" title=\"\">部门规章</a></li>    <li><a target=\"_blank\" href=\"/lawandrules/sselawsrules/overview/\" title=\"\">本所业务规则</a></li>    <li><a target=\"_blank\" href=\"/lawandrules/guide/latest/\" title=\"本所业务指南与流程\">本所业务指南与流程</a></li>    <li><a target=\"_blank\" href=\"/lawandrules/publicadvice/\" title=\"\">公开征求意见</a></li>    <li><a target=\"_blank\" href=\"/lawandrules/ma/\" title=\"\">自律监管与市场服务事项</a></li></ul></div><div class=\"foot_nav\"><p class=\"foot_nav_t\"><a target=\"_blank\" href=\"/aboutus/overview/\" title=\"\">关于</a></p><ul>    <li><a target=\"_blank\" href=\"/aboutus/sseintroduction/introduction/\" title=\"\">本所介绍</a></li>    <li><a target=\"_blank\" href=\"/aboutus/mediacenter/hotandd/\" title=\"\">媒体中心</a></li>    <li><a target=\"_blank\" href=\"/aboutus/cooperation/siccce/info/\" title=\"\">国际交流与合作</a></li>    <li><a target=\"_blank\" href=\"/aboutus/recruitment/sse/\" title=\"\">招聘信息</a></li>    <li><a target=\"_blank\" href=\"/aboutus/contactus/\" title=\"\">联系我们</a></li>    <li><a target=\"_blank\" href=\"/aboutus/research/latest/\" title=\"\">研究</a></li>    <li><a target=\"_blank\" href=\"/aboutus/socialresponsibility/\" title=\"\">社会责任</a></li>    <li><a target=\"_blank\" href=\"//foundation.sse.com.cn/\" title=\"\">公益慈善</a></li>    <li><a target=\"_blank\" href=\"//csm.sse.com.cn/\" title=\"\">中国证券博物馆</a></li>    <li><a target=\"_blank\" href=\"/aboutus/publication/factbook/\" title=\"\">刊物</a></li></ul></div></div>\n" +
//        "\n" +
//        "\n" +
//        "\n" +
//        "\n" +
//        "        <div class=\"sse_foot_oth_main\">\n" +
//        "            <div class=\"container sse_foot_oth\">\n" +
//        "                <p class=\"sse_oth_l\">400投资者服务热线： 400-8888-400</p>\n" +
//        "                <div class=\"sse_oth_r\">\n" +
//        "                    <p class=\"jian_yi\">建议使用IE11.0以上浏览器，1280×800以上分辨率</p>\n" +
//        "                    <p><span class=\"supipv\">上海证券交易所官网现已支持 <i>IPV<em>6</em></i></span><span>©上海证券交易所版权所有\n" +
//        "                        2020 <a target=\"_blank\" href=\"https://beian.miit.gov.cn/\">沪ICP备05004045号-2</a> <a target=\"_blank\"\n" +
//        "                            href=\"http://www.beian.gov.cn/portal/registerSystemInfo?recordcode=31011502016333\"\n" +
//        "                            style=\"display:inline-block;text-decoration:none;\"><img src=\"/images/beian.png\" /><span\n" +
//        "                                style=\"margin: 0px 0px 0px 5px; \">沪公网安备 31011502016333号</span></a></span></p>\n" +
//        "                </div>\n" +
//        "            </div>\n" +
//        "        </div>\n" +
//        "    </div>\n" +
//        "</div>\n" +
//        "<div class=\"js_footer\">\n" +
//        "    <script type=\"text/javascript\">\n" +
//        "        var require = {\n" +
//        "            //js file version,pls change it with any updating\n" +
//        "            urlArgs: \"v=V202103-04\"\n" +
//        "        };\n" +
//        "        var webVersion = require.urlArgs;\n" +
//        "        var inspectMgr = (function () {\n" +
//        "            //wait for static inspect timeout\n" +
//        "            var staticIndex = 1;/*index=0 use the static,otherwise use the local site file*/\n" +
//        "            var staticUrl = \"//static.sse.com.cn/js/lib/inspect.js\"\n" +
//        "            var waitMilliSeconds = 300;\n" +
//        "            var isIE = document.all && !window.atob;\n" +
//        "            if (isIE) {\n" +
//        "                var isIE8 = '\\v' == 'v';\n" +
//        "                if (isIE8) {\n" +
//        "                    waitMilliSeconds = 400;\n" +
//        "                } else {\n" +
//        "                    waitMilliSeconds = 350;\n" +
//        "                }\n" +
//        "            } else {\n" +
//        "                waitMilliSeconds = 300;\n" +
//        "            }\n" +
//        "            setTimeout(function () {\n" +
//        "                if (1 == staticIndex) {\n" +
//        "                    document.getElementById(\"inspectframe\").src = \"\";\n" +
//        "                }\n" +
//        "            }, waitMilliSeconds * 2.5);\n" +
//        "            return {\n" +
//        "                getWaitMilliSeconds: function () { return waitMilliSeconds; },\n" +
//        "                getStaticIndex: function () { return staticIndex; },\n" +
//        "                setStaticIndex: function (index) { staticIndex = index; },\n" +
//        "                getStaticUrl: function () { return staticUrl; }\n" +
//        "            };\n" +
//        "        })();\n" +
//        "    </script>\n" +
//        "    <iframe id=\"inspectframe\" src=\"/home/public/inspect.html\" style=\"display:none\"></iframe>\n" +
//        "\n" +
//        "    <script src=\"/js/lib/require.js\" data-main=\"/js/app/main_2020\"></script>\n" +
//        "\n" +
//        "    <!-- WeChat share -->\n" +
//        "    <script type=\"text/javascript\">\n" +
//        "        if (/micromessenger/.test(navigator.userAgent.toLowerCase())) {\n" +
//        "            require(['/js/app/sse_wechatassistant.js'], function (sseWechatAssistant) {\n" +
//        "                sseWechatAssistant.init('wx8fd294d57db35754');\n" +
//        "            });\n" +
//        "        }\n" +
//        "\n" +
//        "    </script>\n" +
//        "\n" +
//        "</div>\n" +
//        "\n" +
//        "<!-- /.modal 包括这遮罩-->\n" +
//        "\n" +
//        "<div class=\"app_push visible-xs\" id=\"app_close\">\n" +
//        "    <a class=\"app_download_page\" href=\"http://mb.sseinfo.com/ComInfoServer/ssegwappdownload.jsp\">\n" +
//        "        <span class=\"app_push_icon\"><img src=\"/images/ui/app_push_icon.png\" alt=\"\"></span>\n" +
//        "        <p class=\"app_push_text\">\n" +
//        "            <strong class=\"app_push_text_top\">上交所移动App</strong>\n" +
//        "            <span class=\"app_push_text_bottom\"><s class=\"s_left\"></s> 随时随地掌握第一手资讯 <s class=\"s_right\"></s></span>\n" +
//        "        </p>\n" +
//        "        <button class=\"btn\">立即打开</button>\n" +
//        "    </a>\n" +
//        "\n" +
//        "    <i class=\"app_push_close\"></i>\n" +
//        "</div>\n" +
//        "\n" +
//        "\n" +
//        "<!-- SIA Start -->\n" +
//        "\n" +
//        "<script language=\"JavaScript\">var _trackData = _trackData || []; var _$Mwebsite = '10000042';</script>\n" +
//        "<script type=\"text/javascript\" charset=\"utf-8\" id=\"sseinfo_js_id_10000042\" src=\"/js/la_new.js?v20170113\"></script>\n" +
//        "<!-- SIA End -->\n" +
//        "\n" +
//        "\n" +
//        "</body>\n" +
//        "</html>\n" +
//        "\n" +
//        "\n";
//
//    @Test
//    void parserHtml() throws Exception {
//        String xPathStr = "/html/body/div[8]/div[2]/div[2]/div[2]/div/div/div/div[1]/div[2]/div/div[2]/table";
//        HtmlCleaner hcCleaner = new HtmlCleaner();
//        TagNode tagNode = hcCleaner.clean(html);
//        Document dom = new DomSerializer(new CleanerProperties()).createDOM(tagNode);
//        XPath xPath = XPathFactory.newInstance().newXPath();
//        Object result = xPath.evaluate(xPathStr, dom, XPathConstants.NODESET);
//        String data = ((DTMNodeList) result).getDTMIterator().toString().replaceAll("/\\*\\*/", "");
//        data = data.replaceAll("\\s+", "");
//        String dataJ = data.substring(data.indexOf("=") + 1, data.length() - 1);
//    }
//
//    @Test
//    void htmlUnit() throws Exception {
//        final WebClient wc = new WebClient();
//        wc.getCookieManager().setCookiesEnabled(true);
//        wc.getOptions().setJavaScriptEnabled(true);
//        wc.getOptions().setCssEnabled(true);
//        wc.getOptions().setThrowExceptionOnFailingStatusCode(false);
//        wc.getOptions().setThrowExceptionOnScriptError(false);
//        wc.getOptions().setTimeout(10000);
//        makeCookies(wc);
//        wc.addRequestHeader(org.springframework.http.HttpHeaders.CONNECTION, "keep-alive");
//        wc.addRequestHeader(org.springframework.http.HttpHeaders.PRAGMA, "no-cache");
//        wc.addRequestHeader(org.springframework.http.HttpHeaders.CACHE_CONTROL, "no-cache");
//        wc.addRequestHeader("Upgrade-Insecure-Requests", "1");
//        wc.addRequestHeader(org.springframework.http.HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.114 Safari/537.36");
//        wc.addRequestHeader(org.springframework.http.HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
//        wc.addRequestHeader(org.springframework.http.HttpHeaders.REFERER, "http://www.sse.com.cn/assortment/stock/areatrade/trade/detail.shtml?csrcCode=A");
//        wc.addRequestHeader(org.springframework.http.HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.9");
//        final HtmlPage htmlPage = wc.getPage("http://www.sse.com.cn/assortment/stock/areatrade/trade/");
//        final List<?> div = htmlPage.getByXPath("/html/body/div[8]/div[2]/div[2]/div[2]/div/div/div/div[1]/div[2]/div/div[2]/table");
//    }
//
//    private void makeCookies(WebClient wc) {
//        String ck = "sseMenuSpecial=8534; yfx_c_g_u_id_10000042=_ck21041011121216603955971171634; VISITED_MENU=^%^5B^%^228537^%^22^%^2C^%^228536^%^22^%^5D; yfx_f_l_v_t_10000042=f_t_1618024332661__r_t_1618024332661__v_t_1618026122689__r_c_0";
//        for (String s : ck.split(";")) {
//            Cookie cookie = new Cookie("http://www.sse.com.cn", s.split("=")[0], s.split("=")[1]);
//            wc.getCookieManager().addCookie(cookie);
//        }
//
//    }
//
//    @Test
//    void hyDataTest() {
//        String dataJson = "[{\"companycode\":\"601118\",\"csrcCodeDesc\":\"农、林、牧、渔业\",\"fullname\":\"海南天然橡胶产业集团股份有限公司\",\"securityCodeA\":\"601118\",\"securityCodeB\":\"-\"},{\"companycode\":\"600540\",\"csrcCodeDesc\":\"农、林、牧、渔业\",\"fullname\":\"新疆赛里木现代农业股份有限公司\",\"securityCodeA\":\"600540\",\"securityCodeB\":\"-\"},{\"companycode\":\"600354\",\"csrcCodeDesc\":\"农、林、牧、渔业\",\"fullname\":\"甘肃省敦煌种业集团股份有限公司\",\"securityCodeA\":\"600354\",\"securityCodeB\":\"-\"},{\"companycode\":\"600257\",\"csrcCodeDesc\":\"农、林、牧、渔业\",\"fullname\":\"大湖水殖股份有限公司\",\"securityCodeA\":\"600257\",\"securityCodeB\":\"-\"},{\"companycode\":\"600097\",\"csrcCodeDesc\":\"农、林、牧、渔业\",\"fullname\":\"上海开创国际海洋资源股份有限公司\",\"securityCodeA\":\"600097\",\"securityCodeB\":\"-\"},{\"companycode\":\"600506\",\"csrcCodeDesc\":\"农、林、牧、渔业\",\"fullname\":\"新疆库尔勒香梨股份有限公司\",\"securityCodeA\":\"600506\",\"securityCodeB\":\"-\"},{\"companycode\":\"600359\",\"csrcCodeDesc\":\"农、林、牧、渔业\",\"fullname\":\"新疆塔里木农业综合开发股份有限公司\",\"securityCodeA\":\"600359\",\"securityCodeB\":\"-\"},{\"companycode\":\"600265\",\"csrcCodeDesc\":\"农、林、牧、渔业\",\"fullname\":\"云南景谷林业股份有限公司\",\"securityCodeA\":\"600265\",\"securityCodeB\":\"-\"},{\"companycode\":\"600108\",\"csrcCodeDesc\":\"农、林、牧、渔业\",\"fullname\":\"甘肃亚盛实业(集团)股份有限公司\",\"securityCodeA\":\"600108\",\"securityCodeB\":\"-\"},{\"companycode\":\"600598\",\"csrcCodeDesc\":\"农、林、牧、渔业\",\"fullname\":\"黑龙江北大荒农业股份有限公司\",\"securityCodeA\":\"600598\",\"securityCodeB\":\"-\"},{\"companycode\":\"600371\",\"csrcCodeDesc\":\"农、林、牧、渔业\",\"fullname\":\"万向德农股份有限公司\",\"securityCodeA\":\"600371\",\"securityCodeB\":\"-\"},{\"companycode\":\"600975\",\"csrcCodeDesc\":\"农、林、牧、渔业\",\"fullname\":\"湖南新五丰股份有限公司\",\"securityCodeA\":\"600975\",\"securityCodeB\":\"-\"},{\"companycode\":\"600467\",\"csrcCodeDesc\":\"农、林、牧、渔业\",\"fullname\":\"山东好当家海洋发展股份有限公司\",\"securityCodeA\":\"600467\",\"securityCodeB\":\"-\"}]";
//    }
//
//    @Test
//    void random() {
//        System.out.println("jsonpCallback63138".length());
//        for (int i = 0; i < 100; i++) {
//            System.out.println((int) ((Math.random() * 9 + 1) * 10000));
//        }
//    }
//}
