package com.myzuji.breadth.repository;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.sun.org.apache.xml.internal.dtm.ref.DTMNodeList;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.extern.slf4j.Slf4j;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.util.List;

@Slf4j
public class HttpHtmlUnitTest {

    static String html = "";

    @Test
    @Ignore
    void parserHtml() throws Exception {
        String xPathStr = "/html/body/div[8]/div[2]/div[2]/div[2]/div/div/div/div[1]/div[2]/div/div[2]/table";
        HtmlCleaner hcCleaner = new HtmlCleaner();
        TagNode tagNode = hcCleaner.clean(html);
        Document dom = new DomSerializer(new CleanerProperties()).createDOM(tagNode);
        XPath xPath = XPathFactory.newInstance().newXPath();
        Object result = xPath.evaluate(xPathStr, dom, XPathConstants.NODESET);
        String data = ((DTMNodeList) result).getDTMIterator().toString().replaceAll("/\\*\\*/", "");
        data = data.replaceAll("\\s+", "");
        String dataJ = data.substring(data.indexOf("=") + 1, data.length() - 1);
        log.info(dataJ);
    }

    @Test
    @Ignore
    void htmlUnit() throws Exception {
        final WebClient wc = new WebClient();
        wc.getCookieManager().setCookiesEnabled(true);
        wc.getOptions().setJavaScriptEnabled(true);
        wc.getOptions().setCssEnabled(true);
        wc.getOptions().setThrowExceptionOnFailingStatusCode(false);
        wc.getOptions().setThrowExceptionOnScriptError(false);
        wc.getOptions().setTimeout(10000);
        makeCookies(wc);
        wc.addRequestHeader(org.springframework.http.HttpHeaders.CONNECTION, "keep-alive");
        wc.addRequestHeader(org.springframework.http.HttpHeaders.PRAGMA, "no-cache");
        wc.addRequestHeader(org.springframework.http.HttpHeaders.CACHE_CONTROL, "no-cache");
        wc.addRequestHeader("Upgrade-Insecure-Requests", "1");
        wc.addRequestHeader(org.springframework.http.HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.114 Safari/537.36");
        wc.addRequestHeader(org.springframework.http.HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        wc.addRequestHeader(org.springframework.http.HttpHeaders.REFERER, "http://www.sse.com.cn/assortment/stock/areatrade/trade/detail.shtml?csrcCode=A");
        wc.addRequestHeader(org.springframework.http.HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.9");
        final HtmlPage htmlPage = wc.getPage("http://www.sse.com.cn/assortment/stock/areatrade/trade/");
        final List<?> div = htmlPage.getByXPath("/html/body/div[8]/div[2]/div[2]/div[2]/div/div/div/div[1]/div[2]/div/div[2]/table");
    }

    private void makeCookies(WebClient wc) {
        String ck = "sseMenuSpecial=8534; yfx_c_g_u_id_10000042=_ck21041011121216603955971171634; VISITED_MENU=^%^5B^%^228537^%^22^%^2C^%^228536^%^22^%^5D; yfx_f_l_v_t_10000042=f_t_1618024332661__r_t_1618024332661__v_t_1618026122689__r_c_0";
        for (String s : ck.split(";")) {
            Cookie cookie = new Cookie("http://www.sse.com.cn", s.split("=")[0], s.split("=")[1]);
            wc.getCookieManager().addCookie(cookie);
        }

    }

    @Test
    @Ignore
    void hyDataTest() {
        String dataJson = "[{\"companycode\":\"601118\",\"csrcCodeDesc\":\"农、林、牧、渔业\",\"fullname\":\"海南天然橡胶产业集团股份有限公司\",\"securityCodeA\":\"601118\",\"securityCodeB\":\"-\"},{\"companycode\":\"600540\",\"csrcCodeDesc\":\"农、林、牧、渔业\",\"fullname\":\"新疆赛里木现代农业股份有限公司\",\"securityCodeA\":\"600540\",\"securityCodeB\":\"-\"},{\"companycode\":\"600354\",\"csrcCodeDesc\":\"农、林、牧、渔业\",\"fullname\":\"甘肃省敦煌种业集团股份有限公司\",\"securityCodeA\":\"600354\",\"securityCodeB\":\"-\"},{\"companycode\":\"600257\",\"csrcCodeDesc\":\"农、林、牧、渔业\",\"fullname\":\"大湖水殖股份有限公司\",\"securityCodeA\":\"600257\",\"securityCodeB\":\"-\"},{\"companycode\":\"600097\",\"csrcCodeDesc\":\"农、林、牧、渔业\",\"fullname\":\"上海开创国际海洋资源股份有限公司\",\"securityCodeA\":\"600097\",\"securityCodeB\":\"-\"},{\"companycode\":\"600506\",\"csrcCodeDesc\":\"农、林、牧、渔业\",\"fullname\":\"新疆库尔勒香梨股份有限公司\",\"securityCodeA\":\"600506\",\"securityCodeB\":\"-\"},{\"companycode\":\"600359\",\"csrcCodeDesc\":\"农、林、牧、渔业\",\"fullname\":\"新疆塔里木农业综合开发股份有限公司\",\"securityCodeA\":\"600359\",\"securityCodeB\":\"-\"},{\"companycode\":\"600265\",\"csrcCodeDesc\":\"农、林、牧、渔业\",\"fullname\":\"云南景谷林业股份有限公司\",\"securityCodeA\":\"600265\",\"securityCodeB\":\"-\"},{\"companycode\":\"600108\",\"csrcCodeDesc\":\"农、林、牧、渔业\",\"fullname\":\"甘肃亚盛实业(集团)股份有限公司\",\"securityCodeA\":\"600108\",\"securityCodeB\":\"-\"},{\"companycode\":\"600598\",\"csrcCodeDesc\":\"农、林、牧、渔业\",\"fullname\":\"黑龙江北大荒农业股份有限公司\",\"securityCodeA\":\"600598\",\"securityCodeB\":\"-\"},{\"companycode\":\"600371\",\"csrcCodeDesc\":\"农、林、牧、渔业\",\"fullname\":\"万向德农股份有限公司\",\"securityCodeA\":\"600371\",\"securityCodeB\":\"-\"},{\"companycode\":\"600975\",\"csrcCodeDesc\":\"农、林、牧、渔业\",\"fullname\":\"湖南新五丰股份有限公司\",\"securityCodeA\":\"600975\",\"securityCodeB\":\"-\"},{\"companycode\":\"600467\",\"csrcCodeDesc\":\"农、林、牧、渔业\",\"fullname\":\"山东好当家海洋发展股份有限公司\",\"securityCodeA\":\"600467\",\"securityCodeB\":\"-\"}]";
    }

    @Test
    @Ignore
    void random() {
        System.out.println("jsonpCallback63138".length());
        for (int i = 0; i < 100; i++) {
            System.out.println((int) ((Math.random() * 9 + 1) * 10000));
        }
    }
}
