package com.myzuji.study.java.easyexcel.write;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.util.DateUtils;
import com.myzuji.study.java.easyexcel.read.DemoData0;
import lombok.Builder;
import lombok.Data;

import static com.myzuji.study.java.easyexcel.read.DemoData2Listener.merList;

/**
 * 简单描述
 */
@Data
@Builder
public class CsvTemp {
    @ExcelIgnore
    private static String splitFlay = "`";
    @ExcelProperty("订单号")
    private String orderNo;
    @ExcelProperty("交易时间")
    private String tradeDate;
    @ExcelProperty("商户名称")
    private String merName;
    @ExcelProperty("交易状态")
    private String tradeStatus;
    @ExcelProperty("订单金额")
    private String tradeAmount;
    @ExcelProperty("订单结算金额")
    private String tradeSettleAmount;
    @ExcelProperty("用户实际支付金额")
    private String paymentAmount;
    @ExcelProperty("退款金额")
    private String refundAmount;
    @ExcelProperty("退款结算金额")
    private String refundSettleAmount;
    @ExcelIgnore
    @ExcelProperty("报表日期")
    private String reportDate;

    public static CsvTemp makeByDemoData(DemoData0 data) {
        if (merList.containsKey(data.getUnionMerNo())) {
            CsvTemp csvTemp = new CsvTempBuilder()
                .orderNo(splitFlay + "" + data.getOrderNo())
                .tradeDate(splitFlay + "" + DateUtils.format(data.getTradeTime(), "yyyyMMddHHmmss"))
                .merName(splitFlay + "" + data.getMerName())
                .tradeStatus("`SUCCESS")
                .tradeAmount(splitFlay + "" + Math.round(data.getTradeAmount() * 100D))
                .tradeSettleAmount(splitFlay + "" + String.format("%.0f", (data.getTradeAmount() * 100D) - (Math.round(data.getTradeAmount() * 0.02))))
                .paymentAmount(splitFlay + "" + String.format("%.0f", data.getTradeAmount() * 100D))
                .refundAmount("`0")
                .refundSettleAmount("`0")
                .reportDate(DateUtils.format(data.getTradeTime(), "yyyyMMdd"))
                .build();
            return csvTemp;
        }
        return null;
    }
}
