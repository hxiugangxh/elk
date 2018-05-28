package com.ylz.log.elk.controller;

import com.ylz.log.elk.manage.dao.mapper.EchartMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Controller
public class TestController {

    @Autowired
    private EchartMapper echartMapper;

    @RequestMapping("/test")
    @ResponseBody
    public Map<String, Object> index() {

        String name = "张三";
        Integer age = 12;
        Integer idCard = 22222;
        echartMapper.test(name, age, idCard);

        return new HashMap<>();
    }

}
