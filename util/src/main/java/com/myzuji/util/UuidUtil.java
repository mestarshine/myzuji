package com.myzuji.util;

import java.util.UUID;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/10
 */
public class UuidUtil {

    public static String getUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
