package com.ylz.log.elk.manage.controller;

import com.ylz.log.elk.base.util.LoginInfoUtil;
import com.ylz.log.elk.manage.bean.MultiIndexBean;
import com.ylz.log.elk.manage.bean.MutilIndexEnum;
import com.ylz.log.elk.manage.bean.UserCollIndexBean;
import com.ylz.log.elk.manage.bean.VisualizeChartBean;
import com.ylz.log.elk.manage.service.EchartService;
import com.ylz.log.elk.manage.service.MonitorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/echartManage")
public class EchartManagerController {

    @Autowired
    private EchartService echartService;

    @Autowired
    private MonitorService monitorService;

    @RequestMapping("/dataView")
    public String dataView(Map<String, Object> map) {

        List<MultiIndexBean> multiIndexList = monitorService.listMultiIndex();

        map.put("multiIndexList", multiIndexList);

        return "elk/data_view";
    }

    @RequestMapping("/pageVisualizeEchart")
    @ResponseBody
    public Map<String, Object> pageVisualizeEchart(
            @RequestParam(value = "pn") Integer pn,
            @RequestParam(value = "pageSize") Integer pageSize,
            @RequestParam(value = "echartName", defaultValue = "") String echartName
    ) {

        return echartService.pageVisualizeEchart(pn, pageSize, echartName);
    }

    @RequestMapping("/listField")
    @ResponseBody
    public List<String> listField(@RequestParam("index") String index) {
        return monitorService.listField(index, MutilIndexEnum.PROJECT_NUM.getCode());
    }

    @RequestMapping("/saveVisualizeEchart")
    @ResponseBody
    public Map<String, Object> saveVisualizeEchart(VisualizeChartBean visualizeChartBean) {
        Map<String, Object> jsonMap = new HashMap<>();

        boolean flag = false;
        try {
            flag = echartService.saveVisualizeEchart(visualizeChartBean);

            jsonMap.put("flag", flag);
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;

            jsonMap.put("flag", flag);
        }

        return jsonMap;
    }
}
