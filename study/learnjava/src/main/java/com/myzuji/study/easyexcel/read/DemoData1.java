package com.myzuji.study.easyexcel.read;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DemoData1 {

    @ExcelProperty("商户订单号")
    private String merchantOrderNo;
}
