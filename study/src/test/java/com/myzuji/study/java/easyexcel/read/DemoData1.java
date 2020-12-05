package com.myzuji.study.java.easyexcel.read;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class DemoData1 {

    @ExcelProperty("商户订单号")
    private String merchantOrderNo;
}
