package com.ylz.log.elk.manage.controller;

import com.ylz.log.elk.base.util.LoginInfoUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class IndexController {

    @RequestMapping("/index")
    public String index(Map<String, Object> map) {

        map.put("userName", LoginInfoUtil.getUserName());

        return "index";
    }

}
