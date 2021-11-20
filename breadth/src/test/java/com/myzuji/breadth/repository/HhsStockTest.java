package com.myzuji.breadth.repository;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.util.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 说明
 *
 * @author shine
 * @date 2021/12/15
 */
@Slf4j
public class HhsStockTest {

    static WebClient wc = new WebClient();
    static StringBuilder html = new StringBuilder();

    static {
        html.append("<?xml version=\"1.0\" encoding=\"GBK\"?>\n" +
            "<html class=\"black\">\n" +
            "  <head>\n" +
            "    <title>\n" +
            "      万辰生物(300972) 公司资料_F10_同花顺金融服务网\n" +
            "    </title>\n" +
            "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=EmulateIE7;IE=9\"/>\n" +
            "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=gbk\"/>\n" +
            "    <meta name=\"keywords\" content=\"万辰生物最新动态,万辰生物公司概况,万辰生物财务分析,万辰生物股东研究,万辰生物股本结构,万辰生物公司大事,万辰生物分红融资\"/>\n" +
            "    <meta name=\"description\" content=\"暂无\"/>\n" +
            "    <!-- 券商url参数添加，落地只落broker-pcf10.html -->    <script type=\"text/javascript\" src=\"//s.thsi.cn/js/chameleon/chameleon.min.1639920.js\">\n" +
            "    </script>\n" +
            "    <script type=\"text/javascript\" src=\"//s.thsi.cn/js/chameleon/chameleon.min.1639920.js\">\n" +
            "    </script>\n" +
            "    <script>\n" +
            "//<![CDATA[\n" +
            "\n");
        html.append("    function getUrlParams(key, url) {\n" +
            "        var href = url ? url : window.location.href;\n" +
            "        if (href.indexOf(\"?\") < 0 && href.indexOf(\"#\") < 0) {\n" +
            "            return false;\n" +
            "        }\n" +
            "        var hrefStr = href.replace(/#/g, \"&\");\n" +
            "        hrefStr = hrefStr.replace(/\\?/g, \"&\");\n" +
            "        var paramsObj= {};\n" +
            "        hrefStr.split(\"&\").forEach(i => {\n" +
            "            if (i.indexOf(\"=\") > -1) {\n" +
            "                paramsObj[i.split(\"=\")[0]] = i.split(\"=\")[1];\n" +
            "            }\n" +
            "        });\n" +
            "        if (key) {\n" +
            "            return paramsObj[key] || false;\n" +
            "        } else {\n" +
            "            return paramsObj;\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    function addParam(url, obj) {\n" +
            "        var str = \"\",res;\n" +
            "        for (var key in obj) {\n" +
            "            str += (key + \"=\" + obj[key]);\n" +
            "        }\n" +
            "        if (url.indexOf(\"?\") > -1) {\n" +
            "            res = url.split('?').join('?' + str + '&');\n" +
            "        } else if (url.indexOf(\"#\") > -1) {\n" +
            "            res = url.replace(/#/g, \"?\" + str + \"#\");\n" +
            "        } else {\n" +
            "            res = url + \"?\" + str;\n" +
            "        }\n" +
            "        return res;\n" +
            "    }\n" +
            "\n");
        html.append(
            "    var historyUrl = document.referrer;\n" +
                "    if (historyUrl) {\n" +
                "        var broker = getUrlParams('broker', historyUrl);\n" +
                "        if (broker && window.history) {\n" +
                "            var url = window.location.href;\n" +
                "            url = addParam(url, broker ? {broker: broker} : {});\n" +
                "            window.history.pushState('', '', url);\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "//]]>\n" +
                "    </script>\n" +
                "    <!-- f10工具箱，工具函数修改，落地只落utils.html -->    <script type=\"text/javascript\" src=\"//s.thsi.cn/js/chameleon/chameleon.min.1639920.js\">\n" +
                "    </script>\n" +
                "    <script type=\"text/javascript\" charset=\"utf-8\" src=\"//s.thsi.cn/cb?cd/website-thsc-f10-utils/1.4.8/f10-polyfill.js\" crossorigin=\"\">\n" +
                "    </script>\n" +
                "    <script type=\"text/javascript\" charset=\"utf-8\" src=\"//s.thsi.cn/cb?cd/website-thsc-f10-utils/1.4.31/thsc-f10-utils.js\" crossorigin=\"\">\n" +
                "    </script>\n" +
                "    <script type=\"text/javascript\" src=\"//s.thsi.cn/js/chameleon/chameleon.min.1639920.js\">\n" +
                "    </script>\n" +
                "    <script src=\"//s.thsi.cn/js/m/common/bridge.js\" type=\"text/javascript\" crossorigin=\"\">\n" +
                "    </script>\n" +
                "    <script id=\"monitor-script\" api_key=\"ths_f10\" src=\"//s.thsi.cn/js/common/monitor/1.0/monitor.min.js\" crossorigin=\"\">\n" +
                "    </script>\n" +
                "    <script>\n" +
                "//<![CDATA[\n" +
                "\n" +
                "\t\ttry{\n" +
                "\t\t\tfeMonitor.setConfig(\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\tslientDev:false,//开发环境(localhost)是否发送报错请求，默认不发送为true\n" +
                "\t\t\t\tsampleRate:0.1, //采样率,可设置0-1之间，越小采集的频率越低\n" +
                "\t\t\t\tisCollectPerformance:true, //是否采集性能\n" +
                "\t\t\t\ttitle:'pc-F10'\n" +
                "\t\t\t\t}\n" +
                "\t\t\t);\n" +
                "\t\t}catch(e){\n" +
                "\n" +
                "\t\t}\n" +
                "\t\n" +
                "//]]>\n" +
                "    </script>\n" +
                "    <style>\n" +
                "      \n" +
                "        .wencaibs{color:#008BFF!important;cursor:pointer;}\n" +
                "        .wd-tb .ptxt { word-break: break-word;}\n" +
                "        .comment-chart-head{color: #89a;}\n" +
                "        .comment-chart-head span{float: right;}\n" +
                "        .comment-chart-head span i{vertical-align: middle; display: inline-block; width: 20px; height: 20px;margin: 0 2px 0 6px; border-radius: 20px; overflow: hidden;}\n" +
                "        .comment-chart-head .imp-news{background: #da2e1f;}\n" +
                "        .comment-chart-head .yidong-news{background: #e3bf1a;}\n" +
                "\n" +
                "        #ckg_table1 tr td{text-align:center}\n" +
                "        #ckg_table2 tr td{text-align:center}\n" +
                "        #ckg_table3 tr td{text-align:center}\n" +
                "        #ckg_table4 tr td{text-align:center}\n" +
                "        #ckg_table5 tr td{text-align:center}\n" +
                "\t\tli{list-style-type:none;\n" +
                "\t\t\tlist-style-position:outside;}\n" +
                "\t\t#jpjl-input:focus { outline: none; }\n" +
                "\t\n" +
                "    </style>\n" +
                "    <!--统计页面加载时间-->    <script type=\"text/javascript\">\n" +
                "//<![CDATA[\n" +
                "\n" +
                "\t\tvar loadTimer= new Date().getTime();\n" +
                "\t\n" +
                "//]]>\n" +
                "    </script>\n" +
                "    <style>\n" +
                "      \n" +
                "        #search_cyter{\n" +
                "            display: block !important;\n" +
                "        }\n" +
                "\t\t#profile .tickLabels .tickLabel{\n" +
                "\t\t\tpadding-right:12px;\n" +
                "\t\t\tbox-sizing:border-box;\n" +
                "\t\t}\n" +
                "    \n" +
                "    </style>\n" +
                "    <script type=\"text/javascript\">\n" +
                "//<![CDATA[\n" +
                "\n" +
                "\n");
        html.append(
            "//cookie设置\n" +
                "function setReviewJumpState(state) {\n" +
                "\tdocument.cookie = 'reviewJump='\n" +
                "\t\t\t\t\t+ state\n" +
                "\t\t\t\t\t+ ';path=/;domain='\n" +
                "\t\t\t\t\t+ window.location.host;\n" +
                "}\n" +
                "//取得对应名字cookie\n" +
                "function getCookie(name) {\n" +
                "\tvar cookieValue = \"\";\n" +
                "\tvar search = name + \"=\";\n" +
                "\tif (document.cookie.length > 0) {\n" +
                "\t    offset = document.cookie.indexOf(search);\n" +
                "\t    if (offset != -1)    {\n" +
                "\t        offset += search.length;\n" +
                "\t        end = document.cookie.indexOf(\";\", offset);\n" +
                "\t        if (end == -1) end = document.cookie.length;\n" +
                "\t        cookieValue = unescape(document.cookie.substring(offset, end))\n" +
                "\t    }\n" +
                "\t}\n" +
                "\treturn cookieValue;\n" +
                "}\n" +
                "//栏目提前跳转\n" +
                "var reviewJumpState = getCookie('reviewJump');\n" +
                "if (!!reviewJumpState && reviewJumpState != 'nojump') {\n" +
                "\tsetReviewJumpState('nojump');\n" +
                "\twindow.location.href = unescape(reviewJumpState);\n" +
                "} else {\n" +
                "\tsetReviewJumpState('nojump');\n" +
                "}\n" +
                "//动态添加link-同步-会阻塞\n" +
                "function addCssByLink(url){\n" +
                "    document.write('<link rel=\"stylesheet\" type=\"text/css\"');\n" +
                "    document.write(' href=\"' + url + '\">');\n" +
                "}\n" +
                "function isMac() {\n" +
                "        return /macintosh|mac os x/i.test(navigator.userAgent);\n" +
                "};\n" +
                " var hash = window.location.hash.substring(1);\n" +
                "hashArray = hash.split('=');\n" +
                "if (hashArray[0] == 'stockpage') {\n" +
                "    var STOCK_SKIN = 'white';\n" +
                "} else {\n" +
                "    var STOCK_SKIN = getCookie('skin_color');\n" +
                "}\n" +
                "//肤色与客户端同步\n" +
                "function syncClientSkin(){\n" +
                "\ttry {//取得客户端肤色\n" +
                "\t\tif (isMac()) {\n" +
                "\t\t\treturn 'white';\n" +
                "\t\t}\n" +
                "\t\treturn 'black';\n" +
                "\t} catch(e) {}\n" +
                "}\n" +
                "if (!STOCK_SKIN) {\n" +
                "\tSTOCK_SKIN = syncClientSkin();\n" +
                "}\n" +
                "document.documentElement.setAttribute(\"class\",STOCK_SKIN);\n" +
                "if (STOCK_SKIN == 'white') {\n" +
                "\t// addCssByLink('/f10/css/company-compare.css');\n" +
                "\taddCssByLink('//s.thsi.cn/css/basic/stock/company-compare-202006082100.css');\n" +
                " \n" +
                "    addCssByLink('//s.thsi.cn/cb?css/basic/stock/white/20200604153837/style_v4-2.css;css/basic/stock/white/20200604153837/chart_v2.min.20180110.css;css/basic/stock/white/20200604153837/dupont.css;css/basic/stock/white/20200604153837/black_v2-4.20190711.css;css/basic/stock/white/custom.min.css;css/basic/stock/white/20200604153837/operations.css;css/basic/stock/white/20200604153837/writecsskzd.min.20190410.css');\n" +
                "\taddCssByLink('//s.thsi.cn/cb?css/basic/stock/20200604153837/survey-w.css;css/basic/stock/20200604153837/wxgcl_mod.css;css/basic/stock/provider-w_v2.css;css/basic/stock/20200604153837/searchbar-w_v2.css;css/basic/stock/20200604153837/wccept.css;css/basic/stock/wtgfx.css');\n" +
                "\taddCssByLink('//s.thsi.cn/css/basic/stock/fschool/20200604153837/f_school_w.20151214.css');\n" +
                "\taddCssByLink('//s.thsi.cn/css/basic/stock/white/wsplpager.20170504.css');\n" +
                "\taddCssByLink('//s.thsi.cn/css/basic/stock/wchartsdom.css');\n" +
                "\taddCssByLink('//s.thsi.cn/cb?css/basic/stock/white/20200604153837/industrydata.css;css/basic/stock/white/20200604153837/main_v1-2.css;css/basic/stock/white/20200604153837/xgcl.css;css/basic/stock/white/20200604153837/courier.css;css/basic/stock/white/20200604153837/recommend.css;css/basic/stock/white/20200604153837/housedata.css;css/basic/stock/white/202005071600/20200604153837/longhu.css;css/basic/stock/white/20200604153837/operate_white.20170525.css');\n" +
                "\taddCssByLink('//s.thsi.cn/css/basic/stock/white/remind_white.20170713.css');\n" +
                "\taddCssByLink('//s.thsi.cn/css/basic/stock/index_v1.css');\n" +
                "\taddCssByLink('//s.thsi.cn/js/home/v5/thirdpart/scrollbar/jquery.mCustomScrollbar.css');\n" +
                "\taddCssByLink('//s.thsi.cn/css/basic/stock/20200604153837/instition_white_v2.20170720.css');\n" +
                "\tif (!!window.ActiveXObject&&!window.XMLHttpRequest) {\n" +
                "\t\taddCssByLink('//s.thsi.cn/css/basic/stock/white/20200604153837/expression.css');\n" +
                "\t}\n");
        html.append(
            "\t\t\taddCssByLink('//s.thsi.cn/css/basic/stock/20200604153837/jquery.mCustomScrollbar.min.css');\n" +
                "\taddCssByLink('//s.thsi.cn/css/basic/stock/white/20200604153837/patentData_v3.20180130.css');\n" +
                "\taddCssByLink('//s.thsi.cn/css/basic/stock/white/20200604153837/product_cmper_v2.20170706.css');\n" +
                "    \t\taddCssByLink(\"//s.thsi.cn/css/basic/common/white/title.css\")\n" +
                "} else {\n" +
                "\t// addCssByLink('/f10/css/company-compare.css');\n" +
                "addCssByLink('//s.thsi.cn/css/basic/stock/company-compare-202006082100.css');\n" +
                " \taddCssByLink('//s.thsi.cn/cb?css/basic/stock/black/20200604153837/style_v3-5.min.20200426.css;css/basic/stock/black/20200604153837/chart_v2.min.20190410.css;css/basic/stock/black/20200604153837/black_v2-4.20190711.css;css/basic/stock/black/custom.min.css;css/basic/stock/black/20200604153837/operations.css;css/basic/stock/black/20200604153837/csskzd.min.20150608.css;/css/basic/stock/20200604153837/survey.20150929.css;/css/basic/stock/provider_v2.css;/css/basic/stock/20200604153837/searchbar_v2.css;/css/basic/stock/20200604153837/ccept.css');\n" +
                "\taddCssByLink('//s.thsi.cn/css/basic/stock/fschool/20200604153837/f_school.20151214.css');\n" +
                "\taddCssByLink('//s.thsi.cn/css/basic/stock/black/splpager.20170504.css');\n" +
                "\taddCssByLink('//s.thsi.cn/cb?css/basic/stock/chartsdom.css;css/basic/stock/20200604153837/xgcl_mod.css;/css/basic/stock/tgfx.css');\n" +
                "\taddCssByLink('//s.thsi.cn/cb?css/basic/stock/black/20200604153837/industrydata.css;css/basic/stock/black/20200604153837/main_v1-2.css;css/basic/stock/black/20200604153837/xgcl.css;css/basic/stock/black/20200604153837/courier.css;css/basic/stock/black/20200604153837/funcRecommend.css;css/basic/stock/black/20200604153837/dupont.css;css/basic/stock/black/202005071600/20200604153837/longhu.css;css/basic/stock/black/20200604153837/operate.20170525.css');\n" +
                "\taddCssByLink('//s.thsi.cn/css/basic/stock/black/remind_black.20170713.css');\n" +
                "\taddCssByLink('//s.thsi.cn/js/home/v5/thirdpart/scrollbar/jquery.mCustomScrollbar.css');\n" +
                "\n" +
                "\taddCssByLink('//s.thsi.cn/css/basic/stock/index_v1.css');\n" +
                "\taddCssByLink('//s.thsi.cn/css/basic/stock/20200604153837/instition_black_v2.20170720.css');\n" +
                "\tif (!!window.ActiveXObject&&!window.XMLHttpRequest) {\n" +
                "\t\taddCssByLink('//s.thsi.cn/css/basic/stock/black/20200604153837/expression.min.20140801.css');\n" +
                "\t}\n" +
                "\t\taddCssByLink('//s.thsi.cn/css/basic/stock/20200604153837/jquery.mCustomScrollbar.min.css');\n" +
                "\taddCssByLink('//s.thsi.cn/css/basic/stock/20200604153837/patentData_v2.20180130.css');\n" +
                "\taddCssByLink('//s.thsi.cn/css/basic/stock/black/20200604153837/product_cmper_v2.20170706.css');\n" +
                "\t\t\t\taddCssByLink(\"//s.thsi.cn/css/basic/common/black/title.css\")\n" +
                "}\n" +
                "/*calendar*/\n" +
                "function  minDate(){\n" +
                " \tvar data = $('#inte_json').text();\n" +
                "\tvar datajson =new Object();\n" +
                "\tif (data) {\n" +
                "\t\tdata = eval('('+data+')');\n" +
                "\t}\n" +
                "\tfor (var x in data) {\n" +
                "\t\tdatajson = data[x];\n" +
                "\t}\n" +
                "\tvar minDate = datajson.year+'-'+datajson.month+'-'+datajson.day;\n" +
                "\treturn minDate;\n" +
                " }\n" +
                " Date.prototype.Format = function(fmt)\n" +
                "{ //author: meizz\n" +
                "  var o = {\n" +
                "    \"M+\" : this.getMonth()+1,                 //月份\n" +
                "    \"d+\" : this.getDate(),                    //日\n" +
                "    \"h+\" : this.getHours(),                   //小时\n" +
                "    \"m+\" : this.getMinutes(),                 //分\n" +
                "    \"s+\" : this.getSeconds(),                 //秒\n" +
                "    \"q+\" : Math.floor((this.getMonth()+3)/3), //季度\n" +
                "    \"S\"  : this.getMilliseconds()             //毫秒\n" +
                "  };\n" +
                "  if(/(y+)/.test(fmt))\n" +
                "    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+\"\").substr(4 - RegExp.$1.length));\n" +
                "  for(var k in o)\n" +
                "    if(new RegExp(\"(\"+ k +\")\").test(fmt))\n" +
                "  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : ((\"00\"+ o[k]).substr((\"\"+ o[k]).length)));\n" +
                "  return fmt;\n" +
                "}\n" +
                " function  displayDate(){\n" +
                " \tvar data = $('#inte_json').text();\n" +
                " \tvar date =new Array();\n" +
                "\tvar datajson =new Object();\n" +
                "\tvar i=0;\n" +
                "\tif (data) {\n" +
                "\t\tdata = eval('('+data+')');\n" +
                "\t}\n" +
                "\tfor (var x in data) {\n" +
                "\t\tdatajson[x] = data[x];\n" +
                "\t\tdates = datajson[x].year+'-'+datajson[x].month+'-'+datajson[x].day;\n" +
                "\t\tif (x==0|| date[i-1] != dates) {\n" +
                "\t\t\tdate[i] = dates;\n" +
                "\t\t\ti++;\n" +
                "\t\t}\n" +
                "\t}\n");
        html.append(
            "\tvar count =date.length;\n" +
                "\tvar date2 = new Date();\n" +
                "\tvar month =date2.getMonth()+1;\n" +
                "\tvar day =date2.getDate();\n" +
                "\tif ((date2.getMonth()+1)>9) {\n" +
                "\t} else {\n" +
                "\t \tvar month =\"0\"+month;\n" +
                "\t}\n" +
                "\tif (date2.getDate()>9) {\n" +
                "\t} else {\n" +
                "\t \tvar day =\"0\"+day;\n" +
                "\t}\n" +
                "\tyear = date2.getFullYear();\n" +
                "\tdate2 = year+'-'+month+'-'+day;\n" +
                "\tvar result = \"['\";\n" +
                "\tfor (i=0;i<count;i++) {\n" +
                "\t\tif (date2 == date[i]) {\n" +
                "\t\t\tdateArr = date2.split('-');\n" +
                "\t\t\tnewdate = new Date(new Date(dateArr[0], dateArr[1]-1,  dateArr[2])-24*60*60*1000);\n" +
                "\t\t\tdate2 = newdate.Format(\"yyyy-MM-dd\");\n" +
                "\t\t} else {\n" +
                "\t\t\tresult +=date2+\"',\";\n" +
                "\t\t\tdateArr = date2.split('-');\n" +
                "\t\t\tnewdate = new Date(new Date(dateArr[0], dateArr[1]-1,  dateArr[2])-24*60*60*1000);\n" +
                "\t\t\tdate2 = newdate.Format(\"yyyy-MM-dd\");\n" +
                "\t\t\ti--;\n" +
                "\t\t}\n" +
                "\t}\n" +
                "\tresult = result.substr(0,result.length-1)+']';\n" +
                "\treturn result;\n" +
                " }\n");
        html.append(
            "    function showDatePicker1(color) {\n" +
                "        WdatePicker({\n" +
                "            selfCssUrl:'//s.thsi.cn/css/basic/stock/'+STOCK_SKIN+'/mydatepicker.css',\n" +
                "            eCont:'WdateDiv1',\n" +
                "            vel:'elDate1',\n" +
                "            minDate:minDate(),\n" +
                "            maxDate:'%y-%M-%d',\n" +
                "            onpicked:function(dp){\n" +
                "                $('#elDate1').text(dp.cal.getNewDateStr());\n" +
                "                var invalider = $(\".J_calendar\").find(\"iframe\").contents().find(\".WinvalidDay\");\n" +
                "            },\n" +
                "            Mchanged:function(dp){\n" +
                "                var invalider = $(\".J_calendar\").find(\"iframe\").contents().find(\".WinvalidDay\");\n" +
                "            }\n" +
                "        });\n" +
                "    }\n" +
                "    function showDatePicker2(color) {\n" +
                "\n" +
                "        WdatePicker({\n" +
                "            selfCssUrl:'//s.thsi.cn/css/basic/stock/'+STOCK_SKIN+'/mydatepicker.css',\n" +
                "            eCont:'WdateDiv2',\n" +
                "            vel:'elDate2',\n" +
                "            maxDate:'%y-%M-%d',\n" +
                "            onpicked:function(dp){\n" +
                "                $('#elDate2').text(dp.cal.getNewDateStr());\n" +
                "                var invalider = $(\".J_calendar\").find(\"iframe\").contents().find(\".WinvalidDay\");\n" +
                "            },\n" +
                "            Mchanged:function(dp){\n" +
                "                var invalider = $(\".J_calendar\").find(\"iframe\").contents().find(\".WinvalidDay\");\n" +
                "            }\n" +
                "        });\n" +
                "    }\n" +
                "\n" +
                "//]]>\n" +
                "    </script>\n" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"//s.thsi.cn/css/basic/stock/company-compare-202006082100.css\"/>\n" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"//s.thsi.cn/cb?css/basic/stock/black/20200604153837/style_v3-5.min.20200426.css;css/basic/stock/black/20200604153837/chart_v2.min.20190410.css;css/basic/stock/black/20200604153837/black_v2-4.20190711.css;css/basic/stock/black/custom.min.css;css/basic/stock/black/20200604153837/operations.css;css/basic/stock/black/20200604153837/csskzd.min.20150608.css;/css/basic/stock/20200604153837/survey.20150929.css;/css/basic/stock/provider_v2.css;/css/basic/stock/20200604153837/searchbar_v2.css;/css/basic/stock/20200604153837/ccept.css\"/>\n" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"//s.thsi.cn/css/basic/stock/fschool/20200604153837/f_school.20151214.css\"/>\n" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"//s.thsi.cn/css/basic/stock/black/splpager.20170504.css\"/>\n" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"//s.thsi.cn/cb?css/basic/stock/chartsdom.css;css/basic/stock/20200604153837/xgcl_mod.css;/css/basic/stock/tgfx.css\"/>\n" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"//s.thsi.cn/cb?css/basic/stock/black/20200604153837/industrydata.css;css/basic/stock/black/20200604153837/main_v1-2.css;css/basic/stock/black/20200604153837/xgcl.css;css/basic/stock/black/20200604153837/courier.css;css/basic/stock/black/20200604153837/funcRecommend.css;css/basic/stock/black/20200604153837/dupont.css;css/basic/stock/black/202005071600/20200604153837/longhu.css;css/basic/stock/black/20200604153837/operate.20170525.css\"/>\n" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"//s.thsi.cn/css/basic/stock/black/remind_black.20170713.css\"/>\n" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"//s.thsi.cn/js/home/v5/thirdpart/scrollbar/jquery.mCustomScrollbar.css\"/>\n" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"//s.thsi.cn/css/basic/stock/index_v1.css\"/>\n" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"//s.thsi.cn/css/basic/stock/20200604153837/instition_black_v2.20170720.css\"/>\n" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"//s.thsi.cn/css/basic/stock/20200604153837/jquery.mCustomScrollbar.min.css\"/>\n" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"//s.thsi.cn/css/basic/stock/20200604153837/patentData_v2.20180130.css\"/>\n" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"//s.thsi.cn/css/basic/stock/black/20200604153837/product_cmper_v2.20170706.css\"/>\n" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"//s.thsi.cn/css/basic/common/black/title.css\"/>\n" +
                "    <script type=\"text/javascript\" charset=\"utf-8\" src=\"//s.thsi.cn/js/basic/stockph_v2/qdc_ad-4d60ed.js\">\n" +
                "    </script>\n" +
                "    <link href=\"//s.thsi.cn/css/basic/stockph/qdc_ad-ca7dc4.css\" rel=\"stylesheet\" type=\"text/css\"/>\n" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"//s.thsi.cn/css/basic/stock/black/20200604153837/append_v2-3.css\"/>\n" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"//s.thsi.cn/sns/css/external/20200604153837/snsLgtWidget.201503181341.css\"/>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <div class=\"votepopbox votesuccess\" style=\"display: none\">\n" +
                "      <div class=\"vpopicon\">\n" +
                "      </div>\n" +
                "      <p class=\"vpoptxt fz18\">\n" +
                "        谢谢您的宝贵意见\n" +
                "      </p>\n" +
                "      <p class=\"vpoptxt\">\n" +
                "        您的支持是我们最大的动力\n" +
                "      </p>\n" +
                "    </div>\n" +
                "    <div class=\"votepopbox voteinfo\" style=\"display: none\">\n" +
                "      <div class=\"vpopicon\">\n" +
                "      </div>\n" +
                "      <p class=\"vpoptxt fz18\">\n" +
                "        谢谢您的支持\n" +
                "      </p>\n" +
                "      <p class=\"vpoptxt\">\n" +
                "      </p>\n" +
                "    </div>\n" +
                "    <div class=\"wrapper\">\n" +
                "      <!-- F10 Header Start -->      <div class=\"header\">\n" +
                "        <div class=\"hd\">\n" +
                "          <div class=\"logo fl\" id=\"logo_self\">\n" +
                "            <a title=\"同花顺F10\">\n" +
                "              同花顺F10\n" +
                "            </a>\n" +
                "          </div>\n" +
                "          <div onclick=\"sendTalog('f10_click_quot')\" style=\"width:260px; margin-left:60px; font-size:14px;cursor:pointer;margin-right:10px;display:none;text-align:center\" class=\"fl tip \" id=\"quotedata\">\n" +
                "          </div>\n" +
                "          <span class=\"fr skin-change\">\n" +
                "            <a href=\"###\">\n" +
                "              换肤\n" +
                "            </a>\n" +
                "          </span>\n" +
                "          <div class=\"search fr\" id=\"search_cyter\">\n" +
                "            <div id=\"updownchange\" type=\"once\" class=\"codeChange fl\" style=\"display: none;\">\n" +
                "            </div>\n" +
                "            <div class=\"fl searchbar\">\n" +
                "              <div class=\"text fl\">\n" +
                "                <input type=\"text\" id=\"jpjl-input\" value=\"输入股票名称或代码\"/>\n" +
                "                <!--文本框获得焦点给.searchbar添加hover类-->              </div>\n" +
                "              <input type=\"button\" value=\"搜索\" class=\"btn\" id=\"submit\"/>\n" +
                "            </div>\n" +
                "          </div>\n" +
                "        </div>\n" +
                "        <div class=\"bd clear\">\n" +
                "          <div class=\"code fl\">\n" +
                "            <div>\n" +
                "              <h1 style=\"margin:3px 0px 0px 0px\">\n" +
                "                \n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t万辰生物\n" +
                "              </h1>\n" +
                "            </div>\n" +
                "            <div>\n" +
                "              <a href=\"interactive.html\" tid=\"wdm\" posid=\"r1c1\" class=\"iwen\" onclick=\"TA.log({'id':'F10_review','nj':1})\">\n" +
                "                i问董秘\n" +
                "              </a>\n" +
                "              <h1 style=\"margin:3px 0px 0px 0px\">\n" +
                "                \n" +
                "\t\t\t\t\t\t300972\t\t\t\t\t\t\n" +
                "              </h1>\n" +
                "            </div>\n" +
                "          </div>\n" +
                "          <div class=\"nav\">\n" +
                "            <ul>\n" +
                "              <li>\n" +
                "                <a href=\"./\" target=\"_self\" tid=\"zxdt\" posid=\"r1c3\">\n" +
                "                  最新动态\n" +
                "                </a>\n" +
                "              </li>\n" +
                "              <li>\n" +
                "                <a href=\"./company.html\" target=\"_self\" tid=\"gszl\" posid=\"r1c4\" class=\"cur\">\n" +
                "                   公司资料\n" +
                "                </a>\n" +
                "              </li>\n" +
                "              <li>\n" +
                "                <a href=\"./holder.html\" target=\"_self\" tid=\"gdyj\" posid=\"r1c5\">\n" +
                "                  股东研究\n" +
                "                </a>\n" +
                "              </li>\n" +
                "              <li>\n" +
                "                <a href=\"./operate.html\" target=\"_self\" tid=\"jyfx\" posid=\"r1c6\">\n" +
                "                   经营分析\n" +
                "                </a>\n" +
                "              </li>\n" +
                "              <li>\n" +
                "                <a href=\"./equity.html\" target=\"_self\" tid=\"gbjg\" posid=\"r1c7\">\n" +
                "                  股本结构\n" +
                "                </a>\n" +
                "              </li>\n" +
                "              <li>\n" +
                "                <a href=\"./capital.html\" target=\"_self\" tid=\"zbyz\" posid=\"r1c8\">\n" +
                "                   资本运作\n" +
                "                </a>\n" +
                "              </li>\n" +
                "              <li>\n" +
                "                <a href=\"./worth.html\" target=\"_self\" tid=\"ylyc\" posid=\"r1c9\">\n" +
                "                  盈利预测\n" +
                "                </a>\n" +
                "              </li>\n" +
                "              <li>\n" +
                "                <a href=\"./news.html\" target=\"_self\" tid=\"xwgg\" posid=\"r2c3\">\n" +
                "                  新闻公告\n" +
                "                </a>\n" +
                "              </li>\n" +
                "              <li>\n" +
                "                <a href=\"./concept.html\" target=\"_self\" tid=\"gntc\" posid=\"r2c4\">\n" +
                "                  概念题材\n" +
                "                </a>\n" +
                "              </li>\n" +
                "              <li>\n" +
                "                <a href=\"./position.html\" target=\"_self\" tid=\"zlcc\" posid=\"r2c5\">\n" +
                "                  主力持仓\n" +
                "                </a>\n" +
                "              </li>\n" +
                "              <li>\n" +
                "                <a href=\"./finance.html\" target=\"_self\" tid=\"cwgk\" posid=\"r2c6\">\n" +
                "                   财务概况\n" +
                "                </a>\n" +
                "              </li>\n" +
                "              <li>\n" +
                "                <a href=\"./bonus.html\" target=\"_self\" tid=\"fhrz\" posid=\"r2c7\">\n" +
                "                  分红融资\n" +
                "                </a>\n" +
                "              </li>\n" +
                "              <li>\n" +
                "                <a href=\"./event.html\" target=\"_self\" tid=\"gsds\" posid=\"r2c8\">\n" +
                "                  公司大事\n" +
                "                </a>\n" +
                "              </li>\n" +
                "              <li>\n" +
                "                <a href=\"./field.html\" target=\"_self\" tid=\"hydb\" posid=\"r2c9\">\n" +
                "                  行业对比\n" +
                "                </a>\n" +
                "              </li>\n" +
                "            </ul>\n" +
                "          </div>\n" +
                "          <div class=\"subnav\">\n" +
                "            <ul>\n" +
                "              <li class=\"cur_li\">\n" +
                "                <a style=\"position:relative;height:25px;\" class=\"skipto\" type=\"\" nav=\"detail\" name=\"company.html#detail\" href=\"###\">\n" +
                "                  详细情况\t\t\t\t\t\t\t\n" +
                "                </a>\n" +
                "              </li>\n" +
                "              <li>\n" +
                "                <a style=\"position:relative;height:25px;\" class=\"skipto\" type=\"\" nav=\"manager\" name=\"company.html#manager\" href=\"###\">\n" +
                "                  高管介绍\t\t\t\t\t\t\t\n" +
                "                </a>\n" +
                "              </li>\n" +
                "              <li>\n" +
                "                <a style=\"position:relative;height:25px;\" class=\"skipto\" type=\"\" nav=\"publish\" name=\"company.html#publish\" href=\"###\">\n" +
                "                  发行相关\t\t\t\t\t\t\t\n" +
                "                </a>\n" +
                "              </li>\n" +
                "              <li>\n" +
                "                <a style=\"position:relative;height:25px;\" class=\"skipto\" type=\"\" nav=\"share\" name=\"company.html#share\" href=\"###\">\n" +
                "                  参控股公司\t\t\t\t\t\t\t\n" +
                "                </a>\n" +
                "              </li>\n" +
                "              <li>\n" +
                "                <a style=\"position: relative; height: 25px; display: none;\" class=\"skipto\" type=\"\" nav=\"patent\" name=\"company.html#patent\" href=\"###\">\n" +
                "                  专利状况\t\t\t\t\t\t\t\n" +
                "                </a>\n" +
                "              </li>\n" +
                "            </ul>\n" +
                "          </div>\n" +
                "        </div>\n" +
                "      </div>\n" +
                "      <!-- F10 Header End -->      <!-- F10 Content Start -->      <style>\n" +
                "        \n");
        html.append(
            "\t\t.iwc_searchbar{display: none;}\n" +
                "\t\n" +
                "      </style>\n" +
                "      <div class=\"iwc_searchbar clearfix\" style=\"left: -8px;\">\n" +
                "        <div class=\"tips-box\" style=\"display: none;\">\n" +
                "          <div class=\"t-hd\">\n" +
                "          </div>\n" +
                "          <div class=\"t-bd\">\n" +
                "            <div class=\"info\">\n" +
                "              <p>\n" +
                "                F10 功能找不到？在搜索框里直接输入您想要的功能！比如输入“龙虎榜”，赶快试一下哦！\n" +
                "              </p>\n" +
                "              <div class=\"btn\">\n" +
                "                <a href=\"###\" class=\"a-left close\">\n" +
                "                  跳过\n" +
                "                </a>\n" +
                "                <a href=\"###\" class=\"a-right next\">\n" +
                "                  下一步\n" +
                "                </a>\n" +
                "              </div>\n" +
                "            </div>\n" +
                "            <div class=\"info\" style=\"display: none;\">\n" +
                "              <p>\n" +
                "                选股选不好？可以直接输入你想要的问句啦，比如输入“近一周涨幅超过30%的股票”赶紧行动吧！\n" +
                "              </p>\n" +
                "              <div class=\"btn\">\n" +
                "                <a href=\"###\" class=\"a-left prev\">\n" +
                "                  上一步\n" +
                "                </a>\n" +
                "                <a href=\"###\" class=\"a-right close\">\n" +
                "                  完成\n" +
                "                </a>\n" +
                "              </div>\n" +
                "            </div>\n" +
                "          </div>\n" +
                "          <div class=\"t-ft\">\n" +
                "          </div>\n" +
                "        </div>\n" +
                "        <div class=\"searchfx\">\n" +
                "          <input id=\"search_input\" x-webkit-speech=\"\" x-webkit-grammar=\"builtin:search\" lang=\"zh_CN\" class=\"fillbox\" autocomplete=\"off\" type=\"text\" value=\"该股票最热门的概念题材\"/>\n" +
                "          <input class=\"action_btn\" id=\"search_submit\" type=\"button\" value=\"搜索\"/>\n" +
                "          <span class=\"tips-icon\">\n" +
                "          </span>\n" +
                "        </div>\n" +
                "      </div>\n" +
                "      <div class=\"content page_event_content\">\n" +
                "        <link rel=\"stylesheet\" href=\"//s.thsi.cn/css/basic/stock/newPcCss/20200604153837/newCompany_cb.css\"/>\n" +
                "        <link rel=\"stylesheet\" href=\"//s.thsi.cn/css/basic/stock/newPcCss/20200604153837/company_share.css\"/>\n" +
                "        <div class=\"m_box company_overview company_detail\" id=\"detail\" stat=\"compnay_detail\">\n" +
                "          <div class=\"hd flow_index search_z\">\n" +
                "            <h2>\n" +
                "              详细情况\n" +
                "            </h2>\n" +
                "          </div>\n" +
                "          <div class=\"bd\">\n" +
                "            <table class=\"m_table\">\n" +
                "              <tbody>\n" +
                "                <tr class=\"video-btn-box-tr\">\n" +
                "                  <td rowspan=\"3\" align=\"center\">\n" +
                "                    <img src=\"http://eq.10jqka.com.cn/logo/300972.png\" alt=\"福建万辰生物科技股份有限公司\" height=\"60\" width=\"160\"/>\n" +
                "                    <!--<p class=\"email-address\">企业在此投放宣传视频：news@myhexin.com</p>-->                  </td>\n" +
                "                  <td>\n" +
                "                    <strong class=\"hltip fl\">\n" +
                "                      公司名称：\n" +
                "                    </strong>\n" +
                "                    <span>\n" +
                "                      福建万辰生物科技股份有限公司\n" +
                "                    </span>\n" +
                "                  </td>\n" +
                "                  <td>\n" +
                "                    <strong class=\"hltip fl\">\n" +
                "                      所属地域：\n" +
                "                    </strong>\n" +
                "                    <span>\n" +
                "                      福建省\n" +
                "                    </span>\n" +
                "                  </td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                  <td>\n" +
                "                    <strong class=\"hltip fl\">\n" +
                "                      英文名称：\n" +
                "                    </strong>\n" +
                "                    <span>\n" +
                "                      Fujian Wanchen Biotechnology Co.,Ltd\n" +
                "                    </span>\n" +
                "                  </td>\n" +
                "                  <td>\n" +
                "                    <strong class=\"hltip fl\">\n" +
                "                      所属申万行业：\n" +
                "                    </strong>\n" +
                "                    <span>\n" +
                "                      农林牧渔 — 种植业\n" +
                "                    </span>\n" +
                "                  </td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                  <td>\n" +
                "                    <strong class=\"hltip fl\">\n" +
                "                      曾 用 名：\n" +
                "                    </strong>\n" +
                "                    <span>\n" +
                "                      -\n" +
                "                    </span>\n" +
                "                  </td>\n" +
                "                  <td>\n" +
                "                    <strong class=\"hltip fl\">\n" +
                "                      公司网址：\n" +
                "                    </strong>\n" +
                "                    <span>\n" +
                "                      <a href=\"http://www.vanchen.com\" target=\"_blank\">\n" +
                "                        www.vanchen.com\n" +
                "                      </a>\n" +
                "                    </span>\n" +
                "                  </td>\n" +
                "                </tr>\n" +
                "              </tbody>\n" +
                "            </table>\n" +
                "            <div class=\"m_tab_content2\">\n" +
                "              <table class=\"m_table ggintro managelist\">\n" +
                "                <tbody>\n" +
                "                  <tr>\n" +
                "                    <td colspan=\"3\">\n" +
                "                      <strong class=\"hltip fl\">\n" +
                "                        主营业务：\n" +
                "                      </strong>\n" +
                "                      <span>\n" +
                "                        鲜品食用菌的研发、工厂化培育与销售。\n" +
                "                      </span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                  <tr class=\"product_name\">\n" +
                "                    <td colspan=\"3\">\n" +
                "                      <strong class=\"hltip fl\">\n" +
                "                        产品名称：\n" +
                "                      </strong>\n" +
                "                      <span>\n" +
                "                        <span style=\"color:#ffb400\">\n" +
                "                          \n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t金针菇\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\n" +
                "                          <a class=\"m_more\" topstock=\"300972\" cid=\"金针菇\" tag=\"金针菇\" onclick=\"TA.log({'id':'f10_jzds','nj':1})\" href=\"javascript:void(0)\" targ=\"proCompete_box0\" title=\"产品竞争\">\n" +
                "                          </a>\n" +
                "                          \n" +
                "\t\t\t\t\t\t\t    \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t、蟹味菇\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\n" +
                "                          <a class=\"m_more\" topstock=\"300972\" cid=\"蟹味菇\" tag=\"蟹味菇\" onclick=\"TA.log({'id':'f10_jzds','nj':1})\" href=\"javascript:void(0)\" targ=\"proCompete_box1\" title=\"产品竞争\">\n" +
                "                          </a>\n" +
                "                          \n" +
                "\t\t\t\t\t\t\t    \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t、白玉菇\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\n" +
                "                          <a class=\"m_more\" topstock=\"300972\" cid=\"白玉菇\" tag=\"白玉菇\" onclick=\"TA.log({'id':'f10_jzds','nj':1})\" href=\"javascript:void(0)\" targ=\"proCompete_box2\" title=\"产品竞争\">\n" +
                "                          </a>\n" +
                "                          \n" +
                "\t\t\t\t\t\t\t    \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t、海鲜菇\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\n" +
                "                          <a class=\"m_more\" topstock=\"300972\" cid=\"海鲜菇\" tag=\"海鲜菇\" onclick=\"TA.log({'id':'f10_jzds','nj':1})\" href=\"javascript:void(0)\" targ=\"proCompete_box3\" title=\"产品竞争\">\n" +
                "                          </a>\n" +
                "                        </span>\n" +
                "                      </span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                  <!--\n" +
                "\t\t\t\t\t<tr class=\"product_show\">\t\t\t\t\t\t\t\t\t\n" +
                "\t\t\t\t\t\t<td colspan=\"3\" class =\"product\"><strong class=\"hltip fl\">可能存在产品：</strong>\n" +
                "\t\t\t\t\t\t\t<span class=\"product_title\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t</span>\n" +
                "\t\t\t\t\t\t\t<span class=\"performance_trailer\"> \n" +
                "\t\t\t\t\t\t\t    <a class=\"check_detail\" href=\"javascript:void(0);\" onclick=\"clickTalogStat('f10_gszl_xxqk_product_zk','F10new_tbts');\">\n" +
                "                    \t\t\t\t<span class=\"open_btn\">展开&nbsp▼</span><span class=\"close_btn hidden\">收起&nbsp▲</span>\n" +
                "                    \t        </a>\n" +
                "                    \t\t</span>\n" +
                "                    \t\t<span tag=\"zycpcontent\" taname=\"f10_stock_iwc_knczcp\" onclick=\"clickTalogStat('f10_gszl_xxqk_product_wh','F10new_tbts');\" class=\"gd_ques iwcclick\" name=\"可能存在产品\" content=\"是同花顺根据人工智能计算挖掘得出的结果，数据仅供参考。\"></span>\n" +
                "                    \t\t<div class=\"check_else performance_trailer\" style=\"display:none;width:100%;\"> \n" +
                "                    \t\t         <table class=\"m_table m_hl\" style=\"width:100%;\">\n" +
                "                        \t\t\t\t<thead>\n" +
                "\t\t\t\t\t<tr>\n" +
                "                        \t\t\t\t\t\t<th class=\"tc\" width=\"22%\">主营产品名称</th>\n" +
                "                        \t\t\t\t\t\t<th class=\"tc\">出自摘要</th>\n" +
                "                        \t\t\t\t\t</tr>\n" +
                "                        \t\t\t\t</thead>\n" +
                "                        \t\t\t\t<tbody>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t<td class=\"tc\"><span></span></td>\n" +
                "                    \t\t\t        <td class=\"tl\"><span class=\"short_content\"></span><span class=\"more_tag1\"><a class=\"client\" onclick=\"clickTalogStat('f10_gszl_xxqk_product_xq','F10new_tbts');\" href=\"\" target=\"_blank\">详情>></a></span></td>\n" +
                "                    \t\t\t        </tr>\n" +
                "                    \t\t\t        \t\t\t\t\t\t\t\t\t\t\n" +
                "\t\t\t\t\t\t\t\t\t</tbody>\n" +
                "\t\t\t\t\t\t\t\t  </table>\n" +
                "                    \t\t\t</div> \n");
        html.append(
            "\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t<tr>-->                  <tr>\n" +
                "                    <td colspan=\"3\">\n" +
                "                      <div class=\"tipbox_wrap mr10\" style=\"z-index:100\">\n" +
                "                        <strong class=\"hltip fl iwcclick\" taname=\"f10_stock_iwc_kggdck\" data-url=\"http://www.iwencai.com/yike/detail/auid/0948cd92a5d19de8?qs=client_f10_baike_4\" content=\"控股股东是指其出资额占有限责任公司资本总额50％以上，或者是其持有的股份占股份有限公司股本总额50％以上的股东；且出资额或者持有股份的比例虽然不足50％，但依其出资额或者持有的股份所享有的表决权已足以对股东会、股东大会的决议产生重大影响的股东。\">\n" +
                "                          控股股东：\n" +
                "                        </strong>\n" +
                "                        <span>\n" +
                "                          福建含羞草农业开发有限公司\t\t\t\t\t        \n" +
                "                          <span class=\"gray\">\n" +
                "                            (持有福建万辰生物科技股份有限公司股份比例：26.68%）\n" +
                "                          </span>\n" +
                "                        </span>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                  <tr>\n" +
                "                    <td colspan=\"3\">\n" +
                "                      <div class=\"tipbox_wrap mr10\" style=\"z-index:99\">\n" +
                "                        <strong class=\"hltip fl iwcclick\" taname=\"f10_stock_iwc_kggdck\" data-url=\"http://www.iwencai.com/yike/detail/auid/abb88516c5fb2dbd?qs=client_f10_baike_4\" content=\"指虽不是公司的股东，但通过投资关系、协议或者其他安排，能够实际支配公司行为的人\">\n" +
                "                          实际控制人：\n" +
                "                        </strong>\n" +
                "                        <span>\n" +
                "                          王泽宁、王丽卿、陈文柱\t\t\t\t\t\t\t\n" +
                "                          <span class=\"gray\">\n" +
                "                            (持有福建万辰生物科技股份有限公司股份比例：36.82、7.61、5.07%）\n" +
                "                          </span>\n" +
                "                        </span>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                  <tr>\n" +
                "                    <td colspan=\"3\">\n" +
                "                      <div class=\"tipbox_wrap mr10\" style=\"z-index:98\">\n" +
                "                        <strong class=\"hltip fl\">\n" +
                "                          最终控制人：\n" +
                "                        </strong>\n" +
                "                        <span>\n" +
                "                          王泽宁、王丽卿、陈文柱\t\t\t\t\t\t\t\t\n" +
                "                          <span class=\"gray\">\n" +
                "                            (持有福建万辰生物科技股份有限公司股份比例：36.82、7.61、5.07%）\n" +
                "                          </span>\n" +
                "                        </span>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                  <tr>\n" +
                "                    <td class=\"name\">\n" +
                "                      <strong class=\"hltip fl\">\n" +
                "                        董事长：\n" +
                "                      </strong>\n" +
                "                      <span>\n" +
                "                        <a person_id=\"T737676000\" class=\"turnto\" href=\"javascript:void(0)\">\n" +
                "                          王健坤\n" +
                "                        </a>\n" +
                "                        <div class=\"person_table hidden\">\n" +
                "                          <a class=\"close\" href=\"javascript:void(0);\">\n" +
                "                            ×\n" +
                "                          </a>\n" +
                "                          <table class=\"m_table ggintro\" style=\"width: 100%\">\n" +
                "                            <thead>\n" +
                "                              <tr>\n" +
                "                                <td rowspan=\"2\" class=\"title\">\n" +
                "                                  <h3>\n" +
                "                                    \n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t王健坤\t\t\t\t\t\t\t\t\t\t\t\t\n" +
                "                                  </h3>\n" +
                "                                  <input type=\"hidden\" value=\"{&quot;name&quot;:&quot;\\u738b\\u5065\\u5764&quot;,&quot;id&quot;:1,&quot;pid&quot;:0,&quot;children&quot;:[{&quot;name&quot;:&quot;\\u542b\\u7f9e\\u8349(\\u6c5f\\u82cf)\\u98df\\u54c1\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;100.00%&quot;,&quot;id&quot;:2,&quot;pid&quot;:1,&quot;children&quot;:[]}]}\"/>\n" +
                "                                </td>\n" +
                "                                <td colspan=\"3\" class=\"jobs\" style=\"width: 150px;\">\n" +
                "                                  董事,战略委员会委员,董事长,战略委员会主任委员,提名委员会委员\n" +
                "                                </td>\n" +
                "                              </tr>\n" +
                "                              <tr>\n" +
                "                                <td class=\"intro\">\n" +
                "                                  男  53岁  硕士\n" +
                "                                </td>\n" +
                "                                <td class=\"salary\">\n" +
                "                                  <span>\n" +
                "                                    薪酬：\n" +
                "                                  </span>\n" +
                "                                  12万\n" +
                "                                </td>\n" +
                "                                <td>\n" +
                "                                  <div class=\"tipbox_wrap\" style=\"z-index: 99\">\n" +
                "                                    <span>\n" +
                "                                      持股数：\n" +
                "                                    </span>\n" +
                "                                    <span class=\"hold\">\n" +
                "                                      -\n" +
                "                                    </span>\n" +
                "                                  </div>\n" +
                "                                </td>\n" +
                "                              </tr>\n" +
                "                            </thead>\n" +
                "                            <tbody>\n" +
                "                              <tr>\n" +
                "                                <td colspan=\"4\" class=\"mainintro\">\n" +
                "                                  <div>\n" +
                "                                    <p>\n" +
                "                                      王健坤先生,1968年5月出生,中国香港居民,南京理工大学MBA毕业,硕士研究生学历。曾任福建农开发总经理、执行董事,福建东方食品执行董事、总经理,漳州含羞草食品执行董事,江苏含羞草董事长、总经理、执行董事、监事会主席,上海含羞草食品有限公司(后更名为上海含勋商务咨询有限公司)执行董事兼总经理,上海含羞草贸易执行董事兼总经理,青州市澳润福食品有限公司执行董事兼经理。现任江苏省政协委员、福建农开发执行董事、含羞草(江苏)食品有限公司执行董事兼总经理、东方国际(香港)股份有限公司董事、漳州金万辰执行董事兼总经理、南京金万辰执行董事、江苏和正董事、万辰生物董事长。\n" +
                "                                    </p>\n" +
                "                                    <!--\t\t\t\t\t\t\t\t\t\t\t\t\t<p style=\"text-align:right\">此简介更新于-->                                    <!--</p>-->                                  </div>\n" +
                "                                </td>\n" +
                "                              </tr>\n" +
                "                            </tbody>\n" +
                "                          </table>\n" +
                "                          <div class=\"stock-right-container hidden\" data-id=\"0\">\n" +
                "                            <img class=\"full-screen right-top\" src=\"//s.thsi.cn/js/basic/stock/img/full-black.png\"/>\n" +
                "                            <span class=\"close right-top hidden\">\n" +
                "                              ×\n" +
                "                            </span>\n" +
                "                            <div class=\"stock-right\" id=\"stock-right-0\">\n" +
                "                            </div>\n" +
                "                            <div class=\"tips hidden\">\n" +
                "                              该信息基于\n" +
                "                              <a class=\"tyc_link\" href=\"https://www.tianyancha.com/\" target=\"_blank\">\n" +
                "                                <strong>\n" +
                "                                  天眼查\n" +
                "                                </strong>\n" +
                "                              </a>\n" +
                "                              数据分析得出，仅供参考，更多信息请到天眼查官网浏览\n" +
                "                            </div>\n" +
                "                          </div>\n" +
                "                          <div class=\"stock-right-btn see-stock-right tc\">\n" +
                "                            查看股权▼\n" +
                "                          </div>\n" +
                "                          <div class=\"stock-right-btn close-stock-right hidden tc\">\n" +
                "                            收起股权▲\n" +
                "                          </div>\n" +
                "                        </div>\n" +
                "                      </span>\n" +
                "                    </td>\n" +
                "                    <td class=\"name\">\n" +
                "                      <strong class=\"hltip fl\">\n" +
                "                        董　　秘：\n" +
                "                      </strong>\n" +
                "                      <span>\n" +
                "                        <a person_id=\"T737692000\" class=\"turnto\" href=\"javascript:void(0)\">\n" +
                "                          蔡冬娜\n" +
                "                        </a>\n" +
                "                        <div class=\"person_table hidden\">\n" +
                "                          <a class=\"close\" href=\"javascript:void(0);\">\n" +
                "                            ×\n" +
                "                          </a>\n" +
                "                          <table class=\"m_table ggintro\" style=\"width: 100%\">\n" +
                "                            <thead>\n" +
                "                              <tr>\n" +
                "                                <td rowspan=\"2\" class=\"title\">\n" +
                "                                  <h3>\n" +
                "                                    \n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t蔡冬娜\t\t\t\t\t\t\t\t\t\t\t\t\n" +
                "                                  </h3>\n" +
                "                                  <input type=\"hidden\" value=\"{&quot;name&quot;:&quot;\\u8521\\u51ac\\u5a1c&quot;,&quot;id&quot;:1,&quot;pid&quot;:0,&quot;children&quot;:[{&quot;name&quot;:&quot;\\u6f33\\u5dde\\u91d1\\u4e07\\u8fb0\\u6295\\u8d44\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;2.00%&quot;,&quot;id&quot;:2,&quot;pid&quot;:1,&quot;children&quot;:[{&quot;name&quot;:&quot;\\u798f\\u5efa\\u4e07\\u8fb0\\u751f\\u7269\\u79d1\\u6280\\u80a1\\u4efd\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;26.00%&quot;,&quot;id&quot;:3,&quot;pid&quot;:2,&quot;children&quot;:[]}]}]}\"/>\n" +
                "                                </td>\n" +
                "                                <td colspan=\"3\" class=\"jobs\" style=\"width: 150px;\">\n" +
                "                                  副总经理,财务负责人,董事会秘书\n" +
                "                                </td>\n" +
                "                              </tr>\n" +
                "                              <tr>\n" +
                "                                <td class=\"intro\">\n" +
                "                                  女  50岁  大专\n" +
                "                                </td>\n" +
                "                                <td class=\"salary\">\n" +
                "                                  <span>\n" +
                "                                    薪酬：\n" +
                "                                  </span>\n" +
                "                                  19.25万\n" +
                "                                </td>\n" +
                "                                <td>\n" +
                "                                  <div class=\"tipbox_wrap\" style=\"z-index: 99\">\n" +
                "                                    <span>\n" +
                "                                      持股数：\n" +
                "                                    </span>\n" +
                "                                    <span class=\"hold\">\n" +
                "                                      -\n" +
                "                                    </span>\n" +
                "                                  </div>\n" +
                "                                </td>\n" +
                "                              </tr>\n" +
                "                            </thead>\n" +
                "                            <tbody>\n" +
                "                              <tr>\n" +
                "                                <td colspan=\"4\" class=\"mainintro\">\n" +
                "                                  <div>\n" +
                "                                    <p>\n" +
                "                                      蔡冬娜女士,女,1971年出生,汉族,中国国籍,无境外永久居留权,专科学历。曾任漳州信德士集团有限公司财务经理,福建东方食品集团有限公司财务经理、财务总监。现任万辰生物财务总监、董事会秘书。\n" +
                "                                    </p>\n" +
                "                                    <!--\t\t\t\t\t\t\t\t\t\t\t\t\t<p style=\"text-align:right\">职位更新于-->                                    <!--</p>-->                                  </div>\n" +
                "                                </td>\n" +
                "                              </tr>\n" +
                "                            </tbody>\n" +
                "                          </table>\n" +
                "                          <div class=\"stock-right-container hidden\" data-id=\"1\">\n" +
                "                            <img class=\"full-screen right-top\" src=\"//s.thsi.cn/js/basic/stock/img/full-black.png\"/>\n" +
                "                            <span class=\"close right-top hidden\">\n" +
                "                              ×\n" +
                "                            </span>\n" +
                "                            <div class=\"stock-right\" id=\"stock-right-1\">\n" +
                "                            </div>\n" +
                "                            <div class=\"tips hidden\">\n" +
                "                              该信息基于\n" +
                "                              <a class=\"tyc_link\" href=\"https://www.tianyancha.com/\" target=\"_blank\">\n" +
                "                                <strong>\n" +
                "                                  天眼查\n" +
                "                                </strong>\n" +
                "                              </a>\n" +
                "                              数据分析得出，仅供参考，更多信息请到天眼查官网浏览\n" +
                "                            </div>\n" +
                "                          </div>\n" +
                "                          <div class=\"stock-right-btn see-stock-right tc\">\n" +
                "                            查看股权▼\n" +
                "                          </div>\n" +
                "                          <div class=\"stock-right-btn close-stock-right hidden tc\">\n" +
                "                            收起股权▲\n" +
                "                          </div>\n" +
                "                        </div>\n" +
                "                      </span>\n" +
                "                    </td>\n" +
                "                    <td width=\"117px\" class=\"name\">\n" +
                "                      <strong class=\"hltip fl\">\n" +
                "                        法人代表：\n" +
                "                      </strong>\n" +
                "                      <span>\n" +
                "                        <a person_id=\"T737676000\" class=\"turnto\" href=\"javascript:void(0)\">\n" +
                "                          王健坤\n" +
                "                        </a>\n" +
                "                        <div class=\"person_table hidden\">\n" +
                "                          <a class=\"close\" href=\"javascript:void(0);\">\n" +
                "                            ×\n" +
                "                          </a>\n" +
                "                          <table class=\"m_table ggintro\" style=\"width: 100%\">\n" +
                "                            <thead>\n" +
                "                              <tr>\n" +
                "                                <td rowspan=\"2\" class=\"title\">\n" +
                "                                  <h3>\n" +
                "                                    \n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t王健坤\t\t\t\t\t\t\t\t\t\t\t\t\n");
        html.append(
            "                                  </h3>\n" +
                "                                  <input type=\"hidden\" value=\"{&quot;name&quot;:&quot;\\u738b\\u5065\\u5764&quot;,&quot;id&quot;:1,&quot;pid&quot;:0,&quot;children&quot;:[{&quot;name&quot;:&quot;\\u542b\\u7f9e\\u8349(\\u6c5f\\u82cf)\\u98df\\u54c1\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;100.00%&quot;,&quot;id&quot;:2,&quot;pid&quot;:1,&quot;children&quot;:[]}]}\"/>\n" +
                "                                </td>\n" +
                "                                <td colspan=\"3\" class=\"jobs\" style=\"width: 150px;\">\n" +
                "                                  董事,战略委员会委员,董事长,战略委员会主任委员,提名委员会委员\n" +
                "                                </td>\n" +
                "                              </tr>\n" +
                "                              <tr>\n" +
                "                                <td class=\"intro\">\n" +
                "                                  男  53岁  硕士\n" +
                "                                </td>\n" +
                "                                <td class=\"salary\">\n" +
                "                                  <span>\n" +
                "                                    薪酬：\n" +
                "                                  </span>\n" +
                "                                  12万\n" +
                "                                </td>\n" +
                "                                <td>\n" +
                "                                  <div class=\"tipbox_wrap\" style=\"z-index: 99\">\n" +
                "                                    <span>\n" +
                "                                      持股数：\n" +
                "                                    </span>\n" +
                "                                    <span class=\"hold\">\n" +
                "                                      -\n" +
                "                                    </span>\n" +
                "                                  </div>\n" +
                "                                </td>\n" +
                "                              </tr>\n" +
                "                            </thead>\n" +
                "                            <tbody>\n" +
                "                              <tr>\n" +
                "                                <td colspan=\"4\" class=\"mainintro\">\n" +
                "                                  <div>\n" +
                "                                    <p>\n" +
                "                                      王健坤先生,1968年5月出生,中国香港居民,南京理工大学MBA毕业,硕士研究生学历。曾任福建农开发总经理、执行董事,福建东方食品执行董事、总经理,漳州含羞草食品执行董事,江苏含羞草董事长、总经理、执行董事、监事会主席,上海含羞草食品有限公司(后更名为上海含勋商务咨询有限公司)执行董事兼总经理,上海含羞草贸易执行董事兼总经理,青州市澳润福食品有限公司执行董事兼经理。现任江苏省政协委员、福建农开发执行董事、含羞草(江苏)食品有限公司执行董事兼总经理、东方国际(香港)股份有限公司董事、漳州金万辰执行董事兼总经理、南京金万辰执行董事、江苏和正董事、万辰生物董事长。\n" +
                "                                    </p>\n" +
                "                                    <!--\t\t\t\t\t\t\t\t\t\t\t\t\t<p style=\"text-align:right\">此简介更新于-->                                    <!--</p>-->                                  </div>\n" +
                "                                </td>\n" +
                "                              </tr>\n" +
                "                            </tbody>\n" +
                "                          </table>\n" +
                "                          <div class=\"stock-right-container hidden\" data-id=\"2\">\n" +
                "                            <img class=\"full-screen right-top\" src=\"//s.thsi.cn/js/basic/stock/img/full-black.png\"/>\n" +
                "                            <span class=\"close right-top hidden\">\n" +
                "                              ×\n" +
                "                            </span>\n" +
                "                            <div class=\"stock-right\" id=\"stock-right-2\">\n" +
                "                            </div>\n" +
                "                            <div class=\"tips hidden\">\n" +
                "                              该信息基于\n" +
                "                              <a class=\"tyc_link\" href=\"https://www.tianyancha.com/\" target=\"_blank\">\n" +
                "                                <strong>\n" +
                "                                  天眼查\n" +
                "                                </strong>\n" +
                "                              </a>\n" +
                "                              数据分析得出，仅供参考，更多信息请到天眼查官网浏览\n" +
                "                            </div>\n" +
                "                          </div>\n" +
                "                          <div class=\"stock-right-btn see-stock-right tc\">\n" +
                "                            查看股权▼\n" +
                "                          </div>\n" +
                "                          <div class=\"stock-right-btn close-stock-right hidden tc\">\n" +
                "                            收起股权▲\n" +
                "                          </div>\n" +
                "                        </div>\n" +
                "                      </span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                  <tr>\n" +
                "                    <td class=\"name\">\n" +
                "                      <strong class=\"hltip fl\">\n" +
                "                        总 经 理：\n" +
                "                      </strong>\n" +
                "                      <span>\n" +
                "                        <a person_id=\"T737678000\" class=\"turnto\" href=\"javascript:void(0)\">\n" +
                "                          王丽卿\n" +
                "                        </a>\n" +
                "                        <div class=\"person_table hidden\">\n" +
                "                          <a class=\"close\" href=\"javascript:void(0);\">\n" +
                "                            ×\n" +
                "                          </a>\n" +
                "                          <table class=\"m_table ggintro\" style=\"width: 100%\">\n" +
                "                            <thead>\n" +
                "                              <tr>\n" +
                "                                <td rowspan=\"2\" class=\"title\">\n" +
                "                                  <h3>\n" +
                "                                    \n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t王丽卿\t\t\t\t\t\t\t\t\t\t\t\t\n" +
                "                                  </h3>\n" +
                "                                  <input type=\"hidden\" value=\"{&quot;name&quot;:&quot;\\u738b\\u4e3d\\u537f&quot;,&quot;id&quot;:1,&quot;pid&quot;:0,&quot;children&quot;:[{&quot;name&quot;:&quot;\\u6f33\\u5dde\\u91d1\\u4e07\\u8fb0\\u6295\\u8d44\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;37.67%&quot;,&quot;id&quot;:2,&quot;pid&quot;:1,&quot;children&quot;:[{&quot;name&quot;:&quot;\\u798f\\u5efa\\u4e07\\u8fb0\\u751f\\u7269\\u79d1\\u6280\\u80a1\\u4efd\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;30.00%&quot;,&quot;id&quot;:3,&quot;pid&quot;:2,&quot;children&quot;:[{&quot;name&quot;:&quot;\\u5357\\u4eac\\u91d1\\u4e07\\u8fb0\\u751f\\u7269\\u79d1\\u6280\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;100.00%&quot;,&quot;id&quot;:4,&quot;pid&quot;:3,&quot;children&quot;:[]}]}]},{&quot;name&quot;:&quot;\\u6f33\\u5dde\\u4e1c\\u65b9\\u73b0\\u4ee3\\u519c\\u4ea7\\u54c1\\u7269\\u6d41\\u80a1\\u4efd\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;28.57%&quot;,&quot;id&quot;:5,&quot;pid&quot;:1,&quot;children&quot;:[]},{&quot;name&quot;:&quot;\\u6f33\\u5dde\\u5e02\\u9f99\\u6587\\u91d1\\u9675\\u82b1\\u5349\\u7eff\\u5316\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;30.00%&quot;,&quot;id&quot;:6,&quot;pid&quot;:1,&quot;children&quot;:[]}]}\"/>\n" +
                "                                </td>\n" +
                "                                <td colspan=\"3\" class=\"jobs\" style=\"width: 150px;\">\n" +
                "                                  董事,战略委员会委员,总经理,薪酬与考核委员会委员\n" +
                "                                </td>\n" +
                "                              </tr>\n" +
                "                              <tr>\n" +
                "                                <td class=\"intro\">\n" +
                "                                  女  56岁  大专\n" +
                "                                </td>\n" +
                "                                <td class=\"salary\">\n" +
                "                                  <span>\n" +
                "                                    薪酬：\n" +
                "                                  </span>\n" +
                "                                  27.42万\n" +
                "                                </td>\n" +
                "                                <td>\n" +
                "                                  <div class=\"tipbox_wrap\" style=\"z-index: 99\">\n" +
                "                                    <span>\n" +
                "                                      持股数：\n" +
                "                                    </span>\n" +
                "                                    <span class=\"hold\">\n" +
                "                                      -\n" +
                "                                    </span>\n" +
                "                                  </div>\n" +
                "                                </td>\n" +
                "                              </tr>\n" +
                "                            </thead>\n" +
                "                            <tbody>\n" +
                "                              <tr>\n" +
                "                                <td colspan=\"4\" class=\"mainintro\">\n" +
                "                                  <div>\n" +
                "                                    <p>\n" +
                "                                      王丽卿女士,1965年5月出生,中国国籍,无境外永久居留权,专科学历。曾任福建海丽天食品有限公司执行董事、总经理,漳州含羞草贸易执行董事、监事,漳州含羞草食品总经理,漳州金万辰董事、总经理、监事,福建东方食品副总经理,南京金万辰监事、上海含勋商务咨询有限公司监事,上海含羞草贸易监事。现任南京金万辰总经理、南京含羞草食品有限公司监事、福建海丽天食品有限公司监事、万辰生物董事、总经理。\n" +
                "                                    </p>\n" +
                "                                    <!--\t\t\t\t\t\t\t\t\t\t\t\t\t<p style=\"text-align:right\">此简介更新于-->                                    <!--</p>-->                                  </div>\n" +
                "                                </td>\n" +
                "                              </tr>\n" +
                "                            </tbody>\n" +
                "                          </table>\n" +
                "                          <div class=\"stock-right-container hidden\" data-id=\"3\">\n" +
                "                            <img class=\"full-screen right-top\" src=\"//s.thsi.cn/js/basic/stock/img/full-black.png\"/>\n" +
                "                            <span class=\"close right-top hidden\">\n" +
                "                              ×\n" +
                "                            </span>\n" +
                "                            <div class=\"stock-right\" id=\"stock-right-3\">\n" +
                "                            </div>\n" +
                "                            <div class=\"tips hidden\">\n" +
                "                              该信息基于\n" +
                "                              <a class=\"tyc_link\" href=\"https://www.tianyancha.com/\" target=\"_blank\">\n" +
                "                                <strong>\n" +
                "                                  天眼查\n" +
                "                                </strong>\n" +
                "                              </a>\n" +
                "                              数据分析得出，仅供参考，更多信息请到天眼查官网浏览\n" +
                "                            </div>\n" +
                "                          </div>\n" +
                "                          <div class=\"stock-right-btn see-stock-right tc\">\n" +
                "                            查看股权▼\n" +
                "                          </div>\n" +
                "                          <div class=\"stock-right-btn close-stock-right hidden tc\">\n" +
                "                            收起股权▲\n" +
                "                          </div>\n" +
                "                        </div>\n" +
                "                      </span>\n" +
                "                    </td>\n" +
                "                    <td>\n" +
                "                      <strong class=\"hltip fl\">\n" +
                "                        注册资金：\n" +
                "                      </strong>\n" +
                "                      <span>\n" +
                "                        1.54亿元\n" +
                "                      </span>\n" +
                "                    </td>\n" +
                "                    <td>\n" +
                "                      <strong class=\"hltip fl\">\n" +
                "                        员工人数：\n" +
                "                      </strong>\n" +
                "                      <span>\n" +
                "                        1002\n" +
                "                      </span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                  <tr>\n" +
                "                    <td>\n" +
                "                      <strong class=\"hltip fl\">\n" +
                "                        电　　话：\n" +
                "                      </strong>\n" +
                "                      <span>\n" +
                "                        86-0596-6312889\n" +
                "                      </span>\n" +
                "                    </td>\n" +
                "                    <td>\n" +
                "                      <strong class=\"hltip fl\">\n" +
                "                        传　　真：\n" +
                "                      </strong>\n" +
                "                      <span>\n" +
                "                        86-0596-6312860\n" +
                "                      </span>\n" +
                "                    </td>\n" +
                "                    <td>\n" +
                "                      <strong class=\"hltip fl\">\n" +
                "                        邮      编：\n" +
                "                      </strong>\n" +
                "                      <span>\n" +
                "                        363204\n" +
                "                      </span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                  <tr>\n" +
                "                    <td colspan=\"3\">\n" +
                "                      <strong class=\"hltip fl\">\n" +
                "                        办公地址：\n" +
                "                      </strong>\n" +
                "                      <span>\n" +
                "                        福建省漳州市漳浦县漳浦台湾农民创业园\n" +
                "                      </span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                  <tr class=\"intro\">\n" +
                "                    <td colspan=\"3\">\n" +
                "                      <strong class=\"hltip\">\n" +
                "                        公司简介：\n" +
                "                      </strong>\n" +
                "                      <p class=\"tip lh24\">\n" +
                "                        福建万辰生物科技股份有限公司主营业务为鲜品食用菌的研发、工厂化培育与销售。主要产品包括金针菇、蟹味菇和白玉菇等鲜品食用菌。公司现有福建漳州和江苏南京两大生产基地，目前产品主要销往华东地区、华南地区、华中地区，并辐射西南地区、西北地区、华北地区和东北地区。公司食用菌产能位于国内食用菌工厂化生产行业前列。\n" +
                "                      </p>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody>\n" +
                "              </table>\n" +
                "            </div>\n" +
                "          </div>\n" +
                "          <div class=\"ft\">\n" +
                "          </div>\n" +
                "          <div class=\"concept_table proCompete_box none\" id=\"c_pop\">\n" +
                "          </div>\n" +
                "        </div>\n" +
                "        <!-- 高管介绍 -->        <div class=\"m_box z101\" id=\"manager\" stat=\"company_manager\">\n" +
                "          <div class=\"hd\">\n" +
                "            <h2>\n" +
                "              高管介绍\n" +
                "            </h2>\n" +
                "          </div>\n" +
                "          <div class=\"bd\">\n" +
                "            <div class=\"m_tab\">\n" +
                "              <ul>\n" +
                "                <li class=\"cur\" name=\"ml_001\">\n" +
                "                  <a class=\"mlabel\" href=\"javascript:void(0)\" targ=\"ml_001\">\n" +
                "                    董事会(9人)\n" +
                "                  </a>\n" +
                "                </li>\n" +
                "                <li name=\"ml_002\">\n" +
                "                  <a class=\"mlabel\" href=\"javascript:void(0)\" targ=\"ml_002\">\n" +
                "                    监事会(3人)\n" +
                "                  </a>\n" +
                "                </li>\n" +
                "                <li name=\"ml_003\">\n" +
                "                  <a class=\"mlabel\" href=\"javascript:void(0)\" targ=\"ml_003\">\n" +
                "                    高管(6人)\n" +
                "                  </a>\n" +
                "                </li>\n" +
                "              </ul>\n" +
                "            </div>\n" +
                "            <div class=\"m_tab_content\" id=\"ml_001\">\n" +
                "              <table class=\"m_table managelist m_hl\">\n" +
                "                <thead>\n" +
                "                  <tr>\n" +
                "                    <th width=\"5%\">\n" +
                "                      序号\n" +
                "                    </th>\n" +
                "                    <th width=\"8%\">\n" +
                "                      姓名\n" +
                "                    </th>\n" +
                "                    <th width=\"15%\">\n" +
                "                      职务\n" +
                "                    </th>\n" +
                "                    <th width=\"10%\">\n" +
                "                      直接持股数\n" +
                "                    </th>\n" +
                "                    <th width=\"12%\">\n" +
                "                      间接持股数\n" +
                "                    </th>\n" +
                "                    <th width=\"5%\">\n" +
                "                      序号\n" +
                "                    </th>\n" +
                "                    <th width=\"8%\">\n" +
                "                      姓名\n" +
                "                    </th>\n" +
                "                    <th width=\"15%\">\n" +
                "                      职务\n" +
                "                    </th>\n" +
                "                    <th width=\"10%\">\n" +
                "                      直接持股数\n" +
                "                    </th>\n" +
                "                    <th width=\"12%\">\n" +
                "                      间接持股数\n" +
                "                    </th>\n" +
                "                  </tr>\n" +
                "                </thead>\n" +
                "                <tbody>\n" +
                "                  <tr>\n" +
                "                    <th class=\"tc\">\n" +
                "                      1\n" +
                "                    </th>\n" +
                "                    <td class=\"tc name\">\n" +
                "                      <a person_id=\"T737676000\" onclick=\"TA.log({'id':'f10_gszl_ggjs','nj':1})\" class=\"turnto\" href=\"javascript:void(0)\">\n" +
                "                        王健坤\n" +
                "                      </a>\n" +
                "                      <div class=\"person_table hidden\">\n" +
                "                        <a class=\"close\" href=\"javascript:void(0);\">\n" +
                "                          ×\n" +
                "                        </a>\n" +
                "                        <table class=\"m_table ggintro\">\n" +
                "                          <thead>\n" +
                "                            <tr>\n" +
                "                              <td rowspan=\"2\" class=\"title\">\n" +
                "                                <h3>\n" +
                "                                  \n" +
                "                                                    王健坤                                                \n" +
                "                                </h3>\n" +
                "                                <input type=\"hidden\" value=\"{&quot;name&quot;:&quot;\\u738b\\u5065\\u5764&quot;,&quot;id&quot;:1,&quot;pid&quot;:0,&quot;children&quot;:[{&quot;name&quot;:&quot;\\u542b\\u7f9e\\u8349(\\u6c5f\\u82cf)\\u98df\\u54c1\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;100.00%&quot;,&quot;id&quot;:2,&quot;pid&quot;:1,&quot;children&quot;:[]}]}\"/>\n" +
                "                              </td>\n" +
                "                              <td class=\"jobs\" style=\"width: 150px;\">\n" +
                "                                董事长,董事\n" +
                "                              </td>\n" +
                "                              <td class=\"date\" style=\"width: 170px;\">\n" +
                "                                <span>\n" +
                "                                  公告日期：\n" +
                "                                </span>\n" +
                "                                2021-03-26                                            \n" +
                "                              </td>\n" +
                "                              <td class=\"date\">\n" +
                "                                <span>\n" +
                "                                  本届任期：\n" +
                "                                </span>\n" +
                "                                2014-06-10                                                至 2023-07-31\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                              <td class=\"intro\">\n" +
                "                                男53岁硕士\n" +
                "                              </td>\n" +
                "                              <td class=\"salary\">\n" +
                "                                <span>\n" +
                "                                  薪酬：\n" +
                "                                </span>\n" +
                "                                12.00万                                            \n" +
                "                              </td>\n" +
                "                              <td>\n" +
                "                                <div class=\"tipbox_wrap\" style=\"z-index: 99\">\n" +
                "                                  <span>\n" +
                "                                    直接持股数：\n" +
                "                                  </span>\n" +
                "                                  <span class=\"directnum hold\">\n" +
                "                                    -\n" +
                "                                  </span>\n" +
                "                                </div>\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                          </thead>\n" +
                "                          <tbody>\n" +
                "                            <tr>\n" +
                "                              <td colspan=\"4\" class=\"mainintro\">\n" +
                "                                <div>\n" +
                "                                  <p>\n" +
                "                                    王健坤先生,1968年5月出生,中国香港居民,南京理工大学MBA毕业,硕士研究生学历。曾任福建农开发总经理、执行董事,福建东方食品执行董事、总经理,漳州含羞草食品执行董事,江苏含羞草董事长、总经理、执行董事、监事会主席,上海含羞草食品有限公司(后更名为上海含勋商务咨询有限公司)执行董事兼总经理,上海含羞草贸易执行董事兼总经理,青州市澳润福食品有限公司执行董事兼经理。现任江苏省政协委员、福建农开发执行董事、含羞草(江苏)食品有限公司执行董事兼总经理、东方国际(中国香港)股份有限公司董事、漳州金万辰执行董事兼总经理、南京金万辰执行董事、江苏和正董事、万辰生物董事长。\n" +
                "                                  </p>\n" +
                "                                  <p style=\"text-align:right\">\n" +
                "                                    此简介更新于2021-03-26\n" +
                "                                  </p>\n" +
                "                                </div>\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                          </tbody>\n" +
                "                        </table>\n" +
                "                        <div class=\"stock-right-container hidden\" data-id=\"4\">\n" +
                "                          <img class=\"full-screen right-top\" src=\"//s.thsi.cn/js/basic/stock/img/full-black.png\"/>\n" +
                "                          <span class=\"close right-top hidden\">\n" +
                "                            ×\n" +
                "                          </span>\n" +
                "                          <div class=\"stock-right\" id=\"stock-right-4\">\n" +
                "                          </div>\n" +
                "                          <div class=\"tips hidden\">\n" +
                "                            该信息基于\n" +
                "                            <a class=\"tyc_link\" href=\"https://www.tianyancha.com/\" target=\"_blank\">\n" +
                "                              <strong>\n" +
                "                                天眼查\n" +
                "                              </strong>\n" +
                "                            </a>\n" +
                "                            数据分析得出，仅供参考，更多信息请到天眼查官网浏览\n" +
                "                          </div>\n" +
                "                        </div>\n" +
                "                        <div class=\"stock-right-btn see-stock-right tc\">\n" +
                "                          查看股权▼\n" +
                "                        </div>\n" +
                "                        <div class=\"stock-right-btn close-stock-right hidden tc\">\n" +
                "                          收起股权▲\n" +
                "                        </div>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                    <td class=\"tl\">\n" +
                "                      董事长,董事\n" +
                "                    </td>\n" +
                "                    <td>\n" +
                "                      <div>\n" +
                "                        <span>\n" +
                "                          --\n" +
                "                        </span>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                    <td>\n" +
                "                      <div>\n" +
                "                        <span>\n" +
                "                          --\n" +
                "                        </span>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                    <th class=\"tc\">\n" +
                "                      2\n" +
                "                    </th>\n" +
                "                    <td class=\"tc name\">\n" +
                "                      <a person_id=\"T737678000\" onclick=\"TA.log({'id':'f10_gszl_ggjs','nj':1})\" class=\"turnto\" href=\"javascript:void(0)\">\n" +
                "                        王丽卿\n" +
                "                      </a>\n" +
                "                      <div class=\"person_table hidden\">\n" +
                "                        <a class=\"close\" href=\"javascript:void(0);\">\n" +
                "                          ×\n" +
                "                        </a>\n" +
                "                        <table class=\"m_table ggintro\">\n" +
                "                          <thead>\n" +
                "                            <tr>\n" +
                "                              <td rowspan=\"2\" class=\"title\">\n" +
                "                                <h3>\n" +
                "                                  \n" +
                "                                                    王丽卿                                                \n" +
                "                                </h3>\n" +
                "                                <input type=\"hidden\" value=\"{&quot;name&quot;:&quot;\\u738b\\u4e3d\\u537f&quot;,&quot;id&quot;:1,&quot;pid&quot;:0,&quot;children&quot;:[{&quot;name&quot;:&quot;\\u6f33\\u5dde\\u91d1\\u4e07\\u8fb0\\u6295\\u8d44\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;37.67%&quot;,&quot;id&quot;:2,&quot;pid&quot;:1,&quot;children&quot;:[{&quot;name&quot;:&quot;\\u798f\\u5efa\\u4e07\\u8fb0\\u751f\\u7269\\u79d1\\u6280\\u80a1\\u4efd\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;30.00%&quot;,&quot;id&quot;:3,&quot;pid&quot;:2,&quot;children&quot;:[{&quot;name&quot;:&quot;\\u5357\\u4eac\\u91d1\\u4e07\\u8fb0\\u751f\\u7269\\u79d1\\u6280\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;100.00%&quot;,&quot;id&quot;:4,&quot;pid&quot;:3,&quot;children&quot;:[]}]}]},{&quot;name&quot;:&quot;\\u6f33\\u5dde\\u4e1c\\u65b9\\u73b0\\u4ee3\\u519c\\u4ea7\\u54c1\\u7269\\u6d41\\u80a1\\u4efd\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;28.57%&quot;,&quot;id&quot;:5,&quot;pid&quot;:1,&quot;children&quot;:[]},{&quot;name&quot;:&quot;\\u6f33\\u5dde\\u5e02\\u9f99\\u6587\\u91d1\\u9675\\u82b1\\u5349\\u7eff\\u5316\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;30.00%&quot;,&quot;id&quot;:6,&quot;pid&quot;:1,&quot;children&quot;:[]}]}\"/>\n" +
                "                              </td>\n" +
                "                              <td class=\"jobs\" style=\"width: 150px;\">\n" +
                "                                董事\n" +
                "                              </td>\n" +
                "                              <td class=\"date\" style=\"width: 170px;\">\n" +
                "                                <span>\n" +
                "                                  公告日期：\n" +
                "                                </span>\n" +
                "                                2021-03-26                                            \n" +
                "                              </td>\n" +
                "                              <td class=\"date\">\n" +
                "                                <span>\n" +
                "                                  本届任期：\n" +
                "                                </span>\n" +
                "                                2014-06-10                                                至 2023-07-31\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                              <td class=\"intro\">\n" +
                "                                女56岁大专\n" +
                "                              </td>\n" +
                "                              <td class=\"salary\">\n" +
                "                                <span>\n" +
                "                                  薪酬：\n" +
                "                                </span>\n" +
                "                                27.42万                                            \n" +
                "                              </td>\n" +
                "                              <td>\n" +
                "                                <div class=\"tipbox_wrap\" style=\"z-index: 99\">\n" +
                "                                  <span>\n" +
                "                                    直接持股数：\n" +
                "                                  </span>\n" +
                "                                  <span class=\"directnum hold\">\n" +
                "                                    -\n" +
                "                                  </span>\n" +
                "                                </div>\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                          </thead>\n" +
                "                          <tbody>\n" +
                "                            <tr>\n" +
                "                              <td colspan=\"4\" class=\"mainintro\">\n" +
                "                                <div>\n" +
                "                                  <p>\n" +
                "                                    王丽卿女士,1965年5月出生,中国国籍,无境外永久居留权,专科学历。曾任福建海丽天食品有限公司执行董事、总经理,漳州含羞草贸易执行董事、监事,漳州含羞草食品总经理,漳州金万辰董事、总经理、监事,福建东方食品副总经理,南京金万辰监事、上海含勋商务咨询有限公司监事,上海含羞草贸易监事。现任南京金万辰总经理、南京含羞草食品有限公司监事、福建海丽天食品有限公司监事、万辰生物董事、总经理。\n" +
                "                                  </p>\n" +
                "                                  <p style=\"text-align:right\">\n" +
                "                                    此简介更新于2021-03-26\n" +
                "                                  </p>\n" +
                "                                </div>\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                          </tbody>\n" +
                "                        </table>\n" +
                "                        <div class=\"stock-right-container hidden\" data-id=\"5\">\n" +
                "                          <img class=\"full-screen right-top\" src=\"//s.thsi.cn/js/basic/stock/img/full-black.png\"/>\n" +
                "                          <span class=\"close right-top hidden\">\n" +
                "                            ×\n" +
                "                          </span>\n" +
                "                          <div class=\"stock-right\" id=\"stock-right-5\">\n" +
                "                          </div>\n" +
                "                          <div class=\"tips hidden\">\n" +
                "                            该信息基于\n" +
                "                            <a class=\"tyc_link\" href=\"https://www.tianyancha.com/\" target=\"_blank\">\n" +
                "                              <strong>\n" +
                "                                天眼查\n" +
                "                              </strong>\n" +
                "                            </a>\n" +
                "                            数据分析得出，仅供参考，更多信息请到天眼查官网浏览\n" +
                "                          </div>\n" +
                "                        </div>\n" +
                "                        <div class=\"stock-right-btn see-stock-right tc\">\n" +
                "                          查看股权▼\n" +
                "                        </div>\n" +
                "                        <div class=\"stock-right-btn close-stock-right hidden tc\">\n");
        html.append(
            "                          收起股权▲\n" +
                "                        </div>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                    <td class=\"tl\">\n" +
                "                      董事\n" +
                "                    </td>\n" +
                "                    <td>\n" +
                "                      <div>\n" +
                "                        <span>\n" +
                "                          --\n" +
                "                        </span>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                    <td>\n" +
                "                      <div>\n" +
                "                        <span>\n" +
                "                          1169万(估)\n" +
                "                        </span>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                  <tr>\n" +
                "                    <th class=\"tc\">\n" +
                "                      3\n" +
                "                    </th>\n" +
                "                    <td class=\"tc name\">\n" +
                "                      <a person_id=\"T737680000\" onclick=\"TA.log({'id':'f10_gszl_ggjs','nj':1})\" class=\"turnto\" href=\"javascript:void(0)\">\n" +
                "                        李博\n" +
                "                      </a>\n" +
                "                      <div class=\"person_table hidden\">\n" +
                "                        <a class=\"close\" href=\"javascript:void(0);\">\n" +
                "                          ×\n" +
                "                        </a>\n" +
                "                        <table class=\"m_table ggintro\">\n" +
                "                          <thead>\n" +
                "                            <tr>\n" +
                "                              <td rowspan=\"2\" class=\"title\">\n" +
                "                                <h3>\n" +
                "                                  \n" +
                "                                                    李博                                                \n" +
                "                                </h3>\n" +
                "                                <input type=\"hidden\" value=\"[]\"/>\n" +
                "                              </td>\n" +
                "                              <td class=\"jobs\" style=\"width: 150px;\">\n" +
                "                                董事\n" +
                "                              </td>\n" +
                "                              <td class=\"date\" style=\"width: 170px;\">\n" +
                "                                <span>\n" +
                "                                  公告日期：\n" +
                "                                </span>\n" +
                "                                2021-03-26                                            \n" +
                "                              </td>\n" +
                "                              <td class=\"date\">\n" +
                "                                <span>\n" +
                "                                  本届任期：\n" +
                "                                </span>\n" +
                "                                2014-06-10                                                至 2023-07-31\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                              <td class=\"intro\">\n" +
                "                                男38岁硕士\n" +
                "                              </td>\n" +
                "                              <td class=\"salary\">\n" +
                "                                <span>\n" +
                "                                  薪酬：\n" +
                "                                </span>\n" +
                "                                25.81万                                            \n" +
                "                              </td>\n" +
                "                              <td>\n" +
                "                                <div class=\"tipbox_wrap\" style=\"z-index: 99\">\n" +
                "                                  <span>\n" +
                "                                    直接持股数：\n" +
                "                                  </span>\n" +
                "                                  <span class=\"directnum hold\">\n" +
                "                                    -\n" +
                "                                  </span>\n" +
                "                                </div>\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                          </thead>\n" +
                "                          <tbody>\n" +
                "                            <tr>\n" +
                "                              <td colspan=\"4\" class=\"mainintro\">\n" +
                "                                <div>\n" +
                "                                  <p>\n" +
                "                                    李博先生,1983年4月出生,中国国籍,无境外永久居留权,食用菌遗传学专业硕士研究生学历。曾任浙江瑞丰农业发展有限公司(现已更名为“浙江瑞丰农业开发股份有限公司”)生产部技术总监,江苏康盛农业发展有限公司副总经理。现任福建万辰生物科技股份有限公司董事、副总经理。曾参与编辑《中国食用菌菌种学》、《食用菌工厂化栽培实践》,作为主要起草人之一参与“金针菇工厂化栽培技术规范”标准制订,主持福建省种业创新与产业化工程项目“金针菇工厂化专用品种选育及液化菌种产业化示范推广”,参与漳州市科技局两化融合项目“漳州市食用菌行业智能测控系统建设示范”和科技部政策引导类计划项目“食用菌工厂化生产科技示范”等科研项目。\n" +
                "                                  </p>\n" +
                "                                  <p style=\"text-align:right\">\n" +
                "                                    此简介更新于2021-03-26\n" +
                "                                  </p>\n" +
                "                                </div>\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                          </tbody>\n" +
                "                        </table>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                    <td class=\"tl\">\n" +
                "                      董事\n" +
                "                    </td>\n" +
                "                    <td>\n" +
                "                      <div>\n" +
                "                        <span>\n" +
                "                          --\n" +
                "                        </span>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                    <td>\n" +
                "                      <div>\n" +
                "                        <span>\n" +
                "                          119.7万(估)\n" +
                "                        </span>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                    <th class=\"tc\">\n" +
                "                      4\n" +
                "                    </th>\n" +
                "                    <td class=\"tc name\">\n" +
                "                      <a person_id=\"T737686000\" onclick=\"TA.log({'id':'f10_gszl_ggjs','nj':1})\" class=\"turnto\" href=\"javascript:void(0)\">\n" +
                "                        王泽宁\n" +
                "                      </a>\n" +
                "                      <div class=\"person_table hidden\">\n" +
                "                        <a class=\"close\" href=\"javascript:void(0);\">\n" +
                "                          ×\n" +
                "                        </a>\n" +
                "                        <table class=\"m_table ggintro\">\n" +
                "                          <thead>\n" +
                "                            <tr>\n" +
                "                              <td rowspan=\"2\" class=\"title\">\n" +
                "                                <h3>\n" +
                "                                  \n" +
                "                                                    王泽宁                                                \n" +
                "                                </h3>\n" +
                "                                <input type=\"hidden\" value=\"{&quot;name&quot;:&quot;\\u738b\\u6cfd\\u5b81&quot;,&quot;id&quot;:1,&quot;pid&quot;:0,&quot;children&quot;:[{&quot;name&quot;:&quot;\\u798f\\u5efa\\u542b\\u7f9e\\u8349\\u519c\\u4e1a\\u5f00\\u53d1\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;80.00%&quot;,&quot;id&quot;:2,&quot;pid&quot;:1,&quot;children&quot;:[{&quot;name&quot;:&quot;\\u6c5f\\u82cf\\u542b\\u7f9e\\u8349\\u519c\\u4e1a\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;100.00%&quot;,&quot;id&quot;:5,&quot;pid&quot;:2,&quot;children&quot;:[]}]},{&quot;name&quot;:&quot;\\u798f\\u5efa\\u4e07\\u8fb0\\u751f\\u7269\\u79d1\\u6280\\u80a1\\u4efd\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;10.00%&quot;,&quot;id&quot;:3,&quot;pid&quot;:1,&quot;children&quot;:[{&quot;name&quot;:&quot;\\u5357\\u4eac\\u91d1\\u4e07\\u8fb0\\u751f\\u7269\\u79d1\\u6280\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;100.00%&quot;,&quot;id&quot;:6,&quot;pid&quot;:3,&quot;children&quot;:[]}]},{&quot;name&quot;:&quot;\\u6f33\\u5dde\\u91d1\\u4e07\\u8fb0\\u6295\\u8d44\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;53.33%&quot;,&quot;id&quot;:4,&quot;pid&quot;:1,&quot;children&quot;:[]}]}\"/>\n" +
                "                              </td>\n" +
                "                              <td class=\"jobs\" style=\"width: 150px;\">\n" +
                "                                董事\n" +
                "                              </td>\n" +
                "                              <td class=\"date\" style=\"width: 170px;\">\n" +
                "                                <span>\n" +
                "                                  公告日期：\n" +
                "                                </span>\n" +
                "                                2021-03-26                                            \n" +
                "                              </td>\n" +
                "                              <td class=\"date\">\n" +
                "                                <span>\n" +
                "                                  本届任期：\n" +
                "                                </span>\n" +
                "                                2015-03-31                                                至 2023-07-31\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                              <td class=\"intro\">\n" +
                "                                男28岁硕士\n" +
                "                              </td>\n" +
                "                              <td class=\"salary\">\n" +
                "                                <span>\n" +
                "                                  薪酬：\n" +
                "                                </span>\n" +
                "                                8.220万                                            \n" +
                "                              </td>\n" +
                "                              <td>\n" +
                "                                <div class=\"tipbox_wrap\" style=\"z-index: 99\">\n" +
                "                                  <span>\n" +
                "                                    直接持股数：\n" +
                "                                  </span>\n" +
                "                                  <span class=\"directnum hold\">\n" +
                "                                    780万\n" +
                "                                  </span>\n" +
                "                                </div>\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                          </thead>\n" +
                "                          <tbody>\n" +
                "                            <tr>\n" +
                "                              <td colspan=\"4\" class=\"mainintro\">\n" +
                "                                <div>\n" +
                "                                  <p>\n" +
                "                                    王泽宁先生,1993年9月出生,中国国籍,无境外永久居留权,硕士研究生学历。曾任江苏含羞草执行董事兼总经理。现任福建万辰生物科技股份有限公司董事、副总经理。\n" +
                "                                  </p>\n" +
                "                                  <p style=\"text-align:right\">\n" +
                "                                    此简介更新于2021-03-26\n" +
                "                                  </p>\n" +
                "                                </div>\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                          </tbody>\n" +
                "                        </table>\n" +
                "                        <div class=\"stock-right-container hidden\" data-id=\"6\">\n" +
                "                          <img class=\"full-screen right-top\" src=\"//s.thsi.cn/js/basic/stock/img/full-black.png\"/>\n" +
                "                          <span class=\"close right-top hidden\">\n" +
                "                            ×\n" +
                "                          </span>\n" +
                "                          <div class=\"stock-right\" id=\"stock-right-6\">\n" +
                "                          </div>\n" +
                "                          <div class=\"tips hidden\">\n" +
                "                            该信息基于\n" +
                "                            <a class=\"tyc_link\" href=\"https://www.tianyancha.com/\" target=\"_blank\">\n" +
                "                              <strong>\n" +
                "                                天眼查\n" +
                "                              </strong>\n" +
                "                            </a>\n" +
                "                            数据分析得出，仅供参考，更多信息请到天眼查官网浏览\n" +
                "                          </div>\n" +
                "                        </div>\n" +
                "                        <div class=\"stock-right-btn see-stock-right tc\">\n" +
                "                          查看股权▼\n" +
                "                        </div>\n" +
                "                        <div class=\"stock-right-btn close-stock-right hidden tc\">\n" +
                "                          收起股权▲\n" +
                "                        </div>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                    <td class=\"tl\">\n" +
                "                      董事\n" +
                "                    </td>\n" +
                "                    <td>\n" +
                "                      <div>\n" +
                "                        <span>\n" +
                "                          780万\n" +
                "                        </span>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                    <td>\n" +
                "                      <div>\n" +
                "                        <span>\n" +
                "                          4872万(估)\n" +
                "                        </span>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                  <tr>\n" +
                "                    <th class=\"tc\">\n" +
                "                      5\n" +
                "                    </th>\n" +
                "                    <td class=\"tc name\">\n" +
                "                      <a person_id=\"T737683000\" onclick=\"TA.log({'id':'f10_gszl_ggjs','nj':1})\" class=\"turnto\" href=\"javascript:void(0)\">\n" +
                "                        林该春\n" +
                "                      </a>\n" +
                "                      <div class=\"person_table hidden\">\n" +
                "                        <a class=\"close\" href=\"javascript:void(0);\">\n" +
                "                          ×\n" +
                "                        </a>\n" +
                "                        <table class=\"m_table ggintro\">\n" +
                "                          <thead>\n" +
                "                            <tr>\n" +
                "                              <td rowspan=\"2\" class=\"title\">\n" +
                "                                <h3>\n" +
                "                                  \n" +
                "                                                    林该春                                                \n" +
                "                                </h3>\n" +
                "                                <input type=\"hidden\" value=\"{&quot;name&quot;:&quot;\\u6797\\u8be5\\u6625&quot;,&quot;id&quot;:1,&quot;pid&quot;:0,&quot;children&quot;:[{&quot;name&quot;:&quot;\\u6f33\\u5dde\\u542b\\u7f9e\\u8349\\u8fdb\\u51fa\\u53e3\\u8d38\\u6613\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;99.00%&quot;,&quot;id&quot;:2,&quot;pid&quot;:1,&quot;children&quot;:[]},{&quot;name&quot;:&quot;\\u6f33\\u5dde\\u4e1c\\u65b9\\u6052\\u4fe1\\u62c5\\u4fdd\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;99.00%&quot;,&quot;id&quot;:3,&quot;pid&quot;:1,&quot;children&quot;:[]},{&quot;name&quot;:&quot;\\u798f\\u5efa\\u4e1c\\u65b9\\u98df\\u54c1\\u96c6\\u56e2\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;100.00%&quot;,&quot;id&quot;:4,&quot;pid&quot;:1,&quot;children&quot;:[{&quot;name&quot;:&quot;\\u5357\\u4eac\\u542b\\u7f9e\\u8349\\u98df\\u54c1\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;100.00%&quot;,&quot;id&quot;:5,&quot;pid&quot;:4,&quot;children&quot;:[]},{&quot;name&quot;:&quot;\\u4e0a\\u6d77\\u542b\\u7f9e\\u8349\\u8d38\\u6613\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;99.00%&quot;,&quot;id&quot;:7,&quot;pid&quot;:4,&quot;children&quot;:[]}]},{&quot;name&quot;:&quot;\\u6f33\\u5dde\\u542b\\u7f9e\\u8349\\u98df\\u54c1\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;1.00%&quot;,&quot;id&quot;:6,&quot;pid&quot;:1,&quot;children&quot;:[]},{&quot;name&quot;:&quot;\\u6c5f\\u82cf\\u4e07\\u5bb8\\u7f6e\\u4e1a\\u6295\\u8d44\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;99.00%&quot;,&quot;id&quot;:8,&quot;pid&quot;:1,&quot;children&quot;:[]},{&quot;name&quot;:&quot;\\u4e0a\\u6d77\\u542b\\u52cb\\u5546\\u52a1\\u54a8\\u8be2\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;28.50%&quot;,&quot;id&quot;:9,&quot;pid&quot;:1,&quot;children&quot;:[]},{&quot;name&quot;:&quot;\\u6c5f\\u82cf\\u96f6\\u98df\\u5de5\\u574a\\u8fde\\u9501\\u98df\\u54c1\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;99.00%&quot;,&quot;id&quot;:10,&quot;pid&quot;:1,&quot;children&quot;:[]}]}\"/>\n" +
                "                              </td>\n" +
                "                              <td class=\"jobs\" style=\"width: 150px;\">\n" +
                "                                董事\n" +
                "                              </td>\n" +
                "                              <td class=\"date\" style=\"width: 170px;\">\n" +
                "                                <span>\n" +
                "                                  公告日期：\n" +
                "                                </span>\n" +
                "                                2021-03-26                                            \n" +
                "                              </td>\n" +
                "                              <td class=\"date\">\n" +
                "                                <span>\n" +
                "                                  本届任期：\n" +
                "                                </span>\n" +
                "                                2014-06-10                                                至 2023-07-31\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                              <td class=\"intro\">\n" +
                "                                女54岁大专\n" +
                "                              </td>\n" +
                "                              <td class=\"salary\">\n" +
                "                                <span>\n" +
                "                                  薪酬：\n" +
                "                                </span>\n" +
                "                                6.000万                                            \n" +
                "                              </td>\n" +
                "                              <td>\n" +
                "                                <div class=\"tipbox_wrap\" style=\"z-index: 99\">\n" +
                "                                  <span>\n" +
                "                                    直接持股数：\n" +
                "                                  </span>\n" +
                "                                  <span class=\"directnum hold\">\n" +
                "                                    -\n" +
                "                                  </span>\n" +
                "                                </div>\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                          </thead>\n" +
                "                          <tbody>\n" +
                "                            <tr>\n" +
                "                              <td colspan=\"4\" class=\"mainintro\">\n" +
                "                                <div>\n" +
                "                                  <p>\n" +
                "                                    林该春女士,1967年11月出生,中国国籍,无境外永久居留权,专科学历。曾任福建农开发执行董事兼总经理,漳州含羞草贸易执行董事,漳州金万辰执行董事兼总经理,南京金万辰执行董事,江苏含羞草监事会主席、董事长、执行董事兼总经理,江苏万宸家园物业管理有限公司执行董事兼总经理,上海含羞草食品有限公司监事,上海含羞草贸易执行董事,福建东方食品总裁助理,上海含勋商务咨询有限公司执行董事。现任福建东方食品执行董事,南京含羞草执行董事,漳州金万辰监事,江苏含羞草执行董事兼总经理,漳州含羞草食品执行董事、总经理,漳州含羞草贸易执行董事,江苏万宸置业投资有限公司执行董事,万辰生物董事。\n" +
                "                                  </p>\n" +
                "                                  <p style=\"text-align:right\">\n" +
                "                                    此简介更新于2021-03-26\n" +
                "                                  </p>\n" +
                "                                </div>\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                          </tbody>\n" +
                "                        </table>\n" +
                "                        <div class=\"stock-right-container hidden\" data-id=\"7\">\n" +
                "                          <img class=\"full-screen right-top\" src=\"//s.thsi.cn/js/basic/stock/img/full-black.png\"/>\n" +
                "                          <span class=\"close right-top hidden\">\n" +
                "                            ×\n" +
                "                          </span>\n" +
                "                          <div class=\"stock-right\" id=\"stock-right-7\">\n" +
                "                          </div>\n" +
                "                          <div class=\"tips hidden\">\n" +
                "                            该信息基于\n" +
                "                            <a class=\"tyc_link\" href=\"https://www.tianyancha.com/\" target=\"_blank\">\n" +
                "                              <strong>\n" +
                "                                天眼查\n" +
                "                              </strong>\n" +
                "                            </a>\n" +
                "                            数据分析得出，仅供参考，更多信息请到天眼查官网浏览\n" +
                "                          </div>\n" +
                "                        </div>\n" +
                "                        <div class=\"stock-right-btn see-stock-right tc\">\n" +
                "                          查看股权▼\n" +
                "                        </div>\n" +
                "                        <div class=\"stock-right-btn close-stock-right hidden tc\">\n" +
                "                          收起股权▲\n" +
                "                        </div>\n" +
                "                      </div>\n" +
                "                    </td>\n");
        html.append(
            "                    <td class=\"tl\">\n" +
                "                      董事\n" +
                "                    </td>\n" +
                "                    <td>\n" +
                "                      <div>\n" +
                "                        <span>\n" +
                "                          --\n" +
                "                        </span>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                    <td>\n" +
                "                      <div>\n" +
                "                        <span>\n" +
                "                          15.99万(估)\n" +
                "                        </span>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                    <th class=\"tc\">\n" +
                "                      6\n" +
                "                    </th>\n" +
                "                    <td class=\"tc name\">\n" +
                "                      <a person_id=\"T737685000\" onclick=\"TA.log({'id':'f10_gszl_ggjs','nj':1})\" class=\"turnto\" href=\"javascript:void(0)\">\n" +
                "                        陈文柱\n" +
                "                      </a>\n" +
                "                      <div class=\"person_table hidden\">\n" +
                "                        <a class=\"close\" href=\"javascript:void(0);\">\n" +
                "                          ×\n" +
                "                        </a>\n" +
                "                        <table class=\"m_table ggintro\">\n" +
                "                          <thead>\n" +
                "                            <tr>\n" +
                "                              <td rowspan=\"2\" class=\"title\">\n" +
                "                                <h3>\n" +
                "                                  \n" +
                "                                                    陈文柱                                                \n" +
                "                                </h3>\n" +
                "                                <input type=\"hidden\" value=\"{&quot;name&quot;:&quot;\\u9648\\u6587\\u67f1&quot;,&quot;id&quot;:1,&quot;pid&quot;:0,&quot;children&quot;:[{&quot;name&quot;:&quot;\\u5357\\u4eac\\u5361\\u7f57\\u4f9d\\u793c\\u54c1\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;65.00%&quot;,&quot;id&quot;:2,&quot;pid&quot;:1,&quot;children&quot;:[]},{&quot;name&quot;:&quot;\\u798f\\u5efa\\u542b\\u7f9e\\u8349\\u519c\\u4e1a\\u5f00\\u53d1\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;19.00%&quot;,&quot;id&quot;:3,&quot;pid&quot;:1,&quot;children&quot;:[{&quot;name&quot;:&quot;\\u798f\\u5efa\\u4e07\\u8fb0\\u751f\\u7269\\u79d1\\u6280\\u80a1\\u4efd\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;60.00%&quot;,&quot;id&quot;:4,&quot;pid&quot;:3,&quot;children&quot;:[{&quot;name&quot;:&quot;\\u5357\\u4eac\\u91d1\\u4e07\\u8fb0\\u751f\\u7269\\u79d1\\u6280\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;100.00%&quot;,&quot;id&quot;:5,&quot;pid&quot;:4,&quot;children&quot;:[]}]}]},{&quot;name&quot;:&quot;\\u5357\\u4eac\\u534e\\u9020\\u5851\\u4e1a\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;30.00%&quot;,&quot;id&quot;:6,&quot;pid&quot;:1,&quot;children&quot;:[]}]}\"/>\n" +
                "                              </td>\n" +
                "                              <td class=\"jobs\" style=\"width: 150px;\">\n" +
                "                                董事\n" +
                "                              </td>\n" +
                "                              <td class=\"date\" style=\"width: 170px;\">\n" +
                "                                <span>\n" +
                "                                  公告日期：\n" +
                "                                </span>\n" +
                "                                2021-03-26                                            \n" +
                "                              </td>\n" +
                "                              <td class=\"date\">\n" +
                "                                <span>\n" +
                "                                  本届任期：\n" +
                "                                </span>\n" +
                "                                2015-03-31                                                至 2023-07-31\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                              <td class=\"intro\">\n" +
                "                                男41岁高中\n" +
                "                              </td>\n" +
                "                              <td class=\"salary\">\n" +
                "                                <span>\n" +
                "                                  薪酬：\n" +
                "                                </span>\n" +
                "                                6.000万                                            \n" +
                "                              </td>\n" +
                "                              <td>\n" +
                "                                <div class=\"tipbox_wrap\" style=\"z-index: 99\">\n" +
                "                                  <span>\n" +
                "                                    直接持股数：\n" +
                "                                  </span>\n" +
                "                                  <span class=\"directnum hold\">\n" +
                "                                    -\n" +
                "                                  </span>\n" +
                "                                </div>\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                          </thead>\n" +
                "                          <tbody>\n" +
                "                            <tr>\n" +
                "                              <td colspan=\"4\" class=\"mainintro\">\n" +
                "                                <div>\n" +
                "                                  <p>\n" +
                "                                    陈文柱先生,1980年2月出生,中国国籍,无境外永久居留权,高中学历。曾任南京含羞草采购经理,江苏含羞草董事,福建农开发执行董事。现任南京卡罗依礼品有限公司执行董事、南京华造塑业有限公司监事、福建农开发总经理、万辰生物董事。\n" +
                "                                  </p>\n" +
                "                                  <p style=\"text-align:right\">\n" +
                "                                    此简介更新于2021-03-26\n" +
                "                                  </p>\n" +
                "                                </div>\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                          </tbody>\n" +
                "                        </table>\n" +
                "                        <div class=\"stock-right-container hidden\" data-id=\"8\">\n" +
                "                          <img class=\"full-screen right-top\" src=\"//s.thsi.cn/js/basic/stock/img/full-black.png\"/>\n" +
                "                          <span class=\"close right-top hidden\">\n" +
                "                            ×\n" +
                "                          </span>\n" +
                "                          <div class=\"stock-right\" id=\"stock-right-8\">\n" +
                "                          </div>\n" +
                "                          <div class=\"tips hidden\">\n" +
                "                            该信息基于\n" +
                "                            <a class=\"tyc_link\" href=\"https://www.tianyancha.com/\" target=\"_blank\">\n" +
                "                              <strong>\n" +
                "                                天眼查\n" +
                "                              </strong>\n" +
                "                            </a>\n" +
                "                            数据分析得出，仅供参考，更多信息请到天眼查官网浏览\n" +
                "                          </div>\n" +
                "                        </div>\n" +
                "                        <div class=\"stock-right-btn see-stock-right tc\">\n" +
                "                          查看股权▼\n" +
                "                        </div>\n" +
                "                        <div class=\"stock-right-btn close-stock-right hidden tc\">\n" +
                "                          收起股权▲\n" +
                "                        </div>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                    <td class=\"tl\">\n" +
                "                      董事\n" +
                "                    </td>\n" +
                "                    <td>\n" +
                "                      <div>\n" +
                "                        <span>\n" +
                "                          --\n" +
                "                        </span>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                    <td>\n" +
                "                      <div>\n" +
                "                        <span>\n" +
                "                          778万(估)\n" +
                "                        </span>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                  <tr>\n" +
                "                    <th class=\"tc\">\n" +
                "                      7\n" +
                "                    </th>\n" +
                "                    <td class=\"tc name\">\n" +
                "                      <a person_id=\"T115269200\" onclick=\"TA.log({'id':'f10_gszl_ggjs','nj':1})\" class=\"turnto\" href=\"javascript:void(0)\">\n" +
                "                        童锦治\n" +
                "                      </a>\n" +
                "                      <div class=\"person_table hidden\">\n" +
                "                        <a class=\"close\" href=\"javascript:void(0);\">\n" +
                "                          ×\n" +
                "                        </a>\n" +
                "                        <table class=\"m_table ggintro\">\n" +
                "                          <thead>\n" +
                "                            <tr>\n" +
                "                              <td rowspan=\"2\" class=\"title\">\n" +
                "                                <h3>\n" +
                "                                  \n" +
                "                                                    童锦治                                                \n" +
                "                                </h3>\n" +
                "                                <input type=\"hidden\" value=\"[]\"/>\n" +
                "                              </td>\n" +
                "                              <td class=\"jobs\" style=\"width: 150px;\">\n" +
                "                                独立董事\n" +
                "                              </td>\n" +
                "                              <td class=\"date\" style=\"width: 170px;\">\n" +
                "                                <span>\n" +
                "                                  公告日期：\n" +
                "                                </span>\n" +
                "                                2021-03-26                                            \n" +
                "                              </td>\n" +
                "                              <td class=\"date\">\n" +
                "                                <span>\n" +
                "                                  本届任期：\n" +
                "                                </span>\n" +
                "                                2020-08-01                                                至 2023-07-31\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                              <td class=\"intro\">\n" +
                "                                女58岁博士\n" +
                "                              </td>\n" +
                "                              <td class=\"salary\">\n" +
                "                                <span>\n" +
                "                                  薪酬：\n" +
                "                                </span>\n" +
                "                                3.000万                                            \n" +
                "                              </td>\n" +
                "                              <td>\n" +
                "                                <div class=\"tipbox_wrap\" style=\"z-index: 99\">\n" +
                "                                  <span>\n" +
                "                                    直接持股数：\n" +
                "                                  </span>\n" +
                "                                  <span class=\"directnum hold\">\n" +
                "                                    -\n" +
                "                                  </span>\n" +
                "                                </div>\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                          </thead>\n" +
                "                          <tbody>\n" +
                "                            <tr>\n" +
                "                              <td colspan=\"4\" class=\"mainintro\">\n" +
                "                                <div>\n" +
                "                                  <p>\n" +
                "                                    童锦治女士,1963年11月出生,中国国籍,无境外永久居留权,博士研究生学历,博士生导师。曾任广东梅雁吉祥水电股份有限公司独立董事、福建省燕京惠泉啤酒股份有限公司独立董事、漳州片仔黄药业股份有限公司独立董事、九牧王股份有限公司独立董事、安井食品股份有限公司独立董事、厦门信达股份有限公司独立董事;现任厦门大学经济学院教授、厦门农村商业银行股份有限公司独立董事、万辰生物独立董事。\n" +
                "                                  </p>\n" +
                "                                  <p style=\"text-align:right\">\n" +
                "                                    此简介更新于2021-03-26\n" +
                "                                  </p>\n" +
                "                                </div>\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                          </tbody>\n" +
                "                        </table>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                    <td class=\"tl\">\n" +
                "                      独立董事\n" +
                "                    </td>\n" +
                "                    <td>\n" +
                "                      <div>\n" +
                "                        <span>\n" +
                "                          --\n" +
                "                        </span>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                    <td>\n" +
                "                      <div>\n" +
                "                        <span>\n" +
                "                          --\n" +
                "                        </span>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                    <th class=\"tc\">\n" +
                "                      8\n" +
                "                    </th>\n" +
                "                    <td class=\"tc name\">\n" +
                "                      <a person_id=\"T135746200\" onclick=\"TA.log({'id':'f10_gszl_ggjs','nj':1})\" class=\"turnto\" href=\"javascript:void(0)\">\n" +
                "                        肖珉\n" +
                "                      </a>\n" +
                "                      <div class=\"person_table hidden\">\n" +
                "                        <a class=\"close\" href=\"javascript:void(0);\">\n" +
                "                          ×\n" +
                "                        </a>\n" +
                "                        <table class=\"m_table ggintro\">\n" +
                "                          <thead>\n" +
                "                            <tr>\n" +
                "                              <td rowspan=\"2\" class=\"title\">\n" +
                "                                <h3>\n" +
                "                                  \n" +
                "                                                    肖珉                                                \n" +
                "                                </h3>\n" +
                "                                <input type=\"hidden\" value=\"[]\"/>\n" +
                "                              </td>\n" +
                "                              <td class=\"jobs\" style=\"width: 150px;\">\n" +
                "                                独立董事\n" +
                "                              </td>\n" +
                "                              <td class=\"date\" style=\"width: 170px;\">\n" +
                "                                <span>\n" +
                "                                  公告日期：\n" +
                "                                </span>\n" +
                "                                2021-03-26                                            \n" +
                "                              </td>\n" +
                "                              <td class=\"date\">\n" +
                "                                <span>\n" +
                "                                  本届任期：\n" +
                "                                </span>\n" +
                "                                2020-08-01                                                至 2023-07-31\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                              <td class=\"intro\">\n" +
                "                                女50岁博士\n" +
                "                              </td>\n" +
                "                              <td class=\"salary\">\n" +
                "                                <span>\n" +
                "                                  薪酬：\n" +
                "                                </span>\n" +
                "                                3.000万                                            \n" +
                "                              </td>\n" +
                "                              <td>\n" +
                "                                <div class=\"tipbox_wrap\" style=\"z-index: 99\">\n" +
                "                                  <span>\n" +
                "                                    直接持股数：\n" +
                "                                  </span>\n" +
                "                                  <span class=\"directnum hold\">\n" +
                "                                    -\n" +
                "                                  </span>\n" +
                "                                </div>\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                          </thead>\n" +
                "                          <tbody>\n" +
                "                            <tr>\n" +
                "                              <td colspan=\"4\" class=\"mainintro\">\n" +
                "                                <div>\n" +
                "                                  <p>\n" +
                "                                    肖珉女士,1971年生,中国国籍,无境外居留权,厦门大学企业管理专业财务管理方向博士;美国百森商学院MBA;现任厦门大学管理学院财务学系教授、博士生导师。教学及研究领域主要是公司财务、公司治理与财务会计。曾任厦门法拉电子股份有限公司、福建燕京惠泉啤酒股份有限公司独立董事。2018年5月至今,肖珉女士担任厦门瑞尔特卫浴科技股份有限公司独立董事。目前同时兼任易米基金管理有限公司、福建万辰生物科技股份有限公司、漳州雅宝电子股份有限公司、福信富通科技股份有限公司独立董事。\n" +
                "                                  </p>\n" +
                "                                  <p style=\"text-align:right\">\n" +
                "                                    此简介更新于2021-03-26\n" +
                "                                  </p>\n" +
                "                                </div>\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                          </tbody>\n" +
                "                        </table>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                    <td class=\"tl\">\n" +
                "                      独立董事\n" +
                "                    </td>\n" +
                "                    <td>\n" +
                "                      <div>\n" +
                "                        <span>\n" +
                "                          --\n" +
                "                        </span>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                    <td>\n" +
                "                      <div>\n" +
                "                        <span>\n" +
                "                          --\n" +
                "                        </span>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                  <tr>\n" +
                "                    <th class=\"tc\">\n" +
                "                      9\n" +
                "                    </th>\n" +
                "                    <td class=\"tc name\">\n" +
                "                      <a person_id=\"T151525800\" onclick=\"TA.log({'id':'f10_gszl_ggjs','nj':1})\" class=\"turnto\" href=\"javascript:void(0)\">\n" +
                "                        蔡清良\n" +
                "                      </a>\n" +
                "                      <div class=\"person_table hidden\">\n" +
                "                        <a class=\"close\" href=\"javascript:void(0);\">\n" +
                "                          ×\n" +
                "                        </a>\n" +
                "                        <table class=\"m_table ggintro\">\n" +
                "                          <thead>\n" +
                "                            <tr>\n" +
                "                              <td rowspan=\"2\" class=\"title\">\n" +
                "                                <h3>\n" +
                "                                  \n" +
                "                                                    蔡清良                                                \n" +
                "                                </h3>\n" +
                "                                <input type=\"hidden\" value=\"[]\"/>\n" +
                "                              </td>\n" +
                "                              <td class=\"jobs\" style=\"width: 150px;\">\n" +
                "                                独立董事\n" +
                "                              </td>\n" +
                "                              <td class=\"date\" style=\"width: 170px;\">\n" +
                "                                <span>\n" +
                "                                  公告日期：\n" +
                "                                </span>\n" +
                "                                2021-03-26                                            \n" +
                "                              </td>\n" +
                "                              <td class=\"date\">\n" +
                "                                <span>\n" +
                "                                  本届任期：\n" +
                "                                </span>\n" +
                "                                2019-10-15                                                至 2023-07-31\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                              <td class=\"intro\">\n" +
                "                                男55岁本科\n" +
                "                              </td>\n" +
                "                              <td class=\"salary\">\n" +
                "                                <span>\n" +
                "                                  薪酬：\n" +
                "                                </span>\n" +
                "                                2.400万                                            \n" +
                "                              </td>\n" +
                "                              <td>\n" +
                "                                <div class=\"tipbox_wrap\" style=\"z-index: 99\">\n" +
                "                                  <span>\n" +
                "                                    直接持股数：\n" +
                "                                  </span>\n" +
                "                                  <span class=\"directnum hold\">\n" +
                "                                    -\n" +
                "                                  </span>\n" +
                "                                </div>\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                          </thead>\n" +
                "                          <tbody>\n" +
                "                            <tr>\n" +
                "                              <td colspan=\"4\" class=\"mainintro\">\n" +
                "                                <div>\n" +
                "                                  <p>\n" +
                "                                    蔡清良先生,1966年6月出生,中国国籍,无境外永久居留权,本科学历。曾就职于福建方圆人律师事务所;现任闽南师范大学副教授、福建南州律师事务所兼职律师、万辰生物独立董事。\n" +
                "                                  </p>\n" +
                "                                  <p style=\"text-align:right\">\n" +
                "                                    此简介更新于2021-03-26\n" +
                "                                  </p>\n" +
                "                                </div>\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                          </tbody>\n" +
                "                        </table>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                    <td class=\"tl\">\n" +
                "                      独立董事\n" +
                "                    </td>\n" +
                "                    <td>\n" +
                "                      <div>\n" +
                "                        <span>\n" +
                "                          --\n" +
                "                        </span>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                    <td>\n" +
                "                      <div>\n" +
                "                        <span>\n" +
                "                          --\n" +
                "                        </span>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                    <th/>\n" +
                "                    <td/>\n" +
                "                    <td/>\n" +
                "                    <td/>\n" +
                "                    <td/>\n" +
                "                  </tr>\n" +
                "                </tbody>\n" +
                "              </table>\n" +
                "            </div>\n");
        html.append(
            "            <div class=\"m_tab_content\" id=\"ml_002\" style=\"display:none\">\n" +
                "              <table class=\"m_table managelist m_hl\">\n" +
                "                <thead>\n" +
                "                  <tr>\n" +
                "                    <th width=\"5%\">\n" +
                "                      序号\n" +
                "                    </th>\n" +
                "                    <th width=\"8%\">\n" +
                "                      姓名\n" +
                "                    </th>\n" +
                "                    <th width=\"15%\">\n" +
                "                      职务\n" +
                "                    </th>\n" +
                "                    <th width=\"10%\">\n" +
                "                      直接持股数\n" +
                "                    </th>\n" +
                "                    <th width=\"12%\">\n" +
                "                      间接持股数\n" +
                "                    </th>\n" +
                "                    <th width=\"5%\">\n" +
                "                      序号\n" +
                "                    </th>\n" +
                "                    <th width=\"8%\">\n" +
                "                      姓名\n" +
                "                    </th>\n" +
                "                    <th width=\"15%\">\n" +
                "                      职务\n" +
                "                    </th>\n" +
                "                    <th width=\"10%\">\n" +
                "                      直接持股数\n" +
                "                    </th>\n" +
                "                    <th width=\"12%\">\n" +
                "                      间接持股数\n" +
                "                    </th>\n" +
                "                  </tr>\n" +
                "                </thead>\n" +
                "                <tbody>\n" +
                "                  <tr>\n" +
                "                    <th class=\"tc\">\n" +
                "                      1\n" +
                "                    </th>\n" +
                "                    <td class=\"tc name\">\n" +
                "                      <a person_id=\"T737695000\" onclick=\"TA.log({'id':'f10_gszl_ggjs','nj':1})\" class=\"turnto\" href=\"javascript:void(0)\">\n" +
                "                        陈子文\n" +
                "                      </a>\n" +
                "                      <div class=\"person_table hidden\">\n" +
                "                        <a class=\"close\" href=\"javascript:void(0);\">\n" +
                "                          ×\n" +
                "                        </a>\n" +
                "                        <table class=\"m_table ggintro\">\n" +
                "                          <thead>\n" +
                "                            <tr>\n" +
                "                              <td rowspan=\"2\" class=\"title\">\n" +
                "                                <h3>\n" +
                "                                  \n" +
                "                                                    陈子文                                                \n" +
                "                                </h3>\n" +
                "                                <input type=\"hidden\" value=\"[]\"/>\n" +
                "                              </td>\n" +
                "                              <td class=\"jobs\" style=\"width: 150px;\">\n" +
                "                                监事会主席\n" +
                "                              </td>\n" +
                "                              <td class=\"date\" style=\"width: 170px;\">\n" +
                "                                <span>\n" +
                "                                  公告日期：\n" +
                "                                </span>\n" +
                "                                2021-03-26                                            \n" +
                "                              </td>\n" +
                "                              <td class=\"date\">\n" +
                "                                <span>\n" +
                "                                  本届任期：\n" +
                "                                </span>\n" +
                "                                2019-08-25                                                至 2023-07-31\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                              <td class=\"intro\">\n" +
                "                                男53岁中专\n" +
                "                              </td>\n" +
                "                              <td class=\"salary\">\n" +
                "                                <span>\n" +
                "                                  薪酬：\n" +
                "                                </span>\n" +
                "                                17.73万                                            \n" +
                "                              </td>\n" +
                "                              <td>\n" +
                "                                <div class=\"tipbox_wrap\" style=\"z-index: 99\">\n" +
                "                                  <span>\n" +
                "                                    直接持股数：\n" +
                "                                  </span>\n" +
                "                                  <span class=\"directnum hold\">\n" +
                "                                    -\n" +
                "                                  </span>\n" +
                "                                </div>\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                          </thead>\n" +
                "                          <tbody>\n" +
                "                            <tr>\n" +
                "                              <td colspan=\"4\" class=\"mainintro\">\n" +
                "                                <div>\n" +
                "                                  <p>\n" +
                "                                    陈子文先生,1968年7月出生,中国国籍,无境外永久居留权,中专学历。曾任漳浦酒厂技修班长,福建东方食品保障部经理。现任万辰生物设备保障部总监、监事会主席。\n" +
                "                                  </p>\n" +
                "                                  <p style=\"text-align:right\">\n" +
                "                                    此简介更新于2021-03-26\n" +
                "                                  </p>\n" +
                "                                </div>\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                          </tbody>\n" +
                "                        </table>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                    <td class=\"tl\">\n" +
                "                      监事会主席\n" +
                "                    </td>\n" +
                "                    <td>\n" +
                "                      <div>\n" +
                "                        <span>\n" +
                "                          --\n" +
                "                        </span>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                    <td>\n" +
                "                      <div>\n" +
                "                        <span>\n" +
                "                          29.93万(估)\n" +
                "                        </span>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                    <th class=\"tc\">\n" +
                "                      2\n" +
                "                    </th>\n" +
                "                    <td class=\"tc name\">\n" +
                "                      <a person_id=\"T142776400\" onclick=\"TA.log({'id':'f10_gszl_ggjs','nj':1})\" class=\"turnto\" href=\"javascript:void(0)\">\n" +
                "                        李容华\n" +
                "                      </a>\n" +
                "                      <div class=\"person_table hidden\">\n" +
                "                        <a class=\"close\" href=\"javascript:void(0);\">\n" +
                "                          ×\n" +
                "                        </a>\n" +
                "                        <table class=\"m_table ggintro\">\n" +
                "                          <thead>\n" +
                "                            <tr>\n" +
                "                              <td rowspan=\"2\" class=\"title\">\n" +
                "                                <h3>\n" +
                "                                  \n" +
                "                                                    李容华                                                \n" +
                "                                </h3>\n" +
                "                                <input type=\"hidden\" value=\"[]\"/>\n" +
                "                              </td>\n" +
                "                              <td class=\"jobs\" style=\"width: 150px;\">\n" +
                "                                监事\n" +
                "                              </td>\n" +
                "                              <td class=\"date\" style=\"width: 170px;\">\n" +
                "                                <span>\n" +
                "                                  公告日期：\n" +
                "                                </span>\n" +
                "                                2021-03-26                                            \n" +
                "                              </td>\n" +
                "                              <td class=\"date\">\n" +
                "                                <span>\n" +
                "                                  本届任期：\n" +
                "                                </span>\n" +
                "                                2019-03-06                                                至 2023-07-31\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                              <td class=\"intro\">\n" +
                "                                女29岁大专\n" +
                "                              </td>\n" +
                "                              <td class=\"salary\">\n" +
                "                                <span>\n" +
                "                                  薪酬：\n" +
                "                                </span>\n" +
                "                                4.520万                                            \n" +
                "                              </td>\n" +
                "                              <td>\n" +
                "                                <div class=\"tipbox_wrap\" style=\"z-index: 99\">\n" +
                "                                  <span>\n" +
                "                                    直接持股数：\n" +
                "                                  </span>\n" +
                "                                  <span class=\"directnum hold\">\n" +
                "                                    -\n" +
                "                                  </span>\n" +
                "                                </div>\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                          </thead>\n" +
                "                          <tbody>\n" +
                "                            <tr>\n" +
                "                              <td colspan=\"4\" class=\"mainintro\">\n" +
                "                                <div>\n" +
                "                                  <p>\n" +
                "                                    李容华女士,1992年11月出生,中国国籍,无境外永久居留权,专科学历。2014年至今在公司财务部任职,现任万辰生物监事。\n" +
                "                                  </p>\n" +
                "                                  <p style=\"text-align:right\">\n" +
                "                                    此简介更新于2021-03-26\n" +
                "                                  </p>\n" +
                "                                </div>\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                          </tbody>\n" +
                "                        </table>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                    <td class=\"tl\">\n" +
                "                      监事\n" +
                "                    </td>\n" +
                "                    <td>\n" +
                "                      <div>\n" +
                "                        <span>\n" +
                "                          --\n" +
                "                        </span>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                    <td>\n" +
                "                      <div>\n" +
                "                        <span>\n" +
                "                          --\n" +
                "                        </span>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                  <tr>\n" +
                "                    <th class=\"tc\">\n" +
                "                      3\n" +
                "                    </th>\n" +
                "                    <td class=\"tc name\">\n" +
                "                      <a person_id=\"T121643000\" onclick=\"TA.log({'id':'f10_gszl_ggjs','nj':1})\" class=\"turnto\" href=\"javascript:void(0)\">\n" +
                "                        洪强\n" +
                "                      </a>\n" +
                "                      <div class=\"person_table hidden\">\n" +
                "                        <a class=\"close\" href=\"javascript:void(0);\">\n" +
                "                          ×\n" +
                "                        </a>\n" +
                "                        <table class=\"m_table ggintro\">\n" +
                "                          <thead>\n" +
                "                            <tr>\n" +
                "                              <td rowspan=\"2\" class=\"title\">\n" +
                "                                <h3>\n" +
                "                                  \n" +
                "                                                    洪强                                                \n" +
                "                                </h3>\n" +
                "                                <input type=\"hidden\" value=\"{&quot;name&quot;:&quot;\\u6d2a\\u5f3a&quot;,&quot;id&quot;:1,&quot;pid&quot;:0,&quot;children&quot;:[{&quot;name&quot;:&quot;\\u5927\\u6a61\\u6728(\\u5357\\u4eac)\\u73af\\u5883\\u6280\\u672f\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;51.00%&quot;,&quot;id&quot;:2,&quot;pid&quot;:1,&quot;children&quot;:[]},{&quot;name&quot;:&quot;\\u5927\\u6a61\\u6728(\\u5317\\u4eac)\\u5b9e\\u9a8c\\u79d1\\u6280\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;49.00%&quot;,&quot;id&quot;:3,&quot;pid&quot;:1,&quot;children&quot;:[]}]}\"/>\n" +
                "                              </td>\n" +
                "                              <td class=\"jobs\" style=\"width: 150px;\">\n" +
                "                                职工监事\n" +
                "                              </td>\n" +
                "                              <td class=\"date\" style=\"width: 170px;\">\n" +
                "                                <span>\n" +
                "                                  公告日期：\n" +
                "                                </span>\n" +
                "                                2021-03-26                                            \n" +
                "                              </td>\n" +
                "                              <td class=\"date\">\n" +
                "                                <span>\n" +
                "                                  本届任期：\n" +
                "                                </span>\n" +
                "                                2017-05-17                                                至 2023-07-31\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                              <td class=\"intro\">\n" +
                "                                男31岁本科\n" +
                "                              </td>\n" +
                "                              <td class=\"salary\">\n" +
                "                                <span>\n" +
                "                                  薪酬：\n" +
                "                                </span>\n" +
                "                                12.32万                                            \n" +
                "                              </td>\n" +
                "                              <td>\n" +
                "                                <div class=\"tipbox_wrap\" style=\"z-index: 99\">\n" +
                "                                  <span>\n" +
                "                                    直接持股数：\n" +
                "                                  </span>\n" +
                "                                  <span class=\"directnum hold\">\n" +
                "                                    -\n" +
                "                                  </span>\n" +
                "                                </div>\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                          </thead>\n" +
                "                          <tbody>\n" +
                "                            <tr>\n" +
                "                              <td colspan=\"4\" class=\"mainintro\">\n" +
                "                                <div>\n" +
                "                                  <p>\n" +
                "                                    洪强先生,1990年6月出生,中国国籍,无境外永久居留权,本科学历。2013年至今任福建万辰生物科技股份有限公司福建基地一期技术部副经理。现任万辰生物职工监事。\n" +
                "                                  </p>\n" +
                "                                  <p style=\"text-align:right\">\n" +
                "                                    此简介更新于2021-03-26\n" +
                "                                  </p>\n" +
                "                                </div>\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                          </tbody>\n" +
                "                        </table>\n" +
                "                        <div class=\"stock-right-container hidden\" data-id=\"9\">\n" +
                "                          <img class=\"full-screen right-top\" src=\"//s.thsi.cn/js/basic/stock/img/full-black.png\"/>\n" +
                "                          <span class=\"close right-top hidden\">\n" +
                "                            ×\n" +
                "                          </span>\n" +
                "                          <div class=\"stock-right\" id=\"stock-right-9\">\n" +
                "                          </div>\n" +
                "                          <div class=\"tips hidden\">\n" +
                "                            该信息基于\n" +
                "                            <a class=\"tyc_link\" href=\"https://www.tianyancha.com/\" target=\"_blank\">\n" +
                "                              <strong>\n" +
                "                                天眼查\n" +
                "                              </strong>\n" +
                "                            </a>\n" +
                "                            数据分析得出，仅供参考，更多信息请到天眼查官网浏览\n" +
                "                          </div>\n" +
                "                        </div>\n" +
                "                        <div class=\"stock-right-btn see-stock-right tc\">\n" +
                "                          查看股权▼\n" +
                "                        </div>\n" +
                "                        <div class=\"stock-right-btn close-stock-right hidden tc\">\n" +
                "                          收起股权▲\n" +
                "                        </div>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                    <td class=\"tl\">\n" +
                "                      职工监事\n" +
                "                    </td>\n" +
                "                    <td>\n" +
                "                      <div>\n" +
                "                        <span>\n" +
                "                          --\n" +
                "                        </span>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                    <td>\n" +
                "                      <div>\n" +
                "                        <span>\n" +
                "                          --\n" +
                "                        </span>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                    <th/>\n" +
                "                    <td/>\n" +
                "                    <td/>\n" +
                "                    <td/>\n" +
                "                    <td/>\n" +
                "                  </tr>\n" +
                "                </tbody>\n" +
                "              </table>\n" +
                "            </div>\n");
        html.append(
            "            <div class=\"m_tab_content\" id=\"ml_003\" style=\"display:none\">\n" +
                "              <table class=\"m_table managelist m_hl\">\n" +
                "                <thead>\n" +
                "                  <tr>\n" +
                "                    <th width=\"5%\">\n" +
                "                      序号\n" +
                "                    </th>\n" +
                "                    <th width=\"8%\">\n" +
                "                      姓名\n" +
                "                    </th>\n" +
                "                    <th width=\"15%\">\n" +
                "                      职务\n" +
                "                    </th>\n" +
                "                    <th width=\"10%\">\n" +
                "                      直接持股数\n" +
                "                    </th>\n" +
                "                    <th width=\"12%\">\n" +
                "                      间接持股数\n" +
                "                    </th>\n" +
                "                    <th width=\"5%\">\n" +
                "                      序号\n" +
                "                    </th>\n" +
                "                    <th width=\"8%\">\n" +
                "                      姓名\n" +
                "                    </th>\n" +
                "                    <th width=\"15%\">\n" +
                "                      职务\n" +
                "                    </th>\n" +
                "                    <th width=\"10%\">\n" +
                "                      直接持股数\n" +
                "                    </th>\n" +
                "                    <th width=\"12%\">\n" +
                "                      间接持股数\n" +
                "                    </th>\n" +
                "                  </tr>\n" +
                "                </thead>\n" +
                "                <tbody>\n" +
                "                  <tr>\n" +
                "                    <th class=\"tc\">\n" +
                "                      1\n" +
                "                    </th>\n" +
                "                    <td class=\"tc name\">\n" +
                "                      <a person_id=\"T737678000\" onclick=\"TA.log({'id':'f10_gszl_ggjs','nj':1})\" class=\"turnto\" href=\"javascript:void(0)\">\n" +
                "                        王丽卿\n" +
                "                      </a>\n" +
                "                      <div class=\"person_table hidden\">\n" +
                "                        <a class=\"close\" href=\"javascript:void(0);\">\n" +
                "                          ×\n" +
                "                        </a>\n" +
                "                        <table class=\"m_table ggintro\">\n" +
                "                          <thead>\n" +
                "                            <tr>\n" +
                "                              <td rowspan=\"2\" class=\"title\">\n" +
                "                                <h3>\n" +
                "                                  \n" +
                "                                                    王丽卿                                                \n" +
                "                                </h3>\n" +
                "                                <input type=\"hidden\" value=\"{&quot;name&quot;:&quot;\\u738b\\u4e3d\\u537f&quot;,&quot;id&quot;:1,&quot;pid&quot;:0,&quot;children&quot;:[{&quot;name&quot;:&quot;\\u6f33\\u5dde\\u91d1\\u4e07\\u8fb0\\u6295\\u8d44\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;37.67%&quot;,&quot;id&quot;:2,&quot;pid&quot;:1,&quot;children&quot;:[{&quot;name&quot;:&quot;\\u798f\\u5efa\\u4e07\\u8fb0\\u751f\\u7269\\u79d1\\u6280\\u80a1\\u4efd\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;30.00%&quot;,&quot;id&quot;:3,&quot;pid&quot;:2,&quot;children&quot;:[{&quot;name&quot;:&quot;\\u5357\\u4eac\\u91d1\\u4e07\\u8fb0\\u751f\\u7269\\u79d1\\u6280\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;100.00%&quot;,&quot;id&quot;:4,&quot;pid&quot;:3,&quot;children&quot;:[]}]}]},{&quot;name&quot;:&quot;\\u6f33\\u5dde\\u4e1c\\u65b9\\u73b0\\u4ee3\\u519c\\u4ea7\\u54c1\\u7269\\u6d41\\u80a1\\u4efd\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;28.57%&quot;,&quot;id&quot;:5,&quot;pid&quot;:1,&quot;children&quot;:[]},{&quot;name&quot;:&quot;\\u6f33\\u5dde\\u5e02\\u9f99\\u6587\\u91d1\\u9675\\u82b1\\u5349\\u7eff\\u5316\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;30.00%&quot;,&quot;id&quot;:6,&quot;pid&quot;:1,&quot;children&quot;:[]}]}\"/>\n" +
                "                              </td>\n" +
                "                              <td class=\"jobs\" style=\"width: 150px;\">\n" +
                "                                总经理\n" +
                "                              </td>\n" +
                "                              <td class=\"date\" style=\"width: 170px;\">\n" +
                "                                <span>\n" +
                "                                  公告日期：\n" +
                "                                </span>\n" +
                "                                2021-03-26                                            \n" +
                "                              </td>\n" +
                "                              <td class=\"date\">\n" +
                "                                <span>\n" +
                "                                  本届任期：\n" +
                "                                </span>\n" +
                "                                2014-06-10                                                至 2023-07-31\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                              <td class=\"intro\">\n" +
                "                                女56岁大专\n" +
                "                              </td>\n" +
                "                              <td class=\"salary\">\n" +
                "                                <span>\n" +
                "                                  薪酬：\n" +
                "                                </span>\n" +
                "                                27.42万                                            \n" +
                "                              </td>\n" +
                "                              <td>\n" +
                "                                <div class=\"tipbox_wrap\" style=\"z-index: 99\">\n" +
                "                                  <span>\n" +
                "                                    直接持股数：\n" +
                "                                  </span>\n" +
                "                                  <span class=\"directnum hold\">\n" +
                "                                    -\n" +
                "                                  </span>\n" +
                "                                </div>\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                          </thead>\n" +
                "                          <tbody>\n" +
                "                            <tr>\n" +
                "                              <td colspan=\"4\" class=\"mainintro\">\n" +
                "                                <div>\n" +
                "                                  <p>\n" +
                "                                    王丽卿女士,1965年5月出生,中国国籍,无境外永久居留权,专科学历。曾任福建海丽天食品有限公司执行董事、总经理,漳州含羞草贸易执行董事、监事,漳州含羞草食品总经理,漳州金万辰董事、总经理、监事,福建东方食品副总经理,南京金万辰监事、上海含勋商务咨询有限公司监事,上海含羞草贸易监事。现任南京金万辰总经理、南京含羞草食品有限公司监事、福建海丽天食品有限公司监事、万辰生物董事、总经理。\n" +
                "                                  </p>\n" +
                "                                  <p style=\"text-align:right\">\n" +
                "                                    此简介更新于2021-03-26\n" +
                "                                  </p>\n" +
                "                                </div>\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                          </tbody>\n" +
                "                        </table>\n" +
                "                        <div class=\"stock-right-container hidden\" data-id=\"10\">\n" +
                "                          <img class=\"full-screen right-top\" src=\"//s.thsi.cn/js/basic/stock/img/full-black.png\"/>\n" +
                "                          <span class=\"close right-top hidden\">\n" +
                "                            ×\n" +
                "                          </span>\n" +
                "                          <div class=\"stock-right\" id=\"stock-right-10\">\n" +
                "                          </div>\n" +
                "                          <div class=\"tips hidden\">\n" +
                "                            该信息基于\n" +
                "                            <a class=\"tyc_link\" href=\"https://www.tianyancha.com/\" target=\"_blank\">\n" +
                "                              <strong>\n" +
                "                                天眼查\n" +
                "                              </strong>\n" +
                "                            </a>\n" +
                "                            数据分析得出，仅供参考，更多信息请到天眼查官网浏览\n" +
                "                          </div>\n" +
                "                        </div>\n" +
                "                        <div class=\"stock-right-btn see-stock-right tc\">\n" +
                "                          查看股权▼\n" +
                "                        </div>\n" +
                "                        <div class=\"stock-right-btn close-stock-right hidden tc\">\n" +
                "                          收起股权▲\n" +
                "                        </div>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                    <td class=\"tl\">\n" +
                "                      总经理\n" +
                "                    </td>\n" +
                "                    <td>\n" +
                "                      <div>\n" +
                "                        <span>\n" +
                "                          --\n" +
                "                        </span>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                    <td>\n" +
                "                      <div>\n" +
                "                        <span>\n" +
                "                          1169万(估)\n" +
                "                        </span>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                    <th class=\"tc\">\n" +
                "                      2\n" +
                "                    </th>\n" +
                "                    <td class=\"tc name\">\n" +
                "                      <a person_id=\"T737692000\" onclick=\"TA.log({'id':'f10_gszl_ggjs','nj':1})\" class=\"turnto\" href=\"javascript:void(0)\">\n" +
                "                        蔡冬娜\n" +
                "                      </a>\n" +
                "                      <div class=\"person_table hidden\">\n" +
                "                        <a class=\"close\" href=\"javascript:void(0);\">\n" +
                "                          ×\n" +
                "                        </a>\n" +
                "                        <table class=\"m_table ggintro\">\n" +
                "                          <thead>\n" +
                "                            <tr>\n" +
                "                              <td rowspan=\"2\" class=\"title\">\n" +
                "                                <h3>\n" +
                "                                  \n" +
                "                                                    蔡冬娜                                                \n" +
                "                                </h3>\n" +
                "                                <input type=\"hidden\" value=\"{&quot;name&quot;:&quot;\\u8521\\u51ac\\u5a1c&quot;,&quot;id&quot;:1,&quot;pid&quot;:0,&quot;children&quot;:[{&quot;name&quot;:&quot;\\u6f33\\u5dde\\u91d1\\u4e07\\u8fb0\\u6295\\u8d44\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;2.00%&quot;,&quot;id&quot;:2,&quot;pid&quot;:1,&quot;children&quot;:[{&quot;name&quot;:&quot;\\u798f\\u5efa\\u4e07\\u8fb0\\u751f\\u7269\\u79d1\\u6280\\u80a1\\u4efd\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;26.00%&quot;,&quot;id&quot;:3,&quot;pid&quot;:2,&quot;children&quot;:[]}]}]}\"/>\n" +
                "                              </td>\n" +
                "                              <td class=\"jobs\" style=\"width: 150px;\">\n" +
                "                                董事会秘书,副总经理,财务负责人\n" +
                "                              </td>\n" +
                "                              <td class=\"date\" style=\"width: 170px;\">\n" +
                "                                <span>\n" +
                "                                  公告日期：\n" +
                "                                </span>\n" +
                "                                2021-08-30                                            \n" +
                "                              </td>\n" +
                "                              <td class=\"date\">\n" +
                "                                <span>\n" +
                "                                  本届任期：\n" +
                "                                </span>\n" +
                "                                2019-03-26                                                至 2023-07-31\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                              <td class=\"intro\">\n" +
                "                                女50岁大专\n" +
                "                              </td>\n" +
                "                              <td class=\"salary\">\n" +
                "                                <span>\n" +
                "                                  薪酬：\n" +
                "                                </span>\n" +
                "                                19.25万                                            \n" +
                "                              </td>\n" +
                "                              <td>\n" +
                "                                <div class=\"tipbox_wrap\" style=\"z-index: 99\">\n" +
                "                                  <span>\n" +
                "                                    直接持股数：\n" +
                "                                  </span>\n" +
                "                                  <span class=\"directnum hold\">\n" +
                "                                    -\n" +
                "                                  </span>\n" +
                "                                </div>\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                          </thead>\n" +
                "                          <tbody>\n" +
                "                            <tr>\n" +
                "                              <td colspan=\"4\" class=\"mainintro\">\n" +
                "                                <div>\n" +
                "                                  <p>\n" +
                "                                    蔡冬娜女士,女,1971年出生,汉族,中国国籍,无境外永久居留权,专科学历。曾任漳州信德士集团有限公司财务经理,福建东方食品集团有限公司财务经理、财务总监。现任万辰生物财务总监、董事会秘书。\n" +
                "                                  </p>\n" +
                "                                  <p style=\"text-align:right\">\n" +
                "                                    此简介更新于2021-08-30\n" +
                "                                  </p>\n" +
                "                                </div>\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                          </tbody>\n" +
                "                        </table>\n" +
                "                        <div class=\"stock-right-container hidden\" data-id=\"11\">\n" +
                "                          <img class=\"full-screen right-top\" src=\"//s.thsi.cn/js/basic/stock/img/full-black.png\"/>\n" +
                "                          <span class=\"close right-top hidden\">\n" +
                "                            ×\n" +
                "                          </span>\n" +
                "                          <div class=\"stock-right\" id=\"stock-right-11\">\n" +
                "                          </div>\n" +
                "                          <div class=\"tips hidden\">\n" +
                "                            该信息基于\n" +
                "                            <a class=\"tyc_link\" href=\"https://www.tianyancha.com/\" target=\"_blank\">\n" +
                "                              <strong>\n" +
                "                                天眼查\n" +
                "                              </strong>\n" +
                "                            </a>\n" +
                "                            数据分析得出，仅供参考，更多信息请到天眼查官网浏览\n" +
                "                          </div>\n" +
                "                        </div>\n" +
                "                        <div class=\"stock-right-btn see-stock-right tc\">\n" +
                "                          查看股权▼\n" +
                "                        </div>\n" +
                "                        <div class=\"stock-right-btn close-stock-right hidden tc\">\n" +
                "                          收起股权▲\n" +
                "                        </div>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                    <td class=\"tl\">\n" +
                "                      董事会秘书,副总经理,财务负责人\n" +
                "                    </td>\n" +
                "                    <td>\n" +
                "                      <div>\n" +
                "                        <span>\n" +
                "                          --\n" +
                "                        </span>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                    <td>\n" +
                "                      <div>\n" +
                "                        <span>\n" +
                "                          59.87万(估)\n" +
                "                        </span>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                  <tr>\n" +
                "                    <th class=\"tc\">\n" +
                "                      3\n" +
                "                    </th>\n" +
                "                    <td class=\"tc name\">\n" +
                "                      <a person_id=\"T737680000\" onclick=\"TA.log({'id':'f10_gszl_ggjs','nj':1})\" class=\"turnto\" href=\"javascript:void(0)\">\n" +
                "                        李博\n" +
                "                      </a>\n" +
                "                      <div class=\"person_table hidden\">\n" +
                "                        <a class=\"close\" href=\"javascript:void(0);\">\n" +
                "                          ×\n" +
                "                        </a>\n" +
                "                        <table class=\"m_table ggintro\">\n" +
                "                          <thead>\n" +
                "                            <tr>\n" +
                "                              <td rowspan=\"2\" class=\"title\">\n" +
                "                                <h3>\n" +
                "                                  \n" +
                "                                                    李博                                                \n" +
                "                                </h3>\n" +
                "                                <input type=\"hidden\" value=\"[]\"/>\n" +
                "                              </td>\n" +
                "                              <td class=\"jobs\" style=\"width: 150px;\">\n" +
                "                                副总经理\n" +
                "                              </td>\n" +
                "                              <td class=\"date\" style=\"width: 170px;\">\n" +
                "                                <span>\n" +
                "                                  公告日期：\n" +
                "                                </span>\n" +
                "                                2021-03-26                                            \n" +
                "                              </td>\n" +
                "                              <td class=\"date\">\n" +
                "                                <span>\n" +
                "                                  本届任期：\n" +
                "                                </span>\n" +
                "                                2014-06-10                                                至 2023-07-31\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                              <td class=\"intro\">\n" +
                "                                男38岁硕士\n" +
                "                              </td>\n" +
                "                              <td class=\"salary\">\n" +
                "                                <span>\n" +
                "                                  薪酬：\n" +
                "                                </span>\n" +
                "                                25.81万                                            \n" +
                "                              </td>\n" +
                "                              <td>\n" +
                "                                <div class=\"tipbox_wrap\" style=\"z-index: 99\">\n" +
                "                                  <span>\n" +
                "                                    直接持股数：\n" +
                "                                  </span>\n" +
                "                                  <span class=\"directnum hold\">\n" +
                "                                    -\n" +
                "                                  </span>\n" +
                "                                </div>\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                          </thead>\n" +
                "                          <tbody>\n" +
                "                            <tr>\n" +
                "                              <td colspan=\"4\" class=\"mainintro\">\n" +
                "                                <div>\n" +
                "                                  <p>\n" +
                "                                    李博先生,1983年4月出生,中国国籍,无境外永久居留权,食用菌遗传学专业硕士研究生学历。曾任浙江瑞丰农业发展有限公司(现已更名为“浙江瑞丰农业开发股份有限公司”)生产部技术总监,江苏康盛农业发展有限公司副总经理。现任福建万辰生物科技股份有限公司董事、副总经理。曾参与编辑《中国食用菌菌种学》、《食用菌工厂化栽培实践》,作为主要起草人之一参与“金针菇工厂化栽培技术规范”标准制订,主持福建省种业创新与产业化工程项目“金针菇工厂化专用品种选育及液化菌种产业化示范推广”,参与漳州市科技局两化融合项目“漳州市食用菌行业智能测控系统建设示范”和科技部政策引导类计划项目“食用菌工厂化生产科技示范”等科研项目。\n" +
                "                                  </p>\n" +
                "                                  <p style=\"text-align:right\">\n" +
                "                                    此简介更新于2021-03-26\n" +
                "                                  </p>\n" +
                "                                </div>\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                          </tbody>\n" +
                "                        </table>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                    <td class=\"tl\">\n" +
                "                      副总经理\n");
        html.append(
            "                    </td>\n" +
                "                    <td>\n" +
                "                      <div>\n" +
                "                        <span>\n" +
                "                          --\n" +
                "                        </span>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                    <td>\n" +
                "                      <div>\n" +
                "                        <span>\n" +
                "                          119.7万(估)\n" +
                "                        </span>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                    <th class=\"tc\">\n" +
                "                      4\n" +
                "                    </th>\n" +
                "                    <td class=\"tc name\">\n" +
                "                      <a person_id=\"T737686000\" onclick=\"TA.log({'id':'f10_gszl_ggjs','nj':1})\" class=\"turnto\" href=\"javascript:void(0)\">\n" +
                "                        王泽宁\n" +
                "                      </a>\n" +
                "                      <div class=\"person_table hidden\">\n" +
                "                        <a class=\"close\" href=\"javascript:void(0);\">\n" +
                "                          ×\n" +
                "                        </a>\n" +
                "                        <table class=\"m_table ggintro\">\n" +
                "                          <thead>\n" +
                "                            <tr>\n" +
                "                              <td rowspan=\"2\" class=\"title\">\n" +
                "                                <h3>\n" +
                "                                  \n" +
                "                                                    王泽宁                                                \n" +
                "                                </h3>\n" +
                "                                <input type=\"hidden\" value=\"{&quot;name&quot;:&quot;\\u738b\\u6cfd\\u5b81&quot;,&quot;id&quot;:1,&quot;pid&quot;:0,&quot;children&quot;:[{&quot;name&quot;:&quot;\\u798f\\u5efa\\u542b\\u7f9e\\u8349\\u519c\\u4e1a\\u5f00\\u53d1\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;80.00%&quot;,&quot;id&quot;:2,&quot;pid&quot;:1,&quot;children&quot;:[{&quot;name&quot;:&quot;\\u6c5f\\u82cf\\u542b\\u7f9e\\u8349\\u519c\\u4e1a\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;100.00%&quot;,&quot;id&quot;:5,&quot;pid&quot;:2,&quot;children&quot;:[]}]},{&quot;name&quot;:&quot;\\u798f\\u5efa\\u4e07\\u8fb0\\u751f\\u7269\\u79d1\\u6280\\u80a1\\u4efd\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;10.00%&quot;,&quot;id&quot;:3,&quot;pid&quot;:1,&quot;children&quot;:[{&quot;name&quot;:&quot;\\u5357\\u4eac\\u91d1\\u4e07\\u8fb0\\u751f\\u7269\\u79d1\\u6280\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;100.00%&quot;,&quot;id&quot;:6,&quot;pid&quot;:3,&quot;children&quot;:[]}]},{&quot;name&quot;:&quot;\\u6f33\\u5dde\\u91d1\\u4e07\\u8fb0\\u6295\\u8d44\\u6709\\u9650\\u516c\\u53f8&quot;,&quot;info&quot;:&quot;53.33%&quot;,&quot;id&quot;:4,&quot;pid&quot;:1,&quot;children&quot;:[]}]}\"/>\n" +
                "                              </td>\n" +
                "                              <td class=\"jobs\" style=\"width: 150px;\">\n" +
                "                                副总经理\n" +
                "                              </td>\n" +
                "                              <td class=\"date\" style=\"width: 170px;\">\n" +
                "                                <span>\n" +
                "                                  公告日期：\n" +
                "                                </span>\n" +
                "                                2021-03-26                                            \n" +
                "                              </td>\n" +
                "                              <td class=\"date\">\n" +
                "                                <span>\n" +
                "                                  本届任期：\n" +
                "                                </span>\n" +
                "                                2017-01-25                                                至 2023-07-31\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                              <td class=\"intro\">\n" +
                "                                男28岁硕士\n" +
                "                              </td>\n" +
                "                              <td class=\"salary\">\n" +
                "                                <span>\n" +
                "                                  薪酬：\n" +
                "                                </span>\n" +
                "                                8.220万                                            \n" +
                "                              </td>\n" +
                "                              <td>\n" +
                "                                <div class=\"tipbox_wrap\" style=\"z-index: 99\">\n" +
                "                                  <span>\n" +
                "                                    直接持股数：\n" +
                "                                  </span>\n" +
                "                                  <span class=\"directnum hold\">\n" +
                "                                    780万\n" +
                "                                  </span>\n" +
                "                                </div>\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                          </thead>\n" +
                "                          <tbody>\n" +
                "                            <tr>\n" +
                "                              <td colspan=\"4\" class=\"mainintro\">\n" +
                "                                <div>\n" +
                "                                  <p>\n" +
                "                                    王泽宁先生,1993年9月出生,中国国籍,无境外永久居留权,硕士研究生学历。曾任江苏含羞草执行董事兼总经理。现任福建万辰生物科技股份有限公司董事、副总经理。\n" +
                "                                  </p>\n" +
                "                                  <p style=\"text-align:right\">\n" +
                "                                    此简介更新于2021-03-26\n" +
                "                                  </p>\n" +
                "                                </div>\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                          </tbody>\n" +
                "                        </table>\n" +
                "                        <div class=\"stock-right-container hidden\" data-id=\"12\">\n" +
                "                          <img class=\"full-screen right-top\" src=\"//s.thsi.cn/js/basic/stock/img/full-black.png\"/>\n" +
                "                          <span class=\"close right-top hidden\">\n" +
                "                            ×\n" +
                "                          </span>\n" +
                "                          <div class=\"stock-right\" id=\"stock-right-12\">\n" +
                "                          </div>\n" +
                "                          <div class=\"tips hidden\">\n" +
                "                            该信息基于\n" +
                "                            <a class=\"tyc_link\" href=\"https://www.tianyancha.com/\" target=\"_blank\">\n" +
                "                              <strong>\n" +
                "                                天眼查\n" +
                "                              </strong>\n" +
                "                            </a>\n" +
                "                            数据分析得出，仅供参考，更多信息请到天眼查官网浏览\n" +
                "                          </div>\n" +
                "                        </div>\n" +
                "                        <div class=\"stock-right-btn see-stock-right tc\">\n" +
                "                          查看股权▼\n" +
                "                        </div>\n" +
                "                        <div class=\"stock-right-btn close-stock-right hidden tc\">\n" +
                "                          收起股权▲\n" +
                "                        </div>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                    <td class=\"tl\">\n" +
                "                      副总经理\n" +
                "                    </td>\n" +
                "                    <td>\n" +
                "                      <div>\n" +
                "                        <span>\n" +
                "                          780万\n" +
                "                        </span>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                    <td>\n" +
                "                      <div>\n" +
                "                        <span>\n" +
                "                          4872万(估)\n" +
                "                        </span>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                  <tr>\n" +
                "                    <th class=\"tc\">\n" +
                "                      5\n" +
                "                    </th>\n" +
                "                    <td class=\"tc name\">\n" +
                "                      <a person_id=\"T737691000\" onclick=\"TA.log({'id':'f10_gszl_ggjs','nj':1})\" class=\"turnto\" href=\"javascript:void(0)\">\n" +
                "                        柯建平\n" +
                "                      </a>\n" +
                "                      <div class=\"person_table hidden\">\n" +
                "                        <a class=\"close\" href=\"javascript:void(0);\">\n" +
                "                          ×\n" +
                "                        </a>\n" +
                "                        <table class=\"m_table ggintro\">\n" +
                "                          <thead>\n" +
                "                            <tr>\n" +
                "                              <td rowspan=\"2\" class=\"title\">\n" +
                "                                <h3>\n" +
                "                                  \n" +
                "                                                    柯建平                                                \n" +
                "                                </h3>\n" +
                "                                <input type=\"hidden\" value=\"[]\"/>\n" +
                "                              </td>\n" +
                "                              <td class=\"jobs\" style=\"width: 150px;\">\n" +
                "                                副总经理\n" +
                "                              </td>\n" +
                "                              <td class=\"date\" style=\"width: 170px;\">\n" +
                "                                <span>\n" +
                "                                  公告日期：\n" +
                "                                </span>\n" +
                "                                2021-03-26                                            \n" +
                "                              </td>\n" +
                "                              <td class=\"date\">\n" +
                "                                <span>\n" +
                "                                  本届任期：\n" +
                "                                </span>\n" +
                "                                2014-06-10                                                至 2023-07-31\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                              <td class=\"intro\">\n" +
                "                                男58岁大专\n" +
                "                              </td>\n" +
                "                              <td class=\"salary\">\n" +
                "                                <span>\n" +
                "                                  薪酬：\n" +
                "                                </span>\n" +
                "                                5.290万                                            \n" +
                "                              </td>\n" +
                "                              <td>\n" +
                "                                <div class=\"tipbox_wrap\" style=\"z-index: 99\">\n" +
                "                                  <span>\n" +
                "                                    直接持股数：\n" +
                "                                  </span>\n" +
                "                                  <span class=\"directnum hold\">\n" +
                "                                    -\n" +
                "                                  </span>\n" +
                "                                </div>\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                          </thead>\n" +
                "                          <tbody>\n" +
                "                            <tr>\n" +
                "                              <td colspan=\"4\" class=\"mainintro\">\n" +
                "                                <div>\n" +
                "                                  <p>\n" +
                "                                    柯建平先生,1963年10月出生,中国国籍,无境外永久居留权,专科学历。曾任福建省漳浦糖厂供销科组长,漳州含羞草食品供应部经理。现任万辰生物副总经理。\n" +
                "                                  </p>\n" +
                "                                  <p style=\"text-align:right\">\n" +
                "                                    此简介更新于2021-03-26\n" +
                "                                  </p>\n" +
                "                                </div>\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                          </tbody>\n" +
                "                        </table>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                    <td class=\"tl\">\n" +
                "                      副总经理\n" +
                "                    </td>\n" +
                "                    <td>\n" +
                "                      <div>\n" +
                "                        <span>\n" +
                "                          --\n" +
                "                        </span>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                    <td>\n" +
                "                      <div>\n" +
                "                        <span>\n" +
                "                          --\n" +
                "                        </span>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                    <th class=\"tc\">\n" +
                "                      6\n" +
                "                    </th>\n" +
                "                    <td class=\"tc name\">\n" +
                "                      <a person_id=\"T183346900\" onclick=\"TA.log({'id':'f10_gszl_ggjs','nj':1})\" class=\"turnto\" href=\"javascript:void(0)\">\n" +
                "                        潘峰\n" +
                "                      </a>\n" +
                "                      <div class=\"person_table hidden\">\n" +
                "                        <a class=\"close\" href=\"javascript:void(0);\">\n" +
                "                          ×\n" +
                "                        </a>\n" +
                "                        <table class=\"m_table ggintro\">\n" +
                "                          <thead>\n" +
                "                            <tr>\n" +
                "                              <td rowspan=\"2\" class=\"title\">\n" +
                "                                <h3>\n" +
                "                                  \n" +
                "                                                    潘峰                                                \n" +
                "                                </h3>\n" +
                "                                <input type=\"hidden\" value=\"[]\"/>\n" +
                "                              </td>\n" +
                "                              <td class=\"jobs\" style=\"width: 150px;\">\n" +
                "                                副总经理\n" +
                "                              </td>\n" +
                "                              <td class=\"date\" style=\"width: 170px;\">\n" +
                "                                <span>\n" +
                "                                  公告日期：\n" +
                "                                </span>\n" +
                "                                2021-08-30                                            \n" +
                "                              </td>\n" +
                "                              <td class=\"date\">\n" +
                "                                <span>\n" +
                "                                  本届任期：\n" +
                "                                </span>\n" +
                "                                2021-08-26                                                至 2023-07-31\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                            <tr>\n" +
                "                              <td class=\"intro\">\n" +
                "                                男41岁本科\n" +
                "                              </td>\n" +
                "                              <td class=\"salary\">\n" +
                "                                <span>\n" +
                "                                  薪酬：\n" +
                "                                </span>\n" +
                "                                --                                            \n" +
                "                              </td>\n" +
                "                              <td>\n" +
                "                                <div class=\"tipbox_wrap\" style=\"z-index: 99\">\n" +
                "                                  <span>\n" +
                "                                    直接持股数：\n" +
                "                                  </span>\n" +
                "                                  <span class=\"directnum hold\">\n" +
                "                                    -\n" +
                "                                  </span>\n" +
                "                                </div>\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                          </thead>\n" +
                "                          <tbody>\n" +
                "                            <tr>\n" +
                "                              <td colspan=\"4\" class=\"mainintro\">\n" +
                "                                <div>\n" +
                "                                  <p>\n" +
                "                                    潘峰先生,男,1980年出生,汉族,中国国籍,无境外永久居留权,本科学历。曾任浙江秋平真菌研究所技术员,浙江绍兴园林有限公司技术员,上海雪榕生物科技股份有限公司总工艺调控师。现任福建万辰生物科技股份有限公司技术总监。\n" +
                "                                  </p>\n" +
                "                                  <p style=\"text-align:right\">\n" +
                "                                    此简介更新于2021-08-30\n" +
                "                                  </p>\n" +
                "                                </div>\n" +
                "                              </td>\n" +
                "                            </tr>\n" +
                "                          </tbody>\n" +
                "                        </table>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                    <td class=\"tl\">\n" +
                "                      副总经理\n" +
                "                    </td>\n" +
                "                    <td>\n" +
                "                      <div>\n" +
                "                        <span>\n" +
                "                          --\n" +
                "                        </span>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                    <td>\n" +
                "                      <div>\n" +
                "                        <span>\n" +
                "                          --\n" +
                "                        </span>\n" +
                "                      </div>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody>\n" +
                "              </table>\n" +
                "            </div>\n" +
                "            <p class=\"pt5\">\n");
        html.append(
            "              注：点击高管姓名查看高管简历介绍\n" +
                "            </p>\n" +
                "          </div>\n" +
                "          <div class=\"ft\">\n" +
                "          </div>\n" +
                "        </div>\n" +
                "        <!-- 发行相关 -->        <div class=\"m_box company_detail\" id=\"publish\" stat=\"company_publish\">\n" +
                "          <div class=\"hd\">\n" +
                "            <h2>\n" +
                "              发行相关\n" +
                "            </h2>\n" +
                "          </div>\n" +
                "          <div class=\"bd pr\">\n" +
                "            <table class=\"m_table\">\n" +
                "              <tbody>\n" +
                "                <tr>\n" +
                "                  <td>\n" +
                "                    <strong class=\"hltip fl\">\n" +
                "                      成立日期：\n" +
                "                    </strong>\n" +
                "                    <span>\n" +
                "                      2011-12-21\n" +
                "                    </span>\n" +
                "                  </td>\n" +
                "                  <td>\n" +
                "                    <strong class=\"hltip fl\">\n" +
                "                      发行数量：\n" +
                "                    </strong>\n" +
                "                    <span>\n" +
                "                      3837.50万股\n" +
                "                    </span>\n" +
                "                  </td>\n" +
                "                  <td>\n" +
                "                    <strong class=\"hltip fl\">\n" +
                "                      发行价格：\n" +
                "                    </strong>\n" +
                "                    <span>\n" +
                "                      7.19元\n" +
                "                    </span>\n" +
                "                  </td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                  <td>\n" +
                "                    <strong class=\"hltip fl\">\n" +
                "                      上市日期：\n" +
                "                    </strong>\n" +
                "                    <span>\n" +
                "                      2021-04-19\n" +
                "                    </span>\n" +
                "                  </td>\n" +
                "                  <td>\n" +
                "                    <strong class=\"hltip fl\">\n" +
                "                      发行市盈率：\n" +
                "                    </strong>\n" +
                "                    <span>\n" +
                "                      12.1500倍\n" +
                "                    </span>\n" +
                "                  </td>\n" +
                "                  <td>\n" +
                "                    <strong class=\"hltip fl\">\n" +
                "                      预计募资：\n" +
                "                    </strong>\n" +
                "                    <span>\n" +
                "                      6亿元\n" +
                "                    </span>\n" +
                "                  </td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                  <td>\n" +
                "                    <strong class=\"hltip fl\">\n" +
                "                      首日开盘价：\n" +
                "                    </strong>\n" +
                "                    <span>\n" +
                "                      28.88元\n" +
                "                    </span>\n" +
                "                  </td>\n" +
                "                  <td>\n" +
                "                    <strong class=\"hltip fl\">\n" +
                "                      发行\n" +
                "                      <span class=\"hltip  iwcclick\" taname=\"f10_stock_iwc_zql\" content=\"中签率= 股票发行股数/有效申购股数*100%\" data-url=\"http://www.iwencai.com/yike/detail/auid/6fdbde6ab15aba60?qs=client_f10_baike_4\">\n" +
                "                        中签率\n" +
                "                      </span>\n" +
                "                      ：\n" +
                "                    </strong>\n" +
                "                    <span>\n" +
                "                      0.02%\n" +
                "                    </span>\n" +
                "                  </td>\n" +
                "                  <td>\n" +
                "                    <strong class=\"hltip fl\">\n" +
                "                      实际募资：\n" +
                "                    </strong>\n" +
                "                    <span>\n" +
                "                      2.76亿元\n" +
                "                    </span>\n" +
                "                  </td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                  <td colspan=\"3\">\n" +
                "                    <div class=\"main_sell\">\n" +
                "                      <strong class=\"hltip iwcclick\" taname=\"f10_stock_iwc_zql\" content=\"主承销商，是指在股票发行中独家承销或牵头组织承销团经销的证券经营机构。\" data-url=\"http://www.iwencai.com/yike/detail/auid/2d7c8f7de1372d8e?qs=client_f10_baike_4\">\n" +
                "                        主承销商：\n" +
                "                      </strong>\n" +
                "                      <span>\n" +
                "                        民生证券股份有限公司\n" +
                "                      </span>\n" +
                "                    </div>\n" +
                "                    <div class=\"main_sell\">\n" +
                "                      <strong class=\"hltip\">\n" +
                "                        上市保荐人：\n" +
                "                      </strong>\n" +
                "                      <span>\n" +
                "                        民生证券股份有限公司\n" +
                "                      </span>\n" +
                "                    </div>\n" +
                "                  </td>\n" +
                "                </tr>\n" +
                "                <tr class=\"intro\">\n" +
                "                  <td colspan=\"3\">\n" +
                "                    <strong class=\"hltip\">\n" +
                "                      历史沿革：\n" +
                "                    </strong>\n" +
                "                    <p class=\"tip lh24\" style=\"text-indent: 0;\">\n" +
                "                      　　（一）有限公司设立情况\n" +
                "\n" +
                "                      <br/>\n" +
                "                      　　公司前身万辰有限成立于2011年12月21日，设立时名称为“福建含羞草生物科技有限公司”，注册资本为2,000万元，由福建含羞草农业开发有限公司以货币资金出资设立。\n" +
                "\n" +
                "                      <br/>\n" +
                "                      　　根据漳州承信会计师事务所有限责任公司出具的“漳承会验字[2011]第140号”《验资报告》，截至2011年12月20日，万辰有限已收到福建含羞草农业开发有限公司缴纳的注册资本2,000万元整，出资方式为货币资金。\n" +
                "\n" +
                "                      <br/>\n" +
                "                      　　2011年12月21日，万辰有限领取了由漳浦县工商行政管理局颁发的《企业法人营业执照》，注册号为350623100034968。\n" +
                "\n" +
                "                      <br/>\n" +
                "                      　　（二）股份公司...\n" +
                "                      <a href=\"javascript:void(0)\" class=\"more fr\">\n" +
                "                        查看全部▼\n" +
                "                      </a>\n" +
                "                    </p>\n" +
                "                    <p class=\"tip lh24 none\" style=\"text-indent: 0;\">\n" +
                "                      　　（一）有限公司设立情况\n" +
                "\n" +
                "                      <br/>\n" +
                "                      　　公司前身万辰有限成立于2011年12月21日，设立时名称为“福建含羞草生物科技有限公司”，注册资本为2,000万元，由福建含羞草农业开发有限公司以货币资金出资设立。\n" +
                "\n" +
                "                      <br/>\n" +
                "                      　　根据漳州承信会计师事务所有限责任公司出具的“漳承会验字[2011]第140号”《验资报告》，截至2011年12月20日，万辰有限已收到福建含羞草农业开发有限公司缴纳的注册资本2,000万元整，出资方式为货币资金。\n" +
                "\n" +
                "                      <br/>\n" +
                "                      　　2011年12月21日，万辰有限领取了由漳浦县工商行政管理局颁发的《企业法人营业执照》，注册号为350623100034968。\n" +
                "\n" +
                "                      <br/>\n" +
                "                      　　（二）股份公司设立情况\n" +
                "\n" +
                "                      <br/>\n" +
                "                      　　2014年5月26日，万辰有限召开股东会，同意万辰有限企业类型变更为股份有限公司，并以2014年3月31日为评估基准日。\n" +
                "\n" +
                "                      <br/>\n" +
                "                      　　致同会计师事务所（特殊普通合伙）对万辰有限财务报告进行了审计并出具了“致同审字[2014]第350ZA1614号”《审计报告》，确认截至2014年3月31日，万辰有限净资产为9,379.03万元。\n" +
                "\n" +
                "                      <br/>\n" +
                "                      　　厦门市大学资产评估有限公司对万辰有限经审计后资产及负债进行了评估并出具了“大学评估[2014]ZL0010号”《资产评估报告书》，确认万辰有限截至2014年3月31日的净资产评估值为12,095.39万元。\n" +
                "\n" +
                "                      <br/>\n" +
                "                      　　2014年5月26日，万辰有限全体股东福建农开发、漳州金万辰、王泽宁签署《设立福建万辰生物科技股份有限公司之发起人协议》，约定以万辰有限截至2014年3月31日经审计净资产9,379.03万元折合股份6,900万股，剩余净资产2,479.03万元计入资本公积。\n" +
                "\n" +
                "                      <br/>\n" +
                "                      　　致同会计师事务所（特殊普通合伙）对公司全体股东出资到位情况进行了审验并出具了“致同验字（2014）第350ZA0094号”《验资报告》，确认“截至2014年6月10日，公司以2014年3月31日经审计的净资产人民币93,790,333.44元，折股69,000,000.00元作为注册资本，其余作为资本公积”。\n" +
                "\n" +
                "                      <br/>\n" +
                "                      　　2014年6月27日，公司完成工商登记并领取了注册号为“350623100034968”的《营业执照》。\n" +
                "\n" +
                "                      <br/>\n" +
                "                      　　三、报告期内的股本和股东变化情况\n" +
                "\n" +
                "                      <br/>\n" +
                "                      　　报告期内公司股本未发生变化。报告期内公司股东变化较大，系因公司股票在新三板挂牌期间的交易导致。\n" +
                "\n" +
                "                      <br/>\n" +
                "                      　　截至2020年6月30日止，公司注册资本为11,512.50万元。\n" +
                "\n" +
                "                      <br/>\n" +
                "                      　　截至2020年12月31日止，公司注册资本为11,512.50万元。\n" +
                "\n" +
                "                      <br/>\n" +
                "                      　　截至2021年6月30日止，公司注册资本为15350万元。\n" +
                "                      <a href=\"javascript:void(0)\" class=\"less fr\">\n" +
                "                        收起▲\n" +
                "                      </a>\n" +
                "                    </p>\n" +
                "                  </td>\n" +
                "                </tr>\n" +
                "              </tbody>\n" +
                "            </table>\n" +
                "          </div>\n" +
                "          <div class=\"ft\">\n" +
                "          </div>\n" +
                "        </div>\n" +
                "        <div class=\"m_box gssj_scroll\" id=\"share\" stat=\"company_share\">\n" +
                "          <div class=\"hd\">\n" +
                "            <h2>\n" +
                "              参股控股公司\n" +
                "            </h2>\n" +
                "          </div>\n" +
                "          <div class=\"bd pr\">\n" +
                "            <table class=\"m_table m_hl ggintro business\" id=\"ckg_table\">\n" +
                "              <caption class=\"m_cap tip\">\n" +
                "                <div class=\"fr\">\n" +
                "                  最新公告日期：\n" +
                "                  <span class=\"hl\">\n" +
                "                    2021-08-30\n" +
                "                  </span>\n" +
                "                  <a class=\"pdf-icon client\" href=\"http://notice.10jqka.com.cn/api/pdf/bba5cffaa310f407.pdf\" target=\"_blank\" title=\"【查看公告】\">\n" +
                "                  </a>\n" +
                "                  <!-- <span class=\"pdf-icon\" title=\"【查看公告】公告为有参控股的最新的半年报或年报\"></span> -->                </div>\n" +
                "                <span class=\"f14\">\n" +
                "                  参股或控股公司：\n" +
                "                  <strong>\n" +
                "                    2\n" +
                "                  </strong>\n" +
                "                   家，\n" +
                "\t\t\t\t其中合并报表的有：\n" +
                "                  <strong>\n" +
                "                    1\n" +
                "                  </strong>\n" +
                "                   家。\n" +
                "                </span>\n" +
                "              </caption>\n" +
                "              <thead>\n" +
                "                <tr>\n" +
                "                  <th width=\"25px\" class=\"nosort\">\n" +
                "                    序号\n" +
                "                  </th>\n" +
                "                  <th width=\"170px\" class=\"nosort\">\n" +
                "                    关联公司名称\n" +
                "                  </th>\n" +
                "                  <th width=\"50px\" class=\"nosort\">\n" +
                "                    参控关系\n" +
                "                  </th>\n" +
                "                  <th width=\"50px\" class=\"head\">\n" +
                "                    参控比例\n" +
                "                    <s/>\n" +
                "                  </th>\n" +
                "                  <th width=\"105px\" class=\"desc\">\n" +
                "                    投资金额(元)\n" +
                "                    <s/>\n" +
                "                  </th>\n" +
                "                  <th width=\"60px\" class=\"head\">\n" +
                "                    被参控公司净\n" +
                "                    <br/>\n" +
                "                    利润(元)\n" +
                "                    <s/>\n" +
                "                  </th>\n" +
                "                  <th width=\"50px\" class=\"head\">\n" +
                "                    是否报表\n" +
                "                    <br/>\n" +
                "                    合并\n" +
                "                    <s/>\n" +
                "                  </th>\n" +
                "                  <th width=\"140px\" class=\"nosort\">\n" +
                "                    被参股公司主营业务\n" +
                "                  </th>\n" +
                "                </tr>\n" +
                "              </thead>\n" +
                "              <tbody>\n" +
                "                <tr class=\"objrow J_pageritem\">\n" +
                "                  <td style=\"color:#ccc;text-align:center\" class=\"\">\n" +
                "                    1\n" +
                "                  </td>\n" +
                "                  <td style=\"text-align:left;word-break:break-all\" class=\"\">\n" +
                "                    <p class=\"institionName\" onclick=\"sendTalog('f10_stock_company_click_ggsjdetail')\" orgid=\"T004911662\" href=\"javascript:void(0)\">\n" +
                "                      江苏和正生物科技有限公司\n" +
                "                    </p>\n" +
                "                    <div class=\"gssjBox hidden \">\n" +
                "                    </div>\n" +
                "                  </td>\n" +
                "                  <td style=\"text-align:left\" class=\"\">\n" +
                "                    联营企业\n" +
                "                  </td>\n" +
                "                  <td class=\"\">\n" +
                "                    9.00%\n" +
                "                  </td>\n" +
                "                  <td data-unit=\"-\" class=\"objselected\">\n" +
                "                    -\n" +
                "                  </td>\n" +
                "                  <td data-unit=\"-\" class=\"\">\n" +
                "                    -\n" +
                "                  </td>\n" +
                "                  <td style=\"text-align:center\" class=\"\">\n" +
                "                    -\n" +
                "                  </td>\n" +
                "                  <td style=\"text-align:left\" class=\"\">\n" +
                "                  </td>\n" +
                "                </tr>\n" +
                "                <tr class=\"objrow J_pageritem\">\n" +
                "                  <td style=\"color:#ccc;text-align:center\" class=\"\">\n" +
                "                    2\n" +
                "                  </td>\n" +
                "                  <td style=\"text-align:left;word-break:break-all\" class=\"\">\n" +
                "                    <p class=\"institionName\" onclick=\"sendTalog('f10_stock_company_click_ggsjdetail')\" orgid=\"T000249504\" href=\"javascript:void(0)\">\n" +
                "                      南京金万辰生物科技有限公司\n" +
                "                    </p>\n" +
                "                    <div class=\"gssjBox hidden \">\n" +
                "                    </div>\n" +
                "                  </td>\n" +
                "                  <td style=\"text-align:left\" class=\"\">\n" +
                "                    子公司\n" +
                "                  </td>\n" +
                "                  <td class=\"\">\n" +
                "                    100.00%\n" +
                "                  </td>\n" +
                "                  <td data-unit=\"-\" class=\"objselected\">\n" +
                "                    -\n" +
                "                  </td>\n" +
                "                  <td data-unit=\"-\" class=\"\">\n" +
                "                    -\n" +
                "                  </td>\n" +
                "                  <td style=\"text-align:center\" class=\"\">\n" +
                "                    是\n" +
                "                  </td>\n" +
                "                  <td style=\"text-align:left\" class=\"\">\n" +
                "                  </td>\n" +
                "                </tr>\n" +
                "              </tbody>\n" +
                "            </table>\n" +
                "            <div class=\"rp_tipbox zyyw_box none\" style=\"width:190px\">\n" +
                "              <div class=\"tipbox_bd p0_5\">\n" +
                "                <table class=\"m_table\" style=\"width:180px\">\n" +
                "                  <thead>\n" +
                "                    <tr>\n" +
                "                      <th class=\"tl\">\n" +
                "                        主营业务详情：\n" +
                "                      </th>\n" +
                "                    </tr>\n" +
                "                  </thead>\n" +
                "                  <tbody>\n" +
                "                    <tr>\n" +
                "                      <td class=\"tl\" id=\"busi\"/>\n" +
                "                    </tr>\n" +
                "                  </tbody>\n" +
                "                </table>\n" +
                "              </div>\n" +
                "              <s class=\"arrow\" style=\"right:25px\"/>\n" +
                "            </div>\n" +
                "            <div class=\"pager clearfix\">\n" +
                "              <!-- $num = count($this->share['data']); -->            </div>\n" +
                "          </div>\n" +
                "          <div class=\"ft\">\n" +
                "          </div>\n" +
                "        </div>\n" +
                "        <input type=\"hidden\" id=\"eventtype\" value=\"click\"/>\n" +
                "      </div>\n" +
                "      <!-- F10 Content End -->      <!-- F10 Footer Start -->      <div class=\"footer\">\n" +
                "        <div class=\"links\">\n" +
                "          <a href=\"http://stockpage.10jqka.com.cn\" target=\"_blank\" onclick=\"clickTalogStat('jptostock');\">\n" +
                "            同花顺个股页面\n" +
                "          </a>\n" +
                "           |\n" +
                "\t\t    \n" +
                "          <a href=\"http://moni.10jqka.com.cn/\" target=\"_blank\">\n" +
                "            模拟炒股\n" +
                "          </a>\n" +
                "           |\n" +
                "\t\t    \n" +
                "          <a href=\"http://www.10jqka.com.cn/school/\" target=\"_blank\">\n" +
                "            股民学校\n" +
                "          </a>\n" +
                "           |\n" +
                "\t\t    \n" +
                "          <a href=\"http://mobile.10jqka.com.cn/?req=pcf10\" target=\"_blank\">\n" +
                "            手机炒股\n" +
                "          </a>\n" +
                "          |\n" +
                "\t\t    \n" +
                "          <a target=\"_blank\" href=\" http://wpa.qq.com/msgrd?v=3&amp;uin=2270716061&amp;site=qq&amp;menu=yes\">\n" +
                "            联系我们\n" +
                "          </a>\n" +
                "        </div>\n" +
                "        <p>\n" +
                "          免责声明:本信息由同花顺金融研究中心提供，仅供参考，同花顺金融研究中心力求但不保证数据的完全准确，如有错漏请以中国证监会指定上市公司信息披露媒体为准，同花顺金融研究中心不对因该资料全部或部分内容而引致的盈亏承担任何责任。用户个人对服务的使用承担风险。同花顺对此不作任何类型的担保。同花顺不担保服务一定能满足用户的要求，也不担保服务不会受中断，对服务的及时性，安全性，出错发生都不作担保。同花顺对在同花顺上得到的任何信息服务或交易进程不作担保。同花顺提供的包括同花顺理财的所有文章，数据，不构成任何的投资建议，用户查看或依据这些内容所进行的任何行为造成的风险和结果都自行负责，与同花顺无关。 \n" +
                "        </p>\n" +
                "      </div>\n" +
                "      <!-- F10 Footer End -->    </div>\n" +
                "    <div class=\"r-go-top\" id=\"r-go-top\">\n" +
                "      <div class=\"f10_append_con\">\n" +
                "        <span class=\"f10_append_box1\">\n" +
                "          <a title=\"图解\" onclick=\"sendTalog('f10_stock_explainf10')\" class=\"f10_append_explain\" linksrc=\"http://basic.10jqka.com.cn/explain.html\" href=\"javascript:void(0);\">\n" +
                "          </a>\n" +
                "        </span>\n" +
                "        <span class=\"f10_append_box2\">\n" +
                "          <a title=\"提建议\" onclick=\"sendTalog('f10_stock_wtfk')\" class=\"f10_append_advice idea\" href=\"javascript:void(0);\">\n" +
                "          </a>\n" +
                "        </span>\n" +
                "      </div>\n" +
                "      <div class=\"r-widget-con\">\n" +
                "        <a title=\"置顶\" class=\"f10_append_gotop f10_append_icons\" href=\"javascript:void(0);\">\n" +
                "        </a>\n" +
                "      </div>\n" +
                "    </div>\n" +
                "    <input id=\"stockCode\" type=\"hidden\" value=\"300972\"/>\n" +
                "    <input id=\"F001\" type=\"hidden\" value=\"A\"/>\n" +
                "    <input id=\"F002\" type=\"hidden\" value=\"深圳证券交易所\"/>\n" +
                "    <input id=\"F004\" type=\"hidden\" value=\"1\"/>\n" +
                "    <input id=\"stockName\" type=\"hidden\" value=\"万辰生物\"/>\n" +
                "    <input id=\"cateName\" type=\"hidden\" value=\"公司资料\"/>\n" +
                "    <input id=\"catecode\" type=\"hidden\" value=\"company\"/>\n" +
                "    <input id=\"fcatecode\" type=\"hidden\" value=\"company\"/>\n" +
                "    <input id=\"sid\" type=\"hidden\" value=\"F10new_gszl\"/>\n" +
                "    <input id=\"fid\" type=\"hidden\" value=\"F10,F10master,F10main,F10new\"/>\n" +
                "    <input id=\"qid\" type=\"hidden\" value=\"14585038\"/>\n" +
                "    <div style=\"display:none\" id=\"wordraddom\">\n" +
                "      <input type=\"hidden\" id=\"locationttype\" value=\"1\"/>\n" +
                "      <input type=\"hidden\" id=\"location\" value=\"concept.html#ifind\"/>\n" +
                "      <input type=\"hidden\" id=\"defaultcontent\" value=\"该股票最热门的概念题材\"/>\n" +
                "    </div>\n" +
                "    <!-- title 容器 -->    <div class=\"titleBox\" id=\"altlayer\" style=\"display:none; width: 260px;\">\n" +
                "      <div class=\"tipbox_bd p0_5\">\n" +
                "        <span style=\"line-height:24px;\" class=\"tip f14\">\n" +
                "          <span id=\"altlayer_content\">\n" +
                "          </span>\n" +
                "        </span>\n" +
                "      </div>\n" +
                "    </div>\n" +
                "    <script type=\"text/javascript\" crossorigin=\"\" src=\"//s.thsi.cn/cb?js/common/cefapi/1.5.5/cefApi.min.js;js/basic/stock/newPcJs/common.js\">\n" +
                "    </script>\n" +
                "    <script>\n" +
                "//<![CDATA[\n" +
                "\n");
        html.append(
            "try {\n" +
                "    external.createObject('Util');\n" +
                "    //document.getElementById(\"updownchange\").style.display = \"block\";\n" +
                "} catch (e) {}\n" +
                "\n" +
                "//]]>\n" +
                "    </script>\n" +
                "    <script type=\"text/javascript\" crossorigin=\"\" src=\"//s.thsi.cn/cb?js/jquery-1.8.3.min.js;js/basic/common/inheritance.js;js/basic/stock/popWin.js;js/basic/common/Model_v2.js;js/basic/stock/20200604153837/index.js;js/basic/stock/20200604153837/po_v2.js;js/basic/stock/scrollBind.js\">\n" +
                "    </script>\n" +
                "    <script type=\"text/javascript\" crossorigin=\"\" charset=\"utf-8\" src=\"//s.thsi.cn/js/basic/stockph_v2/stock/common_v3-2-c4f8cb.js\">\n" +
                "    </script>\n" +
                "    <!--[if IE ]><script type=\"text/javascript\"  crossorigin src=\"//s.thsi.cn/js/excanvas.min.js\"></script><![endif]-->    <!--[if IE 6]>\n" +
                "<script type=\"text/javascript\"  crossorigin src=\"//s.thsi.cn/js/basic/DD_belatedPNG_0.0.8a-min.js\"></script>\n" +
                "<script type=\"text/javascript\">DD_belatedPNG.fix('.company_logo,.btn-gnjx-left, .btn-gnjx-right, .concept_hot_icons,.iwc_searchbar,.iwc_searchbar .tips-icon,#autocomplete_search_input dd .icona,#autocomplete_search_input dd .iconb');</script>\n" +
                "<![endif]-->    <script type=\"text/javascript\" crossorigin=\"\" src=\"//s.thsi.cn/js/basic/jquery.pos-fixed.js\">\n" +
                "    </script>\n" +
                "    <script type=\"text/javascript\" crossorigin=\"\" src=\"//s.thsi.cn/cb?js/ta.min.js;js/pa.min.js;js/basic/stock/StatLoad.js;js/basic/stock/20200605135220/xgcl_mod.js\">\n" +
                "    </script>\n" +
                "    <script type=\"text/javascript\" crossorigin=\"\" src=\"//s.thsi.cn/js/basic/stock/tongji.min.js\">\n" +
                "    </script>\n" +
                "    <!-- 统计 -->    <script type=\"text/javascript\" crossorigin=\"\" src=\"//s.thsi.cn/cb?js/home/ths_core.min.js;js/home/ths_quote.min.js;js/home/tongji.min.js\" charset=\"utf-8\">\n" +
                "    </script>\n" +
                "    <script type=\"text/javascript\" crossorigin=\"\" src=\"//s.thsi.cn/js/basic/stock/commonLog_v3.js\" charset=\"utf-8\">\n" +
                "    </script>\n" +
                "    <!--F10运营位脚本-->    <script type=\"text/javascript\" crossorigin=\"\" src=\"//s.thsi.cn/cb?/js/basic/stock/20200604153837/append_v3.js;/js/basic/stock/newPcJs/20200604153837/f10widget_v3.js\">\n" +
                "    </script>\n" +
                "    <script type=\"text/javascript\" crossorigin=\"\" src=\"//s.thsi.cn/cb?/js/navigation/jquery.bgiframe.min.js;/js/navigation/jquery.ui.position.min.js\">\n" +
                "    </script>\n" +
                "    <script type=\"text/javascript\" crossorigin=\"\" src=\"//s.thsi.cn/cb?js/basic/stock/json2.js;js/basic/stock/20200605135220/autocomplete_v2_202001081500.js;js/basic/stock/search_v3.js;js/basic/stock/search-guide.js\">\n" +
                "    </script>\n" +
                "    <div class=\"autocomplete\" id=\"autocomplete_search_input\" style=\"display: none; z-index: 1639920944; position: fixed; width: 173px;\">\n" +
                "    </div>\n" +
                "    <script type=\"text/javascript\" crossorigin=\"\" src=\"//s.thsi.cn//sns/sea-modules/gallery/ckplayer6.7/ckplayer/ckplayer-homepage.js\">\n" +
                "    </script>\n" +
                "    <script type=\"text/javascript\" charset=\"utf-8\" src=\"//s.thsi.cn/js/basic/stockph_v2/stock/tableSorter_v6-00bff1.js\">\n" +
                "    </script>\n" +
                "    <script type=\"text/javascript\" crossorigin=\"\" src=\"//s.thsi.cn/cb?js/basic/stock/pagescroll.js;js/basic/common/investTree.js\">\n" +
                "    </script>\n" +
                "    <script type=\"text/javascript\" crossorigin=\"\" src=\"//s.thsi.cn/js/basic/stock/userevaluate_v3.js\">\n" +
                "    </script>\n" +
                "    <script type=\"text/javascript\" crossorigin=\"\" src=\"//s.thsi.cn/js/basic/stock/jquery-ui.min.js\">\n" +
                "    </script>\n" +
                "    <script type=\"text/javascript\" crossorigin=\"\" src=\"//s.thsi.cn/js/basic/stock/highcharts.js\">\n" +
                "    </script>\n" +
                "    <script type=\"text/javascript\" crossorigin=\"\" src=\"//s.thsi.cn/js/basic/stock/patentData.20180130.js\">\n" +
                "    </script>\n" +
                "    <script type=\"text/javascript\" crossorigin=\"\" src=\"//s.thsi.cn/js/basic/stock/jquery.mousewheel.min.js\">\n" +
                "    </script>\n" +
                "    <script type=\"text/javascript\" crossorigin=\"\" src=\"//s.thsi.cn/js/basic/stock/jquery.mCustomScrollbar.min.js\">\n" +
                "    </script>\n" +
                "    <script type=\"text/javascript\" crossorigin=\"\" src=\"//s.thsi.cn/js/basic/stock/20200605135220/product_cmper.js\">\n" +
                "    </script>\n" +
                "    <script type=\"text/javascript\" crossorigin=\"\" src=\"//s.thsi.cn/js/basic/stock/pop_v2.js\">\n" +
                "    </script>\n" +
                "    <!--工商数据-->    <script type=\"text/javascript\" crossorigin=\"\" src=\"//s.thsi.cn/cb?js/home/v5/thirdpart/scrollbar/jquery.mCustomScrollbar.concat.min.js\">\n" +
                "    </script>\n" +
                "    <script type=\"text/javascript\" charset=\"utf-8\" src=\"//s.thsi.cn/js/basic/stockph_v2/stock/instajax-79a8e6.js\">\n" +
                "    </script>\n" +
                "    <script>\n" +
                "//<![CDATA[\n" +
                "\n" +
                "//---------------自定义title弹窗 start----------\n" +
                "var tempalt='';//临时存储title\n" +
                "document.body.onmousemove = function(event) {\n" +
                "\tif(altlayer.style.display==''){\n" +
                "\t\t$(\"#altlayer\").css({\n" +
                "\t\t\tleft: event.pageX,\n" +
                "\t\t\ttop: event.pageY+10\n" +
                "\t\t});\n" +
                "\t}\n" +
                "};\n" +
                "document.body.onmouseover = function(event) {\n" +
                "\tif (event.srcElement.className !== 'titleIcon') {\n" +
                "\t\treturn false;\n" +
                "\t}\n" +
                "\tif(event.srcElement.title && (event.srcElement.title!='' || (event.srcElement.title=='' && tempalt!=''))){\n" +
                "\t\t$(\"#altlayer\").css({\n" +
                "\t\t\tleft: event.pageX,\n" +
                "\t\t\ttop: event.pageY+20,\n" +
                "\t\t\tdisplay: ''\n" +
                "\t\t});\n" +
                "\t\t$(\"#altlayer_content\").html(event.srcElement.title)\n" +
                "\t\ttempalt = event.srcElement.title;\n" +
                "\t\tevent.srcElement.title='';\n" +
                "\t}\n" +
                "};\n" +
                "document.body.onmouseout = function(event) {\n" +
                "\tif (tempalt != '') {\n" +
                "\t\tevent.srcElement.title = tempalt;\t\n" +
                "\t}\n" +
                "\ttempalt = '';\n" +
                "\t$(\"#altlayer\").hide();\n" +
                "};\n" +
                "//---------------自定义title弹窗 end----------\n" +
                "//F10客户端结点设置\n" +
                "function setClientCacheData() {\n" +
                "    try { //判断是否在客户端，存入结点\n" +
                "        external.createObject('Util');\n" +
                "        window.API.use({\n" +
                "            method: 'Util.getHxVer',\n" +
                "            success: function(data) {\n" +
                "                var hexinVer = getVersionStr(data);\n" +
                "                var pathname = window.location.pathname\n" +
                "                pathname = pathname.replace(/\\/\\d{2,3}\\//, '/%4/');\n" +
                "                pathname = pathname.replace(/\\/[\\d|A]\\d{5}\\//, '/%6/');\n" +
                "                //dalert(hexinVer);//上线注意，测试版版本86080  正式版 86090 请注意\n" +
                "                if (hexinVer >= 86080) {\n" +
                "                    window.API.use({\n" +
                "                        method: 'Info.cacheInfoData',\n" +
                "                        data: ['f10_client_node', 'http://basic.10jqka.com.cn' + pathname],\n" +
                "                        success: function() {\n" +
                "                        }\n" +
                "                    })\n" +
                "                } else {\n" +
                "                    window.API.use({\n" +
                "                        method: 'Info.cacheInfoData',\n" +
                "                        data: ['', 'http://basic.10jqka.com.cn/' + pathname],\n" +
                "                        success: function() {\n" +
                "                        }\n" +
                "                    })\n" +
                "                }\n" +
                "            }\n" +
                "        })\n" +
                "        document.getElementById(\"updownchange\").style.display = \"block\";\n" +
                "    } catch (e) {\n" +
                "        //远航版隐藏header\n" +
                "        $(\"#quotedata\").empty()\n" +
                "        $(\"#updownchange\").empty()\n" +
                "    }\n" +
                "\ttry {\n" +
                "\t\tcallNativeHandler('getMacVer', {}, function(data){\n" +
                "\t\t\tif (data.plaform == 'mac' && data.version>='1.3.0' ) {\n" +
                "\t\t\t\tvar pathname = window.location.pathname\n" +
                "                pathname = pathname.replace(/\\/\\d{2,3}\\//, '/%4/');\n" +
                "                pathname = pathname.replace(/\\/[\\d|A]\\d{5}\\//, '/%6/');\n" +
                "\t\t\t\tcallNativeHandler('cache_info_Data', {\n" +
                "\t\t\t\t\t'f10_client_node': 'http://basic.10jqka.com.cn' + pathname\n" +
                "\t\t\t\t}, function(data){\n" +
                "\t\t\t\t});\n" +
                "\t\t\t}\n" +
                "\t\t});\n" +
                "\t} catch (e) {\n" +
                "    }\n" +
                "}\n" +
                "setClientCacheData();\n" +
                "\n" +
                "//]]>\n" +
                "    </script>\n" +
                "    <script type=\"text/javascript\" crossorigin=\"\" src=\"//s.thsi.cn/js/basic/stock/20200604153837/stockpage_v3.20190506.js\">\n" +
                "    </script>\n" +
                "    <script type=\"text/javascript\">\n" +
                "//<![CDATA[\n" +
                "\n" +
                "var sid = 'F10new_gszl';\n" +
                "var fid = 'F10,F10master,F10main,F10new';\n" +
                "var stockcode = '300972';\n" +
                "if (window.location.href.indexOf('interactive')<0\n" +
                "\t\t&&window.location.href.indexOf('dupont')<0) {\n" +
                "    $(\".subnav .skipto\").each(function(){\n" +
                "    \tsonnav = $(this).attr('nav');\n" +
                "    \tif ($(\"#\"+sonnav).length>0 || $(\"[idchange='\"+sonnav+\"']\").length>0) {\n" +
                "    \t} else {\n" +
                "    \t\t$(this).hide();\n" +
                "    \t}\n" +
                "    })\n" +
                "}\n" +
                "if (stockcode.substr(0,2) == '43' || stockcode.substr(0,2) == '83') {\n" +
                "\tvar third_sid = sid+'_thirdboard';\n" +
                "\tvar third_fid = fid+'_thirdboard';\n" +
                "    PA.setStartTime(loadTimer);\n" +
                "\tPA.init({'id':third_sid, 'fid':third_fid, 'stockcode':stockcode});\n" +
                "}\n" +
                "$(document).ready(function(){\n" +
                "var hash = window.location.hash.substring(1);\n" +
                "var hashArray = hash.split('-');\n" +
                "if(hashArray[0]  == 'position'){\n" +
                "\t$(\"#sortNav li\").eq(hashArray[1]).find(\"a\").click();\n" +
                "}\n" +
                "});\n" +
                "try {\n" +
                "    external.createObject('Util');\n" +
                "    window.API.use({\n" +
                "        method: 'Passport.get',\n" +
                "        data: ['m_qs'],\n" +
                "        success: function(data) {\n" +
                "            var qsid = data\n" +
                "            if (qsid == '114') {\n" +
                "                $(\"#f10_top_ad\").hide();\n" +
                "                $(\"#r-go-top\").hide();\n" +
                "            }\n" +
                "        }\n" +
                "    })\n" +
                "} catch (e) {}\n" +
                "if (!isIE6()) {\n" +
                "    $(\".m_box\").statload();\n" +
                "}\n" +
                "try {\n" +
                "    external.createObject('Util');\n" +
                "    window.API.use({\n" +
                "        method: 'Passport.get',\n" +
                "        data: 'm_qs',\n" +
                "        success: function(data) {\n" +
                "            var qsid = data\n" +
                "            if (qsid < 800) {\n" +
                "                fid += ',f10new_qs';\n" +
                "            } else {\n" +
                "                fid += ',f10new_fqs';\n" +
                "            }\n" +
                "            PA.setStartTime(loadTimer);\n" +
                "            setTimeout(function(){\n" +
                "                PA.init({\n" +
                "                    'id': sid,\n" +
                "                    'fid': fid,\n" +
                "                    'stockcode': stockcode,\n" +
                "                    'qsid': qsid\n" +
                "                });\n" +
                "            },100)\n" +
                "        }\n" +
                "    })\n" +
                "} catch (e) {\n" +
                "    PA.setStartTime(loadTimer);\n" +
                "    PA.init({\n" +
                "        'id': sid,\n" +
                "        'fid': fid,\n" +
                "        'stockcode': stockcode\n" +
                "    });\n" +
                "}\n" +
                "try {\n" +
                "    external.createObject('Util');\n" +
                "    var externalSessionId = window.API.createSessionId('external');\n" +
                "    window.API.use({\n" +
                "        method: 'external.registerEvent',\n" +
                "        data: 'onshow',\n" +
                "        sessionId: externalSessionId,\n" +
                "        persistent: true,\n" +
                "        callbackName: 'onshow',\n" +
                "        success: function(data) {\n" +
                "            if (!data) {\n" +
                "                 PA.setStartTime(loadTimer);\n" +
                "                 PA.init({\n" +
                "                    'id': sid,\n" +
                "                    'fid': fid,\n" +
                "                    'stockcode': stockcode,\n" +
                "                    'hide': 1,\n" +
                "                    'nj': 1,\n" +
                "                    _sid: \"__ths_onshow\"\n" +
                "                });\n" +
                "            }\n" +
                "        }\n" +
                "    })\n" +
                "} catch (e) {}\n" +
                "\n" +
                "//]]>\n" +
                "    </script>\n" +
                "    <img id=\"__ths_compnay_detail\" height=\"0\" width=\"0\" style=\"display:none\" src=\"http://stat.10jqka.com.cn/q?dt=1&amp;tt=1&amp;rt=NaN&amp;bt=2&amp;opentime=1639920946805&amp;sp=1&amp;id=compnay_detail&amp;ld=browser&amp;size=1920x1080&amp;nj=1&amp;url=http%3A%2F%2Fbasic.10jqka.com.cn%2F300972%2Fcompany.html&amp;cs=1256x4634&amp;ts=1639920946801\"/>\n" +
                "    <div style=\"display:none\">\n" +
                "      <script type=\"text/javascript\">\n" +
                "//<![CDATA[\n" +
                "\n" +
                "(function(){\n" +
                "var _bdhmProtocol = ((\"https:\" == document.location.protocol) ? \" https://\" : \" http://\");\n" +
                "document.write(unescape(\"%3Cscript src='\" + _bdhmProtocol + \"hm.baidu.com/h.js%3F78c58f01938e4d85eaf619eae71b4ed1' type='text/javascript'%3E%3C/script%3E\"));\n" +
                "});\n" +
                "\n" +
                "//]]>\n" +
                "      </script>\n" +
                "      <script type=\"text/javascript\" charset=\"utf-8\" crossorigin=\"\" src=\"//s.thsi.cn/js/basic/stockph_v2/stock/f10hq_v4-5-576b58.js\">\n" +
                "      </script>\n" +
                "      <script type=\"text/javascript\" charset=\"utf-8\" crossorigin=\"\" src=\"//s.thsi.cn/js/basic/stockph_v2/stock/onlyBAuthorize-4a6ce9.js\">\n" +
                "      </script>\n" +
                "    </div>\n" +
                "    <!--F10头部广告-->    <!--<div id=\"f10_top_ad\" class=\"f10_ad_top none\"><script type=\"text/javascript\">CNZZ_SLOT_RENDER('297563');</script></div>-->    <script type=\"text/javascript\" charset=\"utf-8\" src=\"//s.thsi.cn/js/basic/stockph_v2/stock/newCompany-682469.js\">\n" +
                "    </script>\n" +
                "    <ul class=\"autocomplete br\" style=\"display: none;\">\n" +
                "    </ul>\n" +
                "    <div class=\"xgcl_pop\" id=\"xg_pop\" style=\"display: none;height:auto\">\n" +
                "    </div>\n" +
                "    <div class=\"xg_mask\" style=\"display: none;\">\n" +
                "    </div>\n" +
                "    <img height=\"0\" width=\"0\" style=\"display:none\" src=\"http://stat.10jqka.com.cn/q?ld=browser&amp;scrratio=1920x1080&amp;optime=1639920943986&amp;acpage=1&amp;backcolor=black&amp;url_ver=THSNEWS-7004&amp;log_ver=2.0&amp;id=f10_gszl_300972-show&amp;ifjump=0&amp;nj=1&amp;frtype=0&amp;cururl=http%3A%2F%2Fbasic.10jqka.com.cn%2F300972%2Fcompany.html&amp;scrsize=1256x4634&amp;actime=1639920946955&amp;acloca=undefined,undefined\"/>\n" +
                "    <div class=\"VoteBox\" style=\"display: none;\">\n" +
                "      <div class=\"frbody\">\n" +
                "        <span class=\"closeBtn\">\n" +
                "          X\n" +
                "        </span>\n" +
                "        <p class=\"tit\">\n" +
                "          若您在使用F10的过程中发现错误或有好的建议，请在此反馈。\n" +
                "        </p>\n" +
                "        <textarea class=\"inputgray\" name=\"content\" value=\"请在此输入您的宝贵意见......\"></textarea>        <p class=\"tel clearfix\">\n" +
                "          <span>\n" +
                "            联系方式\n" +
                "          </span>\n" +
                "          <input type=\"text\" name=\"contact\" value=\"\" class=\"inputgray\"/>\n" +
                "          <a href=\"javascript:void(0);\" class=\"vbutton\">\n" +
                "            提交\n" +
                "          </a>\n" +
                "        </p>\n" +
                "      </div>\n" +
                "      <div class=\"qbox\">\n" +
                "        <div class=\"qtit\">\n" +
                "          常见问题\n" +
                "        </div>\n" +
                "        <ul class=\"qlist\">\n" +
                "          <li class=\"hd\">\n" +
                "            问题1：为什么我在这里反馈上市公司的问题没有答复？\n" +
                "          </li>\n" +
                "          <li class=\"cont\">\n" +
                "            回答：该平台是同花顺F10的问题反馈平台，而不是您对上市公司提问的平台。若您需要对上市公司进行提问，请点击\n" +
                "            <a href=\"/300972/interactive.html#interactive\" id=\"r-widget-interact-f10\" taid=\"f10_inter_tw\" title=\"投资者互动平台\">\n" +
                "              投资者互动平台&gt;&gt;\n" +
                "            </a>\n" +
                "          </li>\n" +
                "          <li class=\"hd\">\n" +
                "            问题2：同花顺F10股东人数、主力持仓情况为什么不是每天更新？\n" +
                "          </li>\n" +
                "          <li class=\"cont\">\n" +
                "            回答：上市公司的股东人数数据由上市公司提供，同花顺F10仅提供数据的展示，故不是每日进行更新。\n" +
                "          </li>\n" +
                "          <li class=\"hd\">\n" +
                "            问题3：同花顺F10数据较多，查找不到相应的数据怎么办？\n" +
                "          </li>\n" +
                "          <li class=\"cont\">\n" +
                "            回答：您可以通过F10右上角的搜索功能进行搜索相关的数据。比如查找“同花顺总股本是多少”，您只需要输入‘同花顺总股本’点击搜索即可。\n" +
                "          </li>\n" +
                "          <li class=\"hd\">\n" +
                "            问题4：发现同花顺F10的错误怎么办？\n" +
                "          </li>\n" +
                "          <li class=\"cont\">\n" +
                "            回答：您可以在同花顺F10右侧的问题反馈栏目直接进行反馈，或者拨打同花顺的客服热线95105885进行咨询。\n" +
                "          </li>\n" +
                "          <li class=\"hd\">\n" +
                "            问题5：发现同花顺F10的数据更新慢应该怎么办？\n" +
                "          </li>\n" +
                "          <li class=\"cont\">\n" +
                "            回答：正常情况下，同花顺F10会在第一时间内更新上市公司公告的数据，如果您发现部分数据更新不及时的情况欢迎在此反馈平台进行反馈。\n" +
                "          </li>\n" +
                "          <li class=\"hd\">\n" +
                "            问题6：同花顺F10的数据准确吗？\n" +
                "          </li>\n" +
                "          <li class=\"cont\">\n" +
                "            回答：同花顺F10的数据均来自于上市公司的公告，数据精确，但不排除极少数人工录入错误，如有分歧请以上市公司公告为准。\n" +
                "          </li>\n" +
                "        </ul>\n" +
                "      </div>\n" +
                "      <input type=\"hidden\" name=\"cate_id\" value=\"0\"/>\n" +
                "      <input type=\"hidden\" name=\"username\" value=\"\"/>\n" +
                "      <input type=\"hidden\" name=\"code\" value=\"\"/>\n" +
                "    </div>\n" +
                "  </body>\n" +
                "</html>\n");

    }

