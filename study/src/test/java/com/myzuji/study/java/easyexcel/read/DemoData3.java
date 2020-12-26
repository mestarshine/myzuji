package com.myzuji.study.java.easyexcel.read;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class DemoData3 {

    @ExcelProperty("REFUNDNO")
    private String refundOrderNo;

}
