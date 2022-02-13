package cn.ovoll.learn.rabbitmq.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class ConsumerController {

    public String test(Model model){
        model.addAttribute("userId",1);
        return "index";
    }
}
