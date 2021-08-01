package com.myzuji.study.easyexcel.read;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DemoData3 {

    @ExcelProperty("REFUNDNO")
    private String refundOrderNo;

}
