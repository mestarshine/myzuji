package com.myzuji.backend.domain.base;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/22
 */
public enum DBLockMode {
    NONE, WRITE, READ_ONLY, WRITE_NOWAIT
}
