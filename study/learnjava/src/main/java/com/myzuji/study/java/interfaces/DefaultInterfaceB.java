package com.myzuji.study.java.interfaces;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/01
 */
public interface DefaultInterfaceB {

    default String obtainInterfaceName() {
        return this.getClass().getName();
    }
}
