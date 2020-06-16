package com.myzuji.study.gof23.bridge;

import com.myzuji.study.gof23.bridge.impl.Vivo;
import com.myzuji.study.gof23.bridge.impl.Xiaomi;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/05
 */
public class Client {

    public static void main(String[] args) {
        Phone phone = new FoldedPhone(new Xiaomi());
        phone.open();
        phone.call();
        phone.call();

        Phone phone2 = new FoldedPhone(new Vivo());
        phone2.open();
        phone2.call();
        phone2.call();
    }
}
