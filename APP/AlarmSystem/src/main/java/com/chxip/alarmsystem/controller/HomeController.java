package com.chxip.alarmsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping(value = "/")
    public String toHome(){
        return "index";
    }

    @GetMapping(value = "/toWelcome")
    public String toWelcome(){
        return "welcome";
    }

    @GetMapping(value = "/login")
    public String login(){
        return "login";
    }
}
