package com.ylz.log.elk.monitor.controller;

import com.ylz.log.elk.monitor.bean.MutilIndexBean;
import com.ylz.log.elk.monitor.service.MonitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
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
        log.info("contorller: dataManage");

        List<MutilIndexBean> multiIndexList = monitorService.listMultiIndex();
        List<String> indexList = monitorService.listIndex();

        map.put("multiIndexList", multiIndexList);
        map.put("indexList", indexList);

        return "logmonitor/data_manage";
    }

    /**
     * 列举所有的索引与对应的字段 Map index field
     *
     * @param index
     * @param flag  判断是否是组合索引 1 是 0 否
     * @return
     */
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

        boolean flag = false;
        try {
            flag = monitorService.saveMultiIndex(multiIndex, Arrays.asList(index.split(",")));


            // update 返回的count为0
            if (!flag) {
                jsonMap.put("errorMsg", "保存成功返回值为0");
            }
            jsonMap.put("flag", flag);
        } catch (Exception e) {
            e.printStackTrace();

            jsonMap.put("flag", flag);
            jsonMap.put("errorMsg", e.getMessage());
        }

        return jsonMap;
    }

    @RequestMapping("/hasExist")
    @ResponseBody
    public Map<String, Object> hasExist(@RequestParam("multiIndex") String multiIndex) {
        Map<String, Object> jsonMap = monitorService.hasExist(multiIndex);

        return jsonMap;
    }

    /**
     * 判断数据加载错误是否是因为组合索引对应的es索引不存在
     * 是，整理成信息提示
     * 否，不吭声，系统异常
     *
     * @param index
     * @param flag  判断是否是组合索引 1 是 0 否
     * @return
     */
    @RequestMapping("/dealNotIndex")
    @ResponseBody
    public Map<String, Object> dealNotIndex(
            @RequestParam("index") String index,
            @RequestParam("flag") String flag
    ) {

        return monitorService.dealNotIndex(index, flag);
    }

    @RequestMapping("/delMultiRelIndex")
    @ResponseBody
    public Map<String, Object> delMultiRelIndex(@RequestParam("index") String index) {
        Map<String, Object> jsonMap = new HashMap<>();
        boolean flag = false;
        flag = monitorService.delMultiRelIndex(Arrays.asList(index.split(",")));

        jsonMap.put("flag", flag);

        return jsonMap;
    }

    @RequestMapping("/test")
    @ResponseBody
    public Object test() {

        return monitorService.test();
    }
}
