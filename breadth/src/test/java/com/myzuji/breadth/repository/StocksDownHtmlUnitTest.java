package com.myzuji.breadth.repository;

import com.gargoylesoftware.htmlunit.UnexpectedPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.util.Cookie;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 说明
 *
 * @author shine
 * @date 2021/12/11
 */
@Slf4j
public class StocksDownHtmlUnitTest {

    @Test
    @Ignore
    void htmlUnit() throws Exception {
        final WebClient wc = new WebClient();
        wc.getCookieManager().setCookiesEnabled(true);
        wc.getOptions().setJavaScriptEnabled(true);
        wc.getOptions().setCssEnabled(false);
        wc.getOptions().setThrowExceptionOnFailingStatusCode(false);
        wc.getOptions().setThrowExceptionOnScriptError(false);
        wc.getOptions().setTimeout(10000);
        makeCookies(wc);
        wc.addRequestHeader(org.springframework.http.HttpHeaders.CONNECTION, "keep-alive");
        wc.addRequestHeader(org.springframework.http.HttpHeaders.PRAGMA, "no-cache");
        wc.addRequestHeader(org.springframework.http.HttpHeaders.CACHE_CONTROL, "no-cache");
        wc.addRequestHeader("Upgrade-Insecure-Requests", "1");
        wc.addRequestHeader(org.springframework.http.HttpHeaders.USER_AGENT, "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.93 Safari/537.36");
        wc.addRequestHeader(org.springframework.http.HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        wc.addRequestHeader(org.springframework.http.HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.9");
        final HtmlPage htmlPage = wc.getPage("http://www.sse.com.cn/assortment/stock/list/share/");
        wc.waitForBackgroundJavaScript(10 * 1000);
        wc.setJavaScriptTimeout(30 * 1000);
        log.info("完整的页面htmlPage：{}", htmlPage.asXml());

        //选择股票类型
        HtmlSelect selectStocksType = (HtmlSelect) htmlPage.getElementById("single_select_2");
        List<HtmlOption> selectStocksTypeOptionsList = selectStocksType.getOptions();
        for (HtmlOption option : selectStocksTypeOptionsList) {
            String optionValue = option.getValueAttribute();
            HtmlPage page1 = option.click();
            //选择行业
            HtmlSelect selectStocksTypeCsrCode = (HtmlSelect) page1.getElementById("csrccode");
            List<HtmlOption> selectStocksTypeCsrCodeList = selectStocksTypeCsrCode.getOptions();
            for (HtmlOption htmlOption : selectStocksTypeCsrCodeList) {
                String csrCodeOptionValue = htmlOption.getTextContent();
                HtmlPage page2 = htmlOption.click();
                //查询结果
                HtmlButton queryButton = (HtmlButton) page2.getElementById("btnQuery");
                HtmlPage page3 = queryButton.click();
                //点击下载按钮
                String downloadButton = "/html/body/div[8]/div[2]/div[2]/div[2]/div/div/div/div[1]/div[2]/div[2]/div[1]/p/a";
                HtmlAnchor anchor = (HtmlAnchor) page3.getByXPath(downloadButton).get(0);
                UnexpectedPage page4 = anchor.click();
                try (InputStream downloadPage = page4.getInputStream()) {
                    saveFile(downloadPage, optionValue + csrCodeOptionValue + ".xlsx");
                }
                TimeUnit.SECONDS.sleep(RandomUtils.nextInt(10, 20));
            }
        }
    }

    void saveFile(InputStream is, String fileName) {
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            in = new BufferedInputStream(is);
            out = new BufferedOutputStream(new FileOutputStream(fileName));
            int len = -1;
            byte[] b = new byte[1024];
            while ((len = in.read(b)) != -1) {
                out.write(b, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void makeCookies(WebClient wc) {
        String ck = "yfx_c_g_u_id_10000042=_ck21121121504412408648032882946; sseMenuSpecial=8534; yfx_f_l_v_t_10000042=f_t_1639230644238__r_t_1639230644238__v_t_1639232130444__r_c_0; VISITED_MENU=%5B%228528%22%2C%2210025%22%2C%228536%22%5D";
        for (String s : ck.split(";")) {
            Cookie cookie = new Cookie("http://www.sse.com.cn", s.split("=")[0], s.split("=")[1]);
            wc.getCookieManager().addCookie(cookie);
        }

    }
}
