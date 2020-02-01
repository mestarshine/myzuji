package com.myzuji.zujibackend.controller;

import com.alibaba.druid.support.http.StatViewServlet;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/01
 */
@WebServlet(urlPatterns = "/druid/*",
    initParams = {
        @WebInitParam(name = "loginUsername", value = "druid"),
        @WebInitParam(name = "loginPassword", value = "druid"),
        @WebInitParam(name = "restEnable", value = "false")
    })
public class DruidStatViewServlet extends StatViewServlet {
}
