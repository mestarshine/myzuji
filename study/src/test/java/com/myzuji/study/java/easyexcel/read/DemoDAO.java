package com.myzuji.study.java.easyexcel.read;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.myzuji.study.java.easyexcel.write.CsvTemp;

import java.util.List;

/**
 * 假设这个是你的DAO存储。当然还要这个类让spring管理，当然你不用需要存储，也不需要这个类。
 **/
public class DemoDAO {

    public void save(List<CsvTemp> list, ExcelWriter excelWriter, WriteSheet writeSheet) {
        excelWriter.write(list, writeSheet);
    }
}
