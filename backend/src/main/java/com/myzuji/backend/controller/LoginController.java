package com.myzuji.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/09
 */
@Controller
public class LoginController {

    @RequestMapping(value = "/")
    public ModelAndView login(ModelAndView mv) {
        mv.setViewName("login");
        mv.addObject("message", "input login message");
        return mv;
    }

    @RequestMapping(value = "/login.html")
    public ModelAndView toLogin(ModelAndView mv) {
        mv.setViewName("index");
        return mv;
    }

}
