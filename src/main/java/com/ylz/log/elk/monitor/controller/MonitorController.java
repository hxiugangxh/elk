package com.ylz.log.elk.monitor.controller;

import com.ylz.log.elk.monitor.service.MonitorService;
import org.apache.shiro.subject.support.SubjectThreadState;
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
@RequestMapping("/monitor")
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
    public Map<String, Object> queryByEs(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "index") String index,
            @RequestParam(value = "field", defaultValue = "") String field,
            @RequestParam(value = "searchContent", defaultValue = "") String searchContent
    ) {

        return monitorService.queryByEs(page, pageSize, index, field, searchContent);
    }

    @RequestMapping("/changeIndex")
    @ResponseBody
    public List<String> changeIndex(@RequestParam("index") String index) {
        return monitorService.changeIndex(index);
    }

}
