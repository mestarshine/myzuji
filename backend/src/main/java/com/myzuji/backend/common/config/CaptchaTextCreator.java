package com.myzuji.backend.common.config;

import com.google.code.kaptcha.text.impl.DefaultTextCreator;

import java.util.Random;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/24
 */
public class CaptchaTextCreator extends DefaultTextCreator {
    private static final String[] KC_NUMBERS = "0,1,2,3,4,5,6,7,8,9".split(",");

    @Override
    public String getText() {
        Integer result = 0;
        Random random = new Random();
        int x = random.nextInt(10);
        int y = random.nextInt(10);
        StringBuilder suChinese = new StringBuilder();
        int randomOperands = (int) Math.round(Math.random() * 2);
        if (randomOperands == 0) {
            result = x * y;
            suChinese.append(KC_NUMBERS[x]);
            suChinese.append("*");
            suChinese.append(KC_NUMBERS[y]);
        } else if (randomOperands == 1) {
            if (!(x == 0) && y % x == 0) {
                result = y / x;
                suChinese.append(KC_NUMBERS[y]);
                suChinese.append("÷");
                suChinese.append(KC_NUMBERS[x]);
            } else {
                result = x + y;
                suChinese.append(KC_NUMBERS[x]);
                suChinese.append("+");
                suChinese.append(KC_NUMBERS[y]);
            }
        } else if (randomOperands == 2) {
            if (x >= y) {
                result = x - y;
                suChinese.append(KC_NUMBERS[x]);
                suChinese.append("-");
                suChinese.append(KC_NUMBERS[y]);
            } else {
                result = y - x;
                suChinese.append(KC_NUMBERS[y]);
                suChinese.append("-");
                suChinese.append(KC_NUMBERS[x]);
            }
        } else {
            result = x + y;
            suChinese.append(KC_NUMBERS[x]);
            suChinese.append("+");
            suChinese.append(KC_NUMBERS[y]);
        }
        suChinese.append("=?@" + result);
        return suChinese.toString();
    }
}
