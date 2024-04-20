package com.myzuji.study.java.easyexcel;

import com.alibaba.excel.EasyExcel;
import com.myzuji.study.easyexcel.read.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class ReadTest {

    private static Logger LOGGER = null;

    @BeforeEach
    public void init() {
        System.setProperty("log4j.configurationFile", "log4j2.xml");
        LOGGER = LoggerFactory.getLogger(ReadTest.class);
    }

    /**
     * 最简单的读
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link DemoData0}
     * <p>
     * 2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器，参照{@link DemoData0Listener}
     * <p>
     * 3. 直接读即可
     */
    @Test
    @Disabled
    public void simpleRead() {
        String merFile = ReadTest.class.getResource("/").getPath() + "easyexcel" + File.separator + "demo" +
            File.separator + "demo.xlsx";
        EasyExcel.read(merFile, DemoData3.class, new DemoData3Listener()).sheet().doRead();
        // 有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
        // 写法1：
        String fileName = ReadTest.class.getResource("/").getPath() + "easyexcel" + File.separator + "demo" +
            File.separator + "demo.xlsx";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(fileName, DemoData1.class, new DemoData1Listener()).sheet().headRowNumber(5).doRead();
        LOGGER.info("执行完成");
    }
}