    @BeforeEach
    void before() {
        wc.getCookieManager().setCookiesEnabled(true);
        wc.getOptions().setJavaScriptEnabled(true);
        wc.getOptions().setCssEnabled(false);
        wc.getOptions().setThrowExceptionOnFailingStatusCode(false);
        wc.getOptions().setThrowExceptionOnScriptError(false);
        wc.getOptions().setTimeout(10000);
        makeCookies(wc);
        wc.addRequestHeader(org.springframework.http.HttpHeaders.CONNECTION, "keep-alive");
        wc.addRequestHeader(org.springframework.http.HttpHeaders.CACHE_CONTROL, "max-age=0");
//        wc.addRequestHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"96\", \"Google Chrome\";v=\"96\"");
//        wc.addRequestHeader("sec-ch-ua-mobile", "?0");
//        wc.addRequestHeader("sec-ch-ua-platform", "macOS");
        wc.addRequestHeader("Upgrade-Insecure-Requests", "1");
        wc.addRequestHeader(org.springframework.http.HttpHeaders.USER_AGENT, "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.93 Safari/537.36");
        wc.addRequestHeader(org.springframework.http.HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
//        wc.addRequestHeader("Sec-Fetch-Site", "none");
//        wc.addRequestHeader("Sec-Fetch-Mode", "navigate");
//        wc.addRequestHeader("Sec-Fetch-User", "?1");
//        wc.addRequestHeader("Sec-Fetch-Dest", "document");
        wc.addRequestHeader(org.springframework.http.HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.9");
    }

    private void makeCookies(WebClient wc) {
        String ck = "log=''__utma=156575163.1888104301.1620542472.1620542472.1639580835.2; __utmz=156575163.1639580835.2.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); spversion=20130314; Hm_lvt_78c58f01938e4d85eaf619eae71b4ed1=1639580829,1639917512; searchGuide=sg; Hm_lpvt_78c58f01938e4d85eaf619eae71b4ed1=1639923802; historystock=300972%7C*%7C600371%7C*%7C600466; v=A5AnOHthxaco-JnTzrdRQnmrZ9XnWXQglj3Ip4phXOu-xT7LMmlEM-ZNnSfZ";
        for (String s : ck.split(";")) {
            Cookie cookie = new Cookie("http://q.10jqka.com.cn", s.split("=")[0], s.split("=")[1]);
            wc.getCookieManager().addCookie(cookie);
        }

    }

    /**
     * 证监会行业
     *
     * @throws IOException
     */
    @Test
    void updateZjhhy1() throws IOException {
        String url = "http://q.10jqka.com.cn/zjhhy/";
        wc.getOptions().setJavaScriptEnabled(true);
        wc.getOptions().setCssEnabled(true);
        final HtmlPage htmlPage = wc.getPage(url);
        List<HtmlDivision> htmlDivisionList = htmlPage.getByXPath("/html/body/div[2]/div[1]//div[contains(@class, 'cate_items')]");
        for (HtmlDivision htmlDivision : htmlDivisionList) {
            Iterator<DomElement> iterator = htmlDivision.getChildElements().iterator();
            while (iterator.hasNext()) {
                DomElement domElement = iterator.next();
                domElement.getAttribute("href");
                String crsCodeName = domElement.getTextContent();
                String crsCode = domElement.getAttributeDirect("href").split("code")[1].replaceAll("/", "");
                log.info("crsCodeName :{},crsCode：{}", crsCodeName, crsCode);
                System.out.println(("crsCodeName :" + crsCodeName + ",crsCode：" + crsCode));
            }
        }
        log.info("完整的行业页面htmlPage：{}", htmlPage.asXml());
    }

    @Test
    void updateZjhhy2() throws IOException {
        String url = "http://q.10jqka.com.cn/zjhhy/";
        wc.getOptions().setJavaScriptEnabled(true);
        wc.getOptions().setCssEnabled(true);
        final HtmlPage htmlPage = wc.getPage(url);
        List<HtmlTable> htmlElementList = htmlPage.getByXPath("/html/body/div[2]/div[2]/div[3]/table");
        HtmlTable table = htmlElementList.get(0);
        for (int i = 1; i <= table.getRowCount(); i++) {
            String crsCodeName = table.getCellAt(i, 1).asNormalizedText();
            String crsCode = table.getCellAt(i, 1).getFirstChild().getAttributes().getNamedItem("href").getNodeValue().split("code")[1].replaceAll("/", "");
            String number = table.getCellAt(i, 2).asNormalizedText();
            System.out.println("crsCodeName," + crsCodeName);
            System.out.println("crsCode," + crsCode);
            System.out.println("number," + number);
        }
        log.info("完整的行业页面htmlPage：{}", htmlPage.asXml());
    }

    @Test
    void hyDetail() throws IOException, InterruptedException {
        String url = "http://q.10jqka.com.cn/zjhhy/detail/code/C";
        final HtmlPage htmlPage = wc.getPage(url);
        log.info("完整的行业详情页面htmlPage：{}", htmlPage.asXml());
        boolean hasNext = true;
        int currentPage = 1;
        while (hasNext) {
            System.out.println("第" + currentPage + "页");
            parseHyDetail(htmlPage);
            boolean flag = false;
            for (DomElement domElement : htmlPage.getElementById("m-page").getChildElements()) {
                String element = domElement.getTextContent();
                if ("下一页".equals(element)) {
                    flag = true;
                    currentPage++;
                    domElement.click();
                    TimeUnit.SECONDS.sleep(RandomUtils.nextInt(6, 60));
                }
            }
            hasNext = flag;
        }
    }

    void parseHyDetail(HtmlPage htmlPage) {
        List<HtmlTable> htmlElementList = htmlPage.getByXPath("/html/body/div[2]/div[2]/div[3]/table");
        HtmlTable table = htmlElementList.get(0);
        for (int i = 1; i < table.getRowCount(); i++) {
            System.out.println(table.getCellAt(i, 1).asNormalizedText() + "");
        }
    }

    /**
     * f10 页面分析
     *
     * @throws IOException
     */
    @Test
//    @Ignore
    public void stockInfo() throws IOException {
        String url = "http://basic.10jqka.com.cn/300801/company.html";
        final HtmlPage htmlPage = wc.getPage(url);
        log.info("完整的F10页面htmlPage：{}", htmlPage.asXml());
        List<HtmlElement> htmlElementList = htmlPage.getByXPath("/html/body/div[3]/div[1]/div[2]/div[1]/div[2]/h1");
        //代码，股票名称，所在行业，市场，所属地域，发行价，PE，上市时间
        //code，name，industry，market，area，first_price，pe，list_date
        String code = htmlElementList.get(0).getVisibleText();
        htmlElementList = htmlPage.getByXPath("/html/body/div[3]/div[1]/div[2]/div[1]/div[1]/h1");
        String name = htmlElementList.get(0).getVisibleText();
        String industry = "";
        htmlElementList = htmlPage.getElementsById("detail").get(0).getElementsByTagName("table").get(0).getByXPath("//tr[@class=\"video-btn-box-tr\"]/td[3]/span");
        String area = htmlElementList.get(0).getVisibleText();
        htmlElementList = htmlPage.getByXPath("/html/body/div[3]/div[3]/div[3]/div[2]/table/tbody/tr[1]/td[3]/span");
        String firstPrice = htmlElementList.get(0).getVisibleText();
        htmlElementList = htmlPage.getByXPath("/html/body/div[3]/div[3]/div[3]/div[2]/table/tbody/tr[2]/td[2]/span");
        String pe = htmlElementList.get(0).getVisibleText();
        htmlElementList = htmlPage.getByXPath("/html/body/div[3]/div[3]/div[3]/div[2]/table/tbody/tr[2]/td[1]");
        String listDate = htmlElementList.get(0).getVisibleText();
    }

    @Test
    public void parse() throws IOException {
        final WebClient wc = new WebClient();
        HtmlPage htmlPage = wc.loadHtmlCodeIntoCurrentWindow(html.toString());
        List<HtmlElement> htmlElementList = htmlPage.getByXPath("/html/body/div[3]/div[1]/div[2]/div[1]/div[2]/h1");
        //代码，股票名称，所在行业，市场，所属地域，发行价，PE，上市时间
        //code，name，industry，market，area，first_price，pe，list_date
        String code = htmlElementList.get(0).getVisibleText();
        htmlElementList = htmlPage.getByXPath("/html/body/div[3]/div[1]/div[2]/div[1]/div[1]/h1");
        String name = htmlElementList.get(0).getVisibleText();
        String industry = "";
        htmlElementList = htmlPage.getByXPath("/html/body/div[3]/div[3]/div[1]/div[2]/table/tbody/tr[1]/td[3]/span");
        String area = htmlElementList.get(0).getVisibleText();
        htmlElementList = htmlPage.getByXPath("/html/body/div[3]/div[3]/div[3]/div[2]/table/tbody/tr[1]/td[3]/span");
        String firstPrice = htmlElementList.get(0).getVisibleText();
        htmlElementList = htmlPage.getByXPath("/html/body/div[3]/div[3]/div[3]/div[2]/table/tbody/tr[2]/td[2]/span");
        String pe = htmlElementList.get(0).getVisibleText();
        htmlElementList = htmlPage.getByXPath("/html/body/div[3]/div[3]/div[3]/div[2]/table/tbody/tr[2]/td[1]");
        String listDate = htmlElementList.get(0).getVisibleText();
        System.out.println("code" + code);
        System.out.println("name" + name);
        System.out.println("area" + area);
        System.out.println("firstPrice" + firstPrice);
        System.out.println("pe" + pe);
        System.out.println("listDate" + listDate);
    }

    @Test
    void test1() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            LocalDateTime now = LocalDateTime.now();
            TimeUnit.SECONDS.sleep(RandomUtils.nextInt(5, 30));
            System.out.println("耗时：" + now.until(LocalDateTime.now(), ChronoUnit.SECONDS));
        }
    }

}
