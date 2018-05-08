package com.ylz.log.elk.monitor.controller;

import com.ylz.log.elk.base.util.EsUtil;
import com.ylz.log.elk.monitor.bean.MutilIndexBean;
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
@RequestMapping("/logmonitor")
public class MonitorController {

    @Autowired
    private MonitorService monitorService;

    @RequestMapping("/index")
    public String index(Map<String, Object> map) {

        Map<String, Object> esFieldmap = monitorService.esFieldMap();

        map.put("esFieldmap", esFieldmap);

        return "logmonitor/index";
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

    @RequestMapping("/listField")
    @ResponseBody
    public List<String> listField(@RequestParam("index") String index) {
        return monitorService.listField(index);
    }

    @RequestMapping("/dataManage")
    public String dataManage(Map<String, Object> map) {

        List<MutilIndexBean> multiIndexList = monitorService.listMultiIndex();
        List<String> indexList = monitorService.listIndex();

        map.put("multiIndexList", multiIndexList);
        map.put("indexList", indexList);

        return "logmonitor/data_manage";
    }

    @RequestMapping("/test")
    @ResponseBody
    public Object test() {

        return monitorService.test();
    }
}
