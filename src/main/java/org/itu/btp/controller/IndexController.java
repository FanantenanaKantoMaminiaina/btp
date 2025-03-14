package org.itu.btp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String pageIndex(){
        return "index";
    }

    @GetMapping("/access-denied")
    public String pageAccesInterdit(){
        return "access-denied";
    }
}
