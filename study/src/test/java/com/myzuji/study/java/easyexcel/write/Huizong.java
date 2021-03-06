package com.myzuji.study.java.easyexcel.write;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 简单描述
 */
@Data
public class Huizong {

    @ExcelIgnore
    private String reportTime;

    @ExcelProperty("订单总笔数")
    private String bsS;

    @ExcelProperty("支付订单总金额")
    private String totalAmountS;

    @ExcelProperty("支付订单应结算总金额")
    private String totalSettleAmountS;

    @ExcelProperty("退款单总金额")
    private String totalRefundAmountS;

    @ExcelProperty("退款单应结算总金额")
    private String totalRefundSettleAmountS;

    @ExcelIgnore
    private int bs = 0;

    @ExcelIgnore
    private int totalAmount = 0;

    @ExcelIgnore
    private int totalSettleAmount = 0;

    @ExcelIgnore
    private int totalRefundAmount = 0;

    @ExcelIgnore
    private int totalMerFeeAmount = 0;

    public Huizong() {
    }

    public Huizong(String reportTime) {
        this.reportTime = reportTime;
    }

    public void cal(CsvTemp csvTemp) {
        bs += 1;
        totalAmount += Integer.valueOf(csvTemp.getTradeAmount());
        totalSettleAmount += Integer.valueOf(csvTemp.getTradeSettleAmount());
        bsS = "`" + bs;
        totalAmountS = "`" + totalAmount;
        totalSettleAmountS = "`" + totalSettleAmount;
        totalRefundAmountS = "`" + totalRefundAmount;
    }

}
