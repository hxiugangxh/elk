package com.ylz.log.elk.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/echartManage")
public class EchartManagerController {

    @RequestMapping("/dataView")
    public String dataView() {

        return "elk/data_view";
    }

    @RequestMapping("/test")
    @ResponseBody
    public List<Map<String, Object>> test() {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();

        for (int i = 0 ; i < 47; i++) {
            map = new HashMap<>();

            map.put("id", "1");
            map.put("name", "Item " + i);
            map.put("date", "<i class='iconfont icon-slice33'></i>");
            map.put("item", "item00" + i + "-表格");
            list.add(map);
        }

        return list;
    }
}
