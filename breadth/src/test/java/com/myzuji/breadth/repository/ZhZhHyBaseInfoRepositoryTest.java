package com.myzuji.breadth.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myzuji.breadth.domain.ZhHyBaseInfo;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class ZhZhHyBaseInfoRepositoryTest {

    @Resource
    private ZhHyBaseInfoRepository zhHyBaseInfoRepository;

    @Test
    @Ignore
    void save() throws JsonProcessingException {
        List<ZhHyBaseInfo> zhHyBaseInfos = makeHyBaseInfo();
        for (ZhHyBaseInfo zhHyBaseInfo : zhHyBaseInfos) {
            zhHyBaseInfoRepository.save(zhHyBaseInfo);
        }

    }

    private List<ZhHyBaseInfo> makeHyBaseInfo() throws JsonProcessingException {
        String jsongS = "[\n" +
            "    {\"csrcCode\":\"A\",\n" +
            "     \"csrcName\":\"农、林、牧、渔业\",\n" +
            "     \"stockNumber\":\"15\"\n" +
            "     },\n" +
            "    {\"csrcCode\":\"B\",\n" +
            "     \"csrcName\":\"采矿业\",\n" +
            "     \"stockNumber\":\"51\"\n" +
            "     },\n" +
            "    {\"csrcCode\":\"C\",\n" +
            "     \"csrcName\":\"制造业\",\n" +
            "     \"stockNumber\":\"1136\"\n" +
            "     },\n" +
            "    {\"csrcCode\":\"D\",\n" +
            "     \"csrcName\":\"电力、热力、燃气及水生产和供应业\",\n" +
            "     \"stockNumber\":\"76\"\n" +
            "     },\n" +
            "    {\"csrcCode\":\"E\",\n" +
            "     \"csrcName\":\"建筑业\",\n" +
            "     \"stockNumber\":\"48\"\n" +
            "     },\n" +
            "    {\"csrcCode\":\"F\",\n" +
            "     \"csrcName\":\"批发和零售业\",\n" +
            "     \"stockNumber\":\"102\"\n" +
            "     },\n" +
            "    {\"csrcCode\":\"G\",\n" +
            "     \"csrcName\":\"交通运输、仓储和邮政业\",\n" +
            "     \"stockNumber\":\"77\"\n" +
            "     },\n" +
            "    {\"csrcCode\":\"H\",\n" +
            "     \"csrcName\":\"住宿和餐饮业\",\n" +
            "     \"stockNumber\":\"5\"\n" +
            "     },\n" +
            "    {\"csrcCode\":\"I\",\n" +
            "     \"csrcName\":\"信息传输、软件和信息技术服务业\",\n" +
            "     \"stockNumber\":\"110\"\n" +
            "     },\n" +
            "    {\"csrcCode\":\"J\",\n" +
            "     \"csrcName\":\"金融业\",\n" +
            "     \"stockNumber\":\"84\"\n" +
            "     },\n" +
            "    {\"csrcCode\":\"K\",\n" +
            "     \"csrcName\":\"房地产业\",\n" +
            "     \"stockNumber\":\"71\"\n" +
            "     },\n" +
            "    {\"csrcCode\":\"L\",\n" +
            "     \"csrcName\":\"租赁和商务服务业\",\n" +
            "     \"stockNumber\":\"20\"\n" +
            "     },\n" +
            "    {\"csrcCode\":\"M\",\n" +
            "     \"csrcName\":\"科学研究和技术服务业\",\n" +
            "     \"stockNumber\":\"23\"\n" +
            "     },\n" +
            "    {\"csrcCode\":\"N\",\n" +
            "     \"csrcName\":\"水利、环境和公共设施管理业\",\n" +
            "     \"stockNumber\":\"29\"\n" +
            "     },\n" +
            "    {\"csrcCode\":\"O\",\n" +
            "     \"csrcName\":\"居民服务、修理和其他服务业\",\n" +
            "     \"stockNumber\":\"0\"\n" +
            "     },\n" +
            "    {\"csrcCode\":\"P\",\n" +
            "     \"csrcName\":\"教育\",\n" +
            "     \"stockNumber\":\"3\"\n" +
            "     },\n" +
            "    {\"csrcCode\":\"Q\",\n" +
            "     \"csrcName\":\"卫生和社会工作\",\n" +
            "     \"stockNumber\":\"3\"\n" +
            "     },\n" +
            "    {\"csrcCode\":\"R\",\n" +
            "     \"csrcName\":\"文化、体育和娱乐业\",\n" +
            "     \"stockNumber\":\"27\"\n" +
            "     },\n" +
            "    {\"csrcCode\":\"S\",\n" +
            "     \"csrcName\":\"综合\",\n" +
            "     \"stockNumber\":\"9\"\n" +
            "     }\n" +
            "    ]";
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, ZhHyBaseInfo.class);
        List<ZhHyBaseInfo> zhHyBaseInfos = objectMapper.readValue(jsongS, javaType);
        return zhHyBaseInfos;
    }

    @Test
    void findByCsrcCode() {
        ZhHyBaseInfo zhHyBaseInfo = zhHyBaseInfoRepository.findByCsrcCode("A");
        System.out.println(zhHyBaseInfo.getCsrcName());
    }

    @Test
    void findAll() {
        Iterable<ZhHyBaseInfo> hyBaseInfoIterable = zhHyBaseInfoRepository.findAll();
        for (ZhHyBaseInfo zhHyBaseInfo : hyBaseInfoIterable) {
            System.out.println(zhHyBaseInfo.getCsrcCode());
        }
    }

}
