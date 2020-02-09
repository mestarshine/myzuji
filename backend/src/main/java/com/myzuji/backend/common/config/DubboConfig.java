package com.myzuji.backend.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/02
 */
@Configuration
@PropertySource("classpath:dubbo.properties")
@ImportResource("classpath:META-INF/dubbo-consumer.xml")
public class DubboConfig {
}
