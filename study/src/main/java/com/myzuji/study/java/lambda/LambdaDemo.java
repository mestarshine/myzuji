package com.myzuji.study.java.lambda;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/04
 */
public class LambdaDemo {

    public static void main(String[] args) {
        MyNumber myNumber;
        myNumber = () -> 123.34;
        System.out.println("一个固定值：" + myNumber.getValue());

        myNumber = () -> Math.random() * 100;
        System.out.println("一个随机值：" + myNumber.getValue());
        System.out.println("另一个随机值：" + myNumber.getValue());

        NumericTest isEven = (int n) -> (n % 2) == 0;

        if (isEven.test(10)) {
            System.out.println("10是偶数");
        }
        if (!isEven.test(9)) {
            System.out.println("9不是偶数");
        }

        NumericTest isNonNeg = (n) -> n >= 0;
        if (isNonNeg.test(1)) {
            System.out.println("1不是负数");
        }
        if (!isNonNeg.test(-1)) {
            System.out.println("-1是负数");
        }

        String a = "1001100100001";
        System.out.println(a.contains("10001"));
        System.out.println(a.substring("1001".length(), a.length()));
        String b = "100";
        String c = "1";
        System.out.println(b + c);
        Test test = new Test();
        System.out.println(JSON.toJSONString(test));

    }

    public static class Test {
        private String orderNo;
        private String merNo;
        private List<ShareProfitInfoDto> shareProfitInfoDtoList;

        public Test() {
            this.merNo = "1000";
            this.orderNo = "10099";
            this.shareProfitInfoDtoList = make();
        }

        private List<ShareProfitInfoDto> make() {
            List<ShareProfitInfoDto> shareProfitInfoDtos = new ArrayList<>();
            shareProfitInfoDtos.add(new ShareProfitInfoDto("1000100", 100, "fz"));
            shareProfitInfoDtos.add(new ShareProfitInfoDto("1000101", 100, "fz"));
            return shareProfitInfoDtos;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getMerNo() {
            return merNo;
        }

        public void setMerNo(String merNo) {
            this.merNo = merNo;
        }

        public List<ShareProfitInfoDto> getShareProfitInfoDtoList() {
            return shareProfitInfoDtoList;
        }

        public void setShareProfitInfoDtoList(List<ShareProfitInfoDto> shareProfitInfoDtoList) {
            this.shareProfitInfoDtoList = shareProfitInfoDtoList;
        }
    }

    private static class ShareProfitInfoDto {
        private String payeeMerNo; //收款人商户号

        private int amount; //分账金额

        private String remark; //备注

        public ShareProfitInfoDto(String payeeMerNo, int amount, String remark) {
            this.payeeMerNo = payeeMerNo;
            this.amount = amount;
            this.remark = remark;
        }

        public String getPayeeMerNo() {
            return payeeMerNo;
        }

        public void setPayeeMerNo(String payeeMerNo) {
            this.payeeMerNo = payeeMerNo;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }
}
