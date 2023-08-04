package com.funix.prj_321x.asm03.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @RequestMapping("/")
    public String showSwagger() {
        return  "redirect:/swagger-ui.html";
    }
}
