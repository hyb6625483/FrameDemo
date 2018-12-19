package com.rabbitmq.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ConsumerController {

    @RequestMapping("/Test")
    public String test(Model model){
        model.addAttribute("userId",1);
        return "index";
    }
}
