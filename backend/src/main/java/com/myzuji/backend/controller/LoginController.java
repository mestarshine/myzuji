package com.myzuji.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/09
 */
@Controller
public class LoginController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public RedirectView login() {
        return new RedirectView("/login.html");
    }

    @RequestMapping(value = "/login.html", method = RequestMethod.GET)
    public ModelAndView toLogin(ModelAndView mv) {
        mv.setViewName("login");
        return mv;
    }

    @RequestMapping(value = "/index.html", method = RequestMethod.GET)
    public ModelAndView index(ModelAndView mv) {
        mv.setViewName("index");
        return mv;
    }
}
