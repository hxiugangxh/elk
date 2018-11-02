package com.ylz.log.elk.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    @RequestMapping(value = {"/", "/index"})
    public String index() {
        return "/index";
    }

}
