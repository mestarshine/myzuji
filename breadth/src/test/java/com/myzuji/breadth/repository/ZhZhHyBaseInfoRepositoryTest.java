package com.myzuji.breadth.repository;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.myzuji.breadth.domain.ZhHyBaseInfo;
import com.myzuji.breadth.domain.ZhStocksInfo;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
class ZhZhHyBaseInfoRepositoryTest {

    static WebClient wc = new WebClient();
    @Resource
    private ZhHyBaseInfoRepository zhHyBaseInfoRepository;
    @Resource
    private ZhStocksInfoRepository zhStocksInfoRepository;

    @BeforeEach
    void initWc() {
        wc.getCookieManager().setCookiesEnabled(true);
        wc.getOptions().setJavaScriptEnabled(true);
        wc.getOptions().setCssEnabled(false);
        wc.getOptions().setThrowExceptionOnFailingStatusCode(false);
        wc.getOptions().setThrowExceptionOnScriptError(false);
        wc.getOptions().setTimeout(10000);
        wc.waitForBackgroundJavaScript(10 * 1000);
        wc.setJavaScriptTimeout(30 * 1000);
        makeCookies(wc);
        wc.addRequestHeader(org.springframework.http.HttpHeaders.CONNECTION, "keep-alive");
        wc.addRequestHeader(org.springframework.http.HttpHeaders.CACHE_CONTROL, "max-age=0");
        wc.addRequestHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"96\", \"Google Chrome\";v=\"96\"");
        wc.addRequestHeader("sec-ch-ua-mobile", "?0");
        wc.addRequestHeader("sec-ch-ua-platform", "macOS");
        wc.addRequestHeader("Upgrade-Insecure-Requests", "1");
        wc.addRequestHeader(org.springframework.http.HttpHeaders.USER_AGENT, "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.93 Safari/537.36");
        wc.addRequestHeader(org.springframework.http.HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        wc.addRequestHeader("Sec-Fetch-Site", "none");
        wc.addRequestHeader("Sec-Fetch-Mode", "navigate");
        wc.addRequestHeader("Sec-Fetch-User", "?1");
        wc.addRequestHeader("Sec-Fetch-Dest", "document");
        wc.addRequestHeader(org.springframework.http.HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.9");
    }

    private void makeCookies(WebClient wc) {
        String ck = "__utma=156575163.1888104301.1620542472.1620542472.1639580835.2; __utmz=156575163.1639580835.2.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); spversion=20130314; Hm_lvt_78c58f01938e4d85eaf619eae71b4ed1=1639580829,1639917512; searchGuide=sg; Hm_lpvt_78c58f01938e4d85eaf619eae71b4ed1=1640446808; historystock=002456%7C*%7C600371%7C*%7C300534%7C*%7C002714; v=A9dgjfhkepTEN_5r3Pi-T-qWYEAkHLIQhewmpykG8HcM2_k-Mew7zpXAv1s6";
        for (String s : ck.split(";")) {
            Cookie cookie = new Cookie("http://q.10jqka.com.cn", s.split("=")[0], s.split("=")[1]);
            wc.getCookieManager().addCookie(cookie);
        }

    }

    void initStockBaseInfoWc(WebClient wc) {
        wc.getCookieManager().setCookiesEnabled(true);
        wc.getOptions().setJavaScriptEnabled(true);
        wc.getOptions().setCssEnabled(false);
        wc.getOptions().setThrowExceptionOnFailingStatusCode(false);
        wc.getOptions().setThrowExceptionOnScriptError(false);
        wc.getOptions().setTimeout(10000);
        wc.waitForBackgroundJavaScript(10 * 1000);
        wc.setJavaScriptTimeout(30 * 1000);
        makeStockBaseInfoCookies(wc);
        wc.addRequestHeader(org.springframework.http.HttpHeaders.CONNECTION, "keep-alive");
        wc.addRequestHeader(org.springframework.http.HttpHeaders.CACHE_CONTROL, "max-age=0");
        wc.addRequestHeader("Upgrade-Insecure-Requests", "1");
        wc.addRequestHeader(org.springframework.http.HttpHeaders.USER_AGENT, "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.93 Safari/537.36");
        wc.addRequestHeader(org.springframework.http.HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        wc.addRequestHeader(org.springframework.http.HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.9");
    }

    private void makeStockBaseInfoCookies(WebClient wc) {
        String ck = "__utma=156575163.1888104301.1620542472.1620542472.1639580835.2; __utmz=156575163.1639580835.2.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); spversion=20130314; Hm_lvt_78c58f01938e4d85eaf619eae71b4ed1=1639580829,1639917512; searchGuide=sg; Hm_lpvt_78c58f01938e4d85eaf619eae71b4ed1=1640446808; historystock=002456%7C*%7C600371%7C*%7C300534%7C*%7C002714; v=A7kOI-pe_GHLMqD1hGX4CdgMzi6Wxq14l7rRDNvuNeBfYtdQIxa9SCcK4dBo";
        for (String s : ck.split(";")) {
            Cookie cookie = new Cookie("http://q.10jqka.com.cn", s.split("=")[0], s.split("=")[1]);
            wc.getCookieManager().addCookie(cookie);
        }

    }

    /**
     * 更新行业信息不包含公司家数
     *
     * @throws IOException
     */
    @Test
    @Ignore
    void updateZjhhy1() throws IOException {
        WebClient wc = new WebClient();
        String url = "http://q.10jqka.com.cn/zjhhy/";
        final HtmlPage htmlPage = wc.getPage(url);
        List<HtmlTable> htmlElementList = htmlPage.getByXPath("/html/body/div[2]/div[2]/div[3]/table");
        List<HtmlDivision> htmlDivisionList = htmlPage.getByXPath("/html/body/div[2]/div[1]//div[contains(@class, 'cate_items')]");
        for (HtmlDivision htmlDivision : htmlDivisionList) {
            Iterator<DomElement> iterator = htmlDivision.getChildElements().iterator();
            while (iterator.hasNext()) {
                DomElement domElement = iterator.next();
                domElement.getAttribute("href");
                String crscCodeName = domElement.getTextContent();
                String csrcCode = domElement.getAttributeDirect("href").split("code")[1].replaceAll("/", "");
                ZhHyBaseInfo zhHyBaseInfo = zhHyBaseInfoRepository.findByCsrcCode("csrcCode");
                if (zhHyBaseInfo == null) {
                    zhHyBaseInfo = ZhHyBaseInfo.builder().csrcCode(csrcCode).csrcName(crscCodeName).build();
                } else {
                    zhHyBaseInfo.setCsrcName(crscCodeName);
                }
                zhHyBaseInfoRepository.save(zhHyBaseInfo);
            }
        }
        log.info("完整的页面htmlPage：{}", htmlPage.asXml());
    }

    /**
     * 更新行业信息包含公司家数
     *
     * @throws IOException
     */
    @Test
    @Ignore
    void updateZjhhy2() throws IOException {
        WebClient wc = new WebClient();
        String url = "http://q.10jqka.com.cn/zjhhy/";
        final HtmlPage htmlPage = wc.getPage(url);
        List<HtmlTable> htmlElementList = htmlPage.getByXPath("/html/body/div[2]/div[2]/div[3]/table");
        HtmlTable table = htmlElementList.get(0);
        for (int i = 1; i <= table.getRowCount(); i++) {
            String crscCodeName = table.getCellAt(i, 1).asNormalizedText();
            String csrcCode = table.getCellAt(i, 1).getFirstChild().getAttributes().getNamedItem("href").getNodeValue().split("code")[1].replaceAll("/", "");
            String stockNumber = table.getCellAt(i, 2).asNormalizedText();
            ZhHyBaseInfo zhHyBaseInfo = zhHyBaseInfoRepository.findByCsrcCode("csrcCode");
            if (zhHyBaseInfo == null) {
                zhHyBaseInfo = ZhHyBaseInfo.builder().csrcCode(csrcCode).csrcName(crscCodeName).stockNumber(Integer.valueOf(stockNumber)).build();
            } else {
                zhHyBaseInfo.setCsrcName(crscCodeName);
                zhHyBaseInfo.setStockNumber(Integer.valueOf(stockNumber));
            }
            zhHyBaseInfoRepository.save(zhHyBaseInfo);
        }
        log.info("完整的页面htmlPage：{}", htmlPage.asXml());
    }

    /**
     * 获取基本信息
     *
     * @throws IOException
     */
    @Test
    @Ignore
    void stockBaseInfoTest() throws IOException, InterruptedException {
        LocalDateTime now = LocalDateTime.now();
        Iterable<ZhHyBaseInfo> hyBaseInfoIterable = zhHyBaseInfoRepository.findAll();
        WebClient wc = new WebClient();
        initStockBaseInfoWc(wc);
        for (ZhHyBaseInfo zhHyBaseInfo : hyBaseInfoIterable) {
            fetchHyDetailInfo(zhHyBaseInfo.getCsrcCode(), wc);
        }
        log.info("获取基本信息耗时：{}", now.until(LocalDateTime.now(), ChronoUnit.SECONDS));
    }

    /**
     * 获取行业基本信息
     *
     * @param csrcCode
     */
    private void fetchHyDetailInfo(String csrcCode, WebClient wc) throws IOException, InterruptedException {
        List<String> csrcExite = Arrays.asList("A", "B");
        if (csrcExite.contains(csrcCode)) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        String url = "http://q.10jqka.com.cn/zjhhy/detail/code/" + csrcCode;
        HtmlPage htmlPage = wc.getPage(url);
        boolean hasNext = true;
        String currentPage = "1";
        while (hasNext) {
            log.info("第{}页", currentPage);
            HtmlPage currentHtmlPage = htmlPage;
            parseHyDetail(currentHtmlPage, csrcCode);
            boolean flag = false;
            for (DomElement domElement : htmlPage.getElementById("m-page").getChildElements()) {
                String element = domElement.getTextContent();
                if ("下一页".equals(element)) {
                    flag = true;
                    String nextPage = domElement.getAttributeDirect("page");
                    currentPage = nextPage;
                    domElement.click();
                    log.info("行业：{},下一页：{}", csrcCode, currentPage);
                    TimeUnit.SECONDS.sleep(RandomUtils.nextInt(10, 60));
                }
            }
            hasNext = flag;
        }
        log.info("获取行业信息耗时：{}", now.until(LocalDateTime.now(), ChronoUnit.SECONDS));
    }

    void parseHyDetail(HtmlPage htmlPage, String csrcCode) throws IOException, InterruptedException {
        List<HtmlTable> htmlElementList = htmlPage.getByXPath("/html/body/div[2]/div[2]/div[3]/table");
        HtmlTable table = htmlElementList.get(0);
        for (int i = 1; i < table.getRowCount(); i++) {
            LocalDateTime now = LocalDateTime.now();
            String stockNumber = table.getCellAt(i, 1).asNormalizedText();
            ZhStocksInfo zhStocksInfo = zhStocksInfoRepository.findByStockCode(stockNumber);
            if (zhStocksInfo == null) {
                fetchStockBaseF10Info(stockNumber, csrcCode);
                TimeUnit.SECONDS.sleep(RandomUtils.nextInt(5, 30));
            }
            log.info("行业：{},代码：{}，处理耗时：{}", csrcCode, stockNumber, now.until(LocalDateTime.now(), ChronoUnit.SECONDS));
        }
    }

    /**
     * 抓取F10
     *
     * @param stockNumber
     * @throws IOException
     */
    private void fetchStockBaseF10Info(String stockNumber, String csrcCode) throws IOException {
        String url = "http://basic.10jqka.com.cn/" + stockNumber + "/company.html";
        final HtmlPage htmlPage = wc.getPage(url);
        List<HtmlElement> htmlElementList = htmlPage.getByXPath("/html/body/div[3]/div[1]/div[2]/div[1]/div[2]/h1");
        //代码，股票名称，所在行业，所属地域，发行价，PE，上市时间
        //code，name，industry，area，first_price，pe，list_date
        String code = htmlElementList.get(0).getVisibleText();
        htmlElementList = htmlPage.getByXPath("/html/body/div[3]/div[1]/div[2]/div[1]/div[1]/h1");
        String name = htmlElementList.get(0).getVisibleText();
        htmlElementList = htmlPage.getElementsById("detail").get(0).getElementsByTagName("table").get(0).getByXPath("//tr[@class=\"video-btn-box-tr\"]/td[3]/span");
        String area = htmlElementList.get(0).getVisibleText();
        htmlElementList = htmlPage.getByXPath("/html/body/div[3]/div[3]/div[3]/div[2]/table/tbody/tr[3]/td[1]/span");
        String firstPrice = htmlElementList.get(0).getVisibleText().replaceAll("元", "");
        htmlElementList = htmlPage.getByXPath("/html/body/div[3]/div[3]/div[3]/div[2]/table/tbody/tr[2]/td[2]/span");
        String pe = htmlElementList.get(0).getVisibleText().replaceAll("倍", "");
        htmlElementList = htmlPage.getByXPath("/html/body/div[3]/div[3]/div[3]/div[2]/table/tbody/tr[2]/td[1]/span");
        String listDate = htmlElementList.get(0).getVisibleText();
        ZhStocksInfo zhStocksInfo = ZhStocksInfo.builder()
            .stockCode(code)
            .stockName(name)
            .industry(csrcCode)
            .area("-".equals(area) ? "" : area)
            .firstPrice("-".equals(firstPrice) ? null : BigDecimal.valueOf(Double.parseDouble(firstPrice)))
            .pe("-".equals(pe) ? null : BigDecimal.valueOf(Double.parseDouble(pe)))
            .listDate(LocalDate.parse(listDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            .build();
        zhStocksInfoRepository.save(zhStocksInfo);
    }

    @Test
    @Ignore
    void findStocksInfo() {
        ZhStocksInfo zhStocksInfo = zhStocksInfoRepository.findByStockCode("002124");
        assert "002124".equals(zhStocksInfo.getStockCode());
    }

    @Test
    @Ignore
    void findByCsrcCode() {
        ZhHyBaseInfo zhHyBaseInfo = zhHyBaseInfoRepository.findByCsrcCode("A");
        System.out.println(zhHyBaseInfo.getCsrcName());
    }

    @Test
    @Ignore
    void findAll() {
        Iterable<ZhHyBaseInfo> hyBaseInfoIterable = zhHyBaseInfoRepository.findAll();
        for (ZhHyBaseInfo zhHyBaseInfo : hyBaseInfoIterable) {
            System.out.println(zhHyBaseInfo.getCsrcCode());
        }
    }

}
