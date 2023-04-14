package com.myzuji.breadth.repository;

import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class HttpClientTest {

    @Test
    @Ignore
    public void Test1() {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
//            .setDefaultCookieStore(makeCookies())
            .build();
        HttpPost httpPost = new HttpPost("https://pay.weixin.qq.com/index.php/extend/channel_sub_manage/queryMerchant?g_tk=767126085&g_ty=ajax");
        makeHeader(httpPost);
        try {
            httpPost.setEntity(makeRequest());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpPost);
            HttpEntity responseEntity = httpResponse.getEntity();
//            log.info("响应状态:{}", httpResponse.getStatusLine());
            if (responseEntity != null) {
//                log.info("响应内容长度为：{}",responseEntity.getContentLength());
//                log.info("响应内容为：{}", EntityUtils.toString(responseEntity));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
                if (httpResponse != null) {
                    httpResponse.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    @Ignore
    public void test2() {
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH24mmss")));
    }

    private HttpEntity makeRequest() throws UnsupportedEncodingException {
        List<NameValuePair> postData = new ArrayList<NameValuePair>();
        postData.add(new BasicNameValuePair("pageNum", "1"));
        postData.add(new BasicNameValuePair("ecc_csrf_token", "2d18d6c7481ce97f530fa699cd606516"));
        return new UrlEncodedFormEntity(postData);
    }

    private void makeHeader(HttpPost httpPost) {
        httpPost.setHeader("Connection","keep-alive");
        httpPost.setHeader("Connection","keep-alive");
        httpPost.setHeader("Pragma","no-cache");
        httpPost.setHeader("Cache-Control","no-cache");
        httpPost.setHeader("sec-ch-ua","\"Google Chrome\";v=\"89\", \"Chromium\";v=\"89\", \";Not A Brand\";v=\"99\"");
        httpPost.setHeader("If-Modified-Since","Thu, 1 Jan 1970 00:00:00 GMT");
        httpPost.setHeader("sec-ch-ua-mobile","?0");
        httpPost.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.114 Safari/537.36");
        httpPost.setHeader("Content-Type","application/x-www-form-urlencoded");
        httpPost.setHeader("Accept","*/*");
        httpPost.setHeader("Origin","https://pay.weixin.qq.com");
        httpPost.setHeader("Sec-Fetch-Site","same-origin");
        httpPost.setHeader("Sec-Fetch-Mode","cors");
        httpPost.setHeader("Sec-Fetch-Dest","empty");
        httpPost.setHeader("Referer","https://pay.weixin.qq.com/index.php/extend/channel_sub_manage");
        httpPost.setHeader("Accept-Language","zh-CN,zh;q=0.9");
        httpPost.setHeader("Cookie","RK=EFY84flvc8; ptcz=de215e918072452655b2729554f8279a6bf04dd6811e649c8bffe1c43ef6c5fb; pgv_pvid=9825234576; _ga=GA1.2.827912271.1611229318; pgv_pvi=4760330240; o_cookie=363161476; pac_uid=1_363161476; luin=o0363161476; iip=0; ts_uid=624101032; sd_userid=46531622797168931; sd_cookie_crttime=1622797168931; pgv_info=ssid=s7794505276; session_key=86773732bfd298022d4c70b07da1c85e; Lang=zh; uin=o0363161476; username=13681917565@242091043; mmode=normal; g_ticket=no_ticket_20200921; group_id=9555101914%40chatroom; agenttype=0; menu_template_id=26200; merchant_type=10; mchversion=4; wxp_log_uid=BgAAy%252FQNdieCx3TCjGdjO2uVUWND74mbPgZ0Z5OI4lRe9VETtVzjzg%253D%253D; client_ip=::ffff:58.37.58.112; login_from=1; is_to_launch=1; menu_plat_id=1; fingerprint_hash=d1536f0c6bcc7a6d7c10ebc75bfeeeab; _qpsvr_localtk=0.9080400003099047; skey=@cBoAqXrfj; lskey=00010000ca866bba5779a09a0088a15761a3d39385bb05b0034147caad3446a39b93d81df72e3ce12acfa341; ecc_csrf_cookie=2d18d6c7481ce97f530fa699cd606516; verifysession=h014191a3b74eda8e59cf49279d2264e14e39fbfd598816132f8e20e8a5e85dfd490ef320bfe40c7688; merchant_id=242091043; login_id=13681917565; staff_id=83248757; session_id=d985069015381dc5f4f2338cac371aea; employee_id=85477886; merchant_code=242091043; login_id_type=2; is_login=1; sid_hash=40a396cbfc829563abb1602e8f73224b; merchant_code_other=242091043; ts_last=pay.weixin.qq.com/index.php/extend/channel_sub_manage");
    }

    private static CookieStore makeCookies() {
        CookieStore cookieStore = new BasicCookieStore();
        String ck = "RK=EFY84flvc8; ptcz=de215e918072452655b2729554f8279a6bf04dd6811e649c8bffe1c43ef6c5fb; pgv_pvid=9825234576; _ga=GA1.2.827912271.1611229318; pgv_pvi=4760330240; o_cookie=363161476; pac_uid=1_363161476; luin=o0363161476; iip=0; ts_uid=624101032; sd_userid=46531622797168931; sd_cookie_crttime=1622797168931; pgv_info=ssid=s7794505276; session_key=86773732bfd298022d4c70b07da1c85e; Lang=zh; uin=o0363161476; username=13681917565@242091043; mmode=normal; g_ticket=no_ticket_20200921; group_id=9555101914@chatroom; agenttype=0; menu_template_id=26200; merchant_type=10; mchversion=4; wxp_log_uid=BgAAy%2FQNdieCx3TCjGdjO2uVUWND74mbPgZ0Z5OI4lRe9VETtVzjzg%3D%3D; client_ip=::ffff:58.37.58.112; login_from=1; is_to_launch=1; menu_plat_id=1; fingerprint_hash=d1536f0c6bcc7a6d7c10ebc75bfeeeab; _qpsvr_localtk=0.9080400003099047; skey=@cBoAqXrfj; lskey=00010000ca866bba5779a09a0088a15761a3d39385bb05b0034147caad3446a39b93d81df72e3ce12acfa341; ecc_csrf_cookie=2d18d6c7481ce97f530fa699cd606516; verifysession=h014191a3b74eda8e59cf49279d2264e14e39fbfd598816132f8e20e8a5e85dfd490ef320bfe40c7688; merchant_id=242091043; login_id=13681917565; staff_id=83248757; session_id=d985069015381dc5f4f2338cac371aea; employee_id=85477886; merchant_code=242091043; login_id_type=2; is_login=1; sid_hash=40a396cbfc829563abb1602e8f73224b; merchant_code_other=242091043; ts_last=pay.weixin.qq.com/index.php/extend/channel_sub_manage";
        for (String s : ck.split(";")) {
            BasicClientCookie cookie = new BasicClientCookie(s.split("=")[0],s.split("=")[1]);
            cookieStore.addCookie(cookie);
        }
        return cookieStore;
    }
}
