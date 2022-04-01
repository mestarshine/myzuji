package com.myzuji.breadth.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 说明
 *
 * @author shine
 * @date 2021/12/25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "zh_stocks_info")
public class ZhStocksInfo {

    @Id
    private String stockCode;
    private String stockName;
    /**
     * 所属行业
     */
    private String industry;
    /**
     * 所属地域
     */
    private String area;
    /**
     * 发行价
     */
    private BigDecimal firstPrice;
    /**
     * 上市时间
     */
    private LocalDate listDate;
    private BigDecimal pe;
}
