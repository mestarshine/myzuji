package com.myzuji.backend.common.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/01
 */
@Configuration
@PropertySource("classpath:druid.properties")
public class DruidDbConfig extends DruidDataSourceAutoConfigure {

}

