package com.myzuji.backend.domain.base;

import java.lang.annotation.*;

/**
 * 最后更新时间域注解，在数据入库时将自动取最新时间
 *
 * @author shine
 * @date 2020/02/22
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface UpdateTime {
}
