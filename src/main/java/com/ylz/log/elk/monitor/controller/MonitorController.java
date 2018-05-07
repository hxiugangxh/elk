package com.ylz.log.elk.monitor.controller;

import com.ylz.log.elk.monitor.service.MonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.management.GarbageCollectorMXBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/monior")
public class MonitorController {

    @Autowired
    private MonitorService monitorService;

    @RequestMapping("/index")
    public String index(Map<String, Object> map) {

        Map<String, Object> esFieldmap = monitorService.esFieldMap();

        map.put("esFieldmap", esFieldmap);

        return "log/index";
    }

    @RequestMapping("/queryByEs")
    @ResponseBody
    public List<Map<String, Object>> queryByEs(
            @RequestParam(value = "index", required = false) String index) {
        List<Map<String, Object>> dataList = monitorService.queryByEs(index);

        return dataList;
    }

}
