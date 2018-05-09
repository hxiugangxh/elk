package com.ylz.log.elk.monitor.controller;

import com.ylz.log.elk.base.util.EsUtil;
import com.ylz.log.elk.monitor.bean.MutilIndexBean;
import com.ylz.log.elk.monitor.service.MonitorService;
import org.apache.shiro.subject.support.SubjectThreadState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.management.GarbageCollectorMXBean;
import java.util.*;

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
    public List<String> listField(
            @RequestParam("index") String index,
            @RequestParam(value = "flag", defaultValue = "") String flag
    ) {
        return monitorService.listField(index, flag);
    }

    @RequestMapping("/dataManage")
    public String dataManage(Map<String, Object> map) {

        List<MutilIndexBean> multiIndexList = monitorService.listMultiIndex();
        List<String> indexList = monitorService.listIndex();

        map.put("multiIndexList", multiIndexList);
        map.put("indexList", indexList);

        return "logmonitor/data_manage";
    }

    @RequestMapping("/listReflectField")
    @ResponseBody
    public List<Map<String, Object>> listReflectField(
            @RequestParam(value = "index", defaultValue = "") String index,
            @RequestParam(value = "flag", defaultValue = "0") String flag
    ) {

        return monitorService.listReflectField(index, flag);
    }

    @RequestMapping(value = "/saveMultiIndex")
    @ResponseBody
    public Map<String, Object> saveMultiIndex(
            @RequestParam("multiIndex") String multiIndex,
            @RequestParam("index") String index

    ) {
        Map<String, Object> jsonMap = new HashMap<>();

        System.out.println("multiIndex = " + multiIndex);
        System.out.println("index = " + index);

        boolean flag = monitorService.saveMultiIndex(multiIndex, Arrays.asList(index.split(",")));

        jsonMap.put("flag", flag);

        return jsonMap;
    }

    @RequestMapping("/test")
    @ResponseBody
    public Object test() {

        return monitorService.test();
    }
}
