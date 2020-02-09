package com.myzuji.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 说明
 *
 * @author shine
 * @date 2020/02/09
 */
@Controller
public class LoginController {

    @RequestMapping(value = "login.html")
    public String login(Model model){
        model.addAttribute("message","login");
        return "login";
    }
}
