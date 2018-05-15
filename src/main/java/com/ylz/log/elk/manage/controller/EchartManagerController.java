package com.ylz.log.elk.manage.controller;

import com.ylz.log.elk.manage.bean.MultiIndexBean;
import com.ylz.log.elk.manage.bean.VisualizeChartBean;
import com.ylz.log.elk.manage.service.EchartService;
import com.ylz.log.elk.manage.service.IndexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@Controller
@RequestMapping("/echartManage")
public class EchartManagerController {

    @Autowired
    private EchartService echartService;

    @Autowired
    private IndexService indexService;

    @RequestMapping("/dataView")
    public String dataView(Map<String, Object> map) {

        List<MultiIndexBean> multiIndexList = indexService.listMultiIndex();

        map.put("multiIndexList", multiIndexList);

        return "elk/data_view";
    }

    @RequestMapping("/pageVisualizeEchart")
    @ResponseBody
    public Map<String, Object> pageVisualizeEchart(
            @RequestParam(value = "pn", defaultValue = "1") Integer pn,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "echartName", defaultValue = "") String echartName,
            @RequestParam(value = "sortName", defaultValue = "") String sortName,
            @RequestParam(value = "sortOrder", defaultValue = "") String sortOrder

    ) {

        return echartService.pageVisualizeEchart(pn, pageSize, echartName, sortName, sortOrder);
    }

    /**
     * 获取可以汇聚的列
     *
     * @param index
     * @return
     */
    @RequestMapping("/listConverField")
    @ResponseBody
    public Map<String, List<String>> listConverField(@RequestParam("index") String index) {
        return echartService.listConverField(index);
    }

    @RequestMapping(value = "/saveVisualizeEchart", method = RequestMethod.POST)
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

    /**
     * @param relIndex 索引
     * @param field    列名
     * @param lastDay  默认查询最近lastDay
     * @return
     */
    @RequestMapping("/generatEchart")
    @ResponseBody
    public Map<String, Object> generatEchart(
            @RequestParam("relIndex") String relIndex,
            @RequestParam("field") String field,
            @RequestParam(value = "lastDay", defaultValue = "") Integer lastDay
    ) {

        return echartService.generatEchart(relIndex, field, lastDay);
    }

    @RequestMapping(value = "/delVisualizeEchart", method = RequestMethod.DELETE)
    @ResponseBody
    public Map<String, Object> delVisualizeEchart(@RequestParam("id") String id) {
        Map<String, Object> jsonMap = new HashMap<>();

        boolean flag = false;
        try {
            flag = echartService.delVisualizeEchart(Arrays.asList(id.split(",")));

            jsonMap.put("flag", flag);
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;

            jsonMap.put("flag", flag);
        }

        return jsonMap;
    }

    @RequestMapping(value = "/detailVisualizeEchart/{id}")
    @ResponseBody
    public Map<String, Object> detailVisualizeEchart(@PathVariable("id") Integer id) {
        Map<String, Object> jsonMap = new HashMap<>();

        VisualizeChartBean visualizeChartBean = echartService.getVisualizeEchart(id);
        Map<String, Object> echartMap = echartService.generatEchart(visualizeChartBean.getRelIndex(),
                visualizeChartBean.getField(), visualizeChartBean.getLastDay());

        jsonMap.put("visualizeChartBean", visualizeChartBean);
        jsonMap.put("echartMap", echartMap);

        return jsonMap;
    }

    @RequestMapping(value = "/editVisualizeEchart/{id}")
    @ResponseBody
    public Map<String, Object> editVisualizeEchart(@PathVariable("id") Integer id) {
        Map<String, Object> jsonMap = new HashMap<>();

        VisualizeChartBean visualizeChartBean = echartService.getVisualizeEchart(id);
        Map<String, Object> echartMap = echartService.generatEchart(visualizeChartBean.getRelIndex(),
                visualizeChartBean.getField(), visualizeChartBean.getLastDay());

        jsonMap.put("visualizeChartBean", visualizeChartBean);
        jsonMap.put("echartMap", echartMap);

        return jsonMap;
    }

    @RequestMapping(value = "/modifyVisualizeEchart/{id}", method =  RequestMethod.PUT)
    @ResponseBody
    public Map<String, Object> modifyVisualizeEchart(VisualizeChartBean visualizeChartBean) {
        Map<String, Object> jsonMap = new HashMap<>();

        boolean flag = false;
        try {
            flag = echartService.modifyVisualizeEchart(visualizeChartBean);

            jsonMap.put("flag", flag);
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;

            jsonMap.put("flag", flag);
        }

        return jsonMap;
    }

    @RequestMapping("/dataShowPanel")
    public String dataShowPanel() {

        return "elk/data_show_panel";
    }

    @RequestMapping("/pageVisualizePanelEchart")
    @ResponseBody
    public Map<String, Object> pageVisualizePanelEchart(
            @RequestParam(value = "pn", defaultValue = "1") Integer pn,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "panelName", defaultValue = "") String panelName,
            @RequestParam(value = "sortName", defaultValue = "") String sortName,
            @RequestParam(value = "sortOrder", defaultValue = "") String sortOrder

    ) {

        return echartService.pageVisualizePanelEchart(pn, pageSize, panelName, sortName, sortOrder);
    }

    @RequestMapping("/pageSelectEchart")
    @ResponseBody
    public Map<String, Object> pageSelectEchart(
            @RequestParam("pn") Integer pn,
            @RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize,
            @RequestParam(value = "echartName", defaultValue = "") String echartName
    ) {

        return echartService.pageSelectEchart(pn, pageSize, echartName);

    }
}
