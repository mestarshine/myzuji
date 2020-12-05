package com.myzuji.study.java.easyexcel.read;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import com.myzuji.study.java.easyexcel.ReadTest;
import com.myzuji.study.java.easyexcel.write.CsvTemp;
import com.myzuji.study.java.easyexcel.write.Huizong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * 模板的读取类
 */
// 有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
public class DemoData0Listener extends AnalysisEventListener<com.myzuji.study.java.easyexcel.read.DemoData0> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoData0Listener.class);
    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 5000;
    private static final WriteSheet writeSheet = EasyExcel.writerSheet("模板").build();
    private static final HashMap<String, Huizong> huizongHashMap = new HashMap<String, Huizong>();
    private static final HashMap<String, ExcelWriter> excelWriterHashMap = new HashMap<String, ExcelWriter>();
    private static final HashMap<String, List<CsvTemp>> csvTempHashMap = new HashMap<String, List<CsvTemp>>();
    /**
     * 假设这个是一个DAO，当然有业务逻辑这个也可以是一个service。当然如果不用存储这个对象没用。
     */
    private final com.myzuji.study.java.easyexcel.read.DemoDAO demoDAO;

    public DemoData0Listener() {
        // 这里是demo，所以随便new一个。实际使用如果到了spring,请使用下面的有参构造函数
        demoDAO = new com.myzuji.study.java.easyexcel.read.DemoDAO();
    }

    /**
     * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
     *
     * @param demoDAO
     */
    public DemoData0Listener(com.myzuji.study.java.easyexcel.read.DemoDAO demoDAO) {
        this.demoDAO = demoDAO;
    }

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(com.myzuji.study.java.easyexcel.read.DemoData0 data, AnalysisContext context) {
//        LOGGER.info("解析到一条数据:{}", JSON.toJSONString(data));
        CsvTemp csvTemp = CsvTemp.makeByDemoData(data);

        if (csvTemp != null) {
            String reportDate = csvTemp.getReportDate();
            Huizong huizong = (huizongHashMap.containsKey(reportDate) ? huizongHashMap.get(reportDate) : new Huizong(csvTemp.getReportDate()));
            huizong.cal(csvTemp);
            huizongHashMap.put(csvTemp.getReportDate(), huizong);
            if (!excelWriterHashMap.containsKey(reportDate)) {
                ExcelWriter excelWriter = EasyExcel.write(ReadTest.class.getResource("/").getPath() + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + reportDate + ".xlsx", CsvTemp.class).build();
                excelWriterHashMap.put(reportDate, excelWriter);
            }
            List<CsvTemp> list;
            if (csvTempHashMap.containsKey(reportDate)) {
                list = csvTempHashMap.get(reportDate);
            } else {
                list = new ArrayList<CsvTemp>();
            }
            list.add(csvTemp);
            csvTempHashMap.put(reportDate, list);

            // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
            if (list.size() >= BATCH_COUNT) {
                saveData(reportDate);
                // 存储完成清理 list
                csvTempHashMap.remove(reportDate);
            }

        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        Iterator iterator = huizongHashMap.keySet().iterator();
        while (iterator.hasNext()) {
            String reportTime = (String) iterator.next();
            ExcelWriter excelWriter = excelWriterHashMap.get(reportTime);
            Huizong huizong = huizongHashMap.get(reportTime);
            WriteTable writeTable = EasyExcel.writerTable(0).needHead(Boolean.TRUE).head(Huizong.class).build();
            excelWriter.write(data(huizong), writeSheet, writeTable);
        }

        LOGGER.info("所有数据解析完成！");
        // 千万别忘记finish 会帮忙关闭流
        Iterator excelWriterIterator = excelWriterHashMap.keySet().iterator();
        while (excelWriterIterator.hasNext()) {
            String reportTime = (String) excelWriterIterator.next();
            ExcelWriter excelWriter = excelWriterHashMap.get(reportTime);
            excelWriter.finish();
        }
    }

    private List<Huizong> data(Huizong huizong) {
        List<Huizong> list = new ArrayList<Huizong>();
        list.add(huizong);
        return list;
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        Iterator iterator = csvTempHashMap.keySet().iterator();
        while (iterator.hasNext()) {
            String reportTime = (String) iterator.next();
            ExcelWriter excelWriter = excelWriterHashMap.get(reportTime);
            List<CsvTemp> csvTempList = csvTempHashMap.get(reportTime);
            demoDAO.save(csvTempList, excelWriter, writeSheet);
        }
    }

    private void saveData(String reportDate) {
        ExcelWriter excelWriter = excelWriterHashMap.get(reportDate);
        List<CsvTemp> csvTempList = csvTempHashMap.get(reportDate);
        demoDAO.save(csvTempList, excelWriter, writeSheet);
    }
}
