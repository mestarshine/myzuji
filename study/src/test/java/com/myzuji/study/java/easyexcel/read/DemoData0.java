package com.myzuji.study.java.easyexcel.read;

import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

/**
 * 基础数据类.这里的排序和excel里面的排序一致
 **/
@Data
public class DemoData0 {
    private String orderNo;
    @DateTimeFormat("yyyyMMddHHmmss")
    private Date tradeTime;
    private String unionMerNo;
    private String termId;
    private String merName;
    private String unionTradeNo;
    private Double tradeAmount;
}
