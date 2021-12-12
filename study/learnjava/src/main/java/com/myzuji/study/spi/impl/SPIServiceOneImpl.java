package com.myzuji.study.spi.impl;

import com.myzuji.study.spi.SPIService;

/**
 * 说明
 *
 * @author shine
 * @date 2020/03/03
 */
public class SPIServiceOneImpl implements SPIService {

    @Override
    public void execute() {
        System.out.println("SPIOneImpl.excute()");
    }
}
