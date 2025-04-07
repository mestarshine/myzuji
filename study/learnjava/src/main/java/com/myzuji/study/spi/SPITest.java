package com.myzuji.study.spi;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * 说明
 *
 * @author shine
 * @date 2020/03/03
 */
public class SPITest {

    @Test
    @Disabled
    void sPITest() {
        ServiceLoader<SPIService> loader = ServiceLoader.load(SPIService.class);
        Iterator<SPIService> spiServiceIterator = loader.iterator();
        while (spiServiceIterator.hasNext()) {
            SPIService spiService = spiServiceIterator.next();
            spiService.execute();
        }
    }
}
