package com.myzuji.study.spi;

import sun.misc.Service;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * 说明
 *
 * @author shine
 * @date 2020/03/03
 */
public class SPITest {

    public static void main(String[] args) {
        Iterator<SPIService> providers = Service.providers(SPIService.class);
        while (providers.hasNext()) {
            SPIService spiService = providers.next();
            spiService.execute();
        }
        System.out.println("============");
        ServiceLoader<SPIService> loader = ServiceLoader.load(SPIService.class);
        Iterator<SPIService> spiServiceIterator = loader.iterator();
        while (spiServiceIterator.hasNext()) {
            SPIService spiService = spiServiceIterator.next();
            spiService.execute();
        }
    }
}
