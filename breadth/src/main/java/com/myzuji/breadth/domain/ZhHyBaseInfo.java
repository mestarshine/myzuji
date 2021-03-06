package com.myzuji.breadth.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 行业信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "zh_hy_base_info")
public class ZhHyBaseInfo {

    /**
     * 行业代码
     */
    @Id
    private String csrcCode;

    /**
     * 行业代码
     */
    private String csrcName;

    /**
     * 该行业下的总数
     */
    private Integer stockNumber;

}
