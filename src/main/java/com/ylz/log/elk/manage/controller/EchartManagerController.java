package com.ylz.log.elk.manage.controller;

import com.ylz.log.elk.manage.bean.VisualizeChartBean;
import com.ylz.log.elk.manage.bean.VisualizePanelEchartBean;
import com.ylz.log.elk.manage.constants.Constants;
import com.ylz.log.elk.manage.service.EchartService;
import com.ylz.log.elk.manage.service.IndexService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
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

        map.put("formateMap", Constants.formateMap);
        map.put("timeFieldMap", Constants.timeFieldMap);

        return "elk/data_view";
    }

    @RequestMapping("/loadMultiIndex")
    @ResponseBody
    public Map<String, Object> loadMultiIndex() {
        Map<String, Object> jsonMap = new HashMap<>();

        try {
            jsonMap.put("multiIndexList", indexService.listMultiIndex());
            jsonMap.put("flag", true);
        } catch (Exception e) {
            log.error("exception", e);
            jsonMap.put("flag", false);
        }

        return jsonMap;
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
    public Map<String, Object> listConverField(@RequestParam("index") String index) {
        Map<String, Object> jsonMap = new HashMap<>();

        try {
            jsonMap.put("fieldList", echartService.listConverField(index));
            jsonMap.put("flag", true);
        } catch (Exception e) {
            e.printStackTrace();

            jsonMap.put("flag", false);
        }

        return jsonMap;
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

            jsonMap.put("flag", flag);
        }

        return jsonMap;
    }


    @RequestMapping("/generatEchart")
    @ResponseBody
    public Map<String, Object> generatEchart(VisualizeChartBean visualizeChartBean) {

        return echartService.generatEchart(visualizeChartBean);
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

            jsonMap.put("flag", flag);
        }

        return jsonMap;
    }

    @RequestMapping(value = "/editVisualizeEchart/{id}")
    @ResponseBody
    public Map<String, Object> editVisualizeEchart(@PathVariable("id") Integer id) {
        Map<String, Object> jsonMap = new HashMap<>();

        VisualizeChartBean visualizeChartBean = null;
        try {
            visualizeChartBean = echartService.getVisualizeEchart(id);
            Map<String, Object> echartMap = echartService.generatEchart(visualizeChartBean);

            jsonMap.put("flag", true);
            jsonMap.put("visualizeChartBean", visualizeChartBean);
            jsonMap.put("echartMap", echartMap);
        } catch (Exception e) {
            e.printStackTrace();
            jsonMap.put("flag", false);
            if (StringUtils.isNotEmpty(e.getMessage()) && e.getMessage().contains("no such index")) {
                jsonMap.put("errorMsg", visualizeChartBean.getEchartName() + "有不存在的文件");
            } else {
                jsonMap.put("errorMsg", e.getMessage() + "");
            }
        }

        return jsonMap;
    }

    @RequestMapping(value = "/modifyVisualizeEchart/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public Map<String, Object> modifyVisualizeEchart(VisualizeChartBean visualizeChartBean) {
        Map<String, Object> jsonMap = new HashMap<>();

        boolean flag = false;
        try {
            flag = echartService.modifyVisualizeEchart(visualizeChartBean);

            jsonMap.put("flag", flag);
        } catch (Exception e) {
            e.printStackTrace();

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

    /**
     * 添加面板页面，查询图表方法
     *
     * @param pn
     * @param pageSize
     * @param echartName
     * @return
     */
    @RequestMapping("/pageSelectEchart")
    @ResponseBody
    public Map<String, Object> pageSelectEchart(
            @RequestParam("pn") Integer pn,
            @RequestParam(value = "pageSize", defaultValue = "4") Integer pageSize,
            @RequestParam(value = "echartName", defaultValue = "") String echartName
    ) {
        return echartService.pageSelectEchart(pn, pageSize, echartName);

    }

    @RequestMapping("/saveVisualizePanelEchart")
    @ResponseBody
    public Map<String, Object> saveVisualizePanelEchart(
            VisualizePanelEchartBean visualizePanelEchartBean,
            @RequestParam("echartId") String echartId
    ) {
        Map<String, Object> jsonMap = new HashMap<>();

        boolean flag = false;
        try {
            flag = echartService.saveVisualizePanelEchart(visualizePanelEchartBean,
                    Arrays.asList(echartId.split(",")));

            jsonMap.put("flag", flag);
        } catch (Exception e) {
            e.printStackTrace();

            jsonMap.put("flag", flag);
        }

        return jsonMap;
    }

    @RequestMapping("/delVisualizePanelEchart")
    @ResponseBody
    public Map<String, Object> delVisualizePanelEchart(@RequestParam("id") String id) {
        Map<String, Object> jsonMap = new HashMap<>();

        boolean flag = false;
        try {
            flag = echartService.delVisualizePanelEchart(Arrays.asList(id.split(",")));

            jsonMap.put("flag", flag);
        } catch (Exception e) {
            e.printStackTrace();

            jsonMap.put("flag", flag);
        }

        return jsonMap;
    }

    @RequestMapping("/editVisualizePanelEchart/{id}")
    @ResponseBody
    public Map<String, Object> editVisualizePanelEchart(@PathVariable("id") Integer id) {

        return echartService.editVisualizePanelEchart(id);
    }

    @RequestMapping("/modifyVisualizePanelEchart")
    @ResponseBody
    public Map<String, Object> modifyVisualizePanelEchart(
            VisualizePanelEchartBean visualizePanelEchartBean,
            @RequestParam("echartId") String echartId
    ) {
        Map<String, Object> jsonMap = new HashMap<>();

        boolean flag = false;
        try {
            flag = echartService.modifyVisualizePanelEchart(visualizePanelEchartBean,
                    Arrays.asList(echartId.split(",")));

            jsonMap.put("flag", flag);
        } catch (Exception e) {
            e.printStackTrace();

            jsonMap.put("flag", flag);
        }

        return jsonMap;
    }

    @RequestMapping("/hasExistEchartName")
    @ResponseBody
    public Map<String, Object> hasExistEchartName(@RequestParam("echartName") String echartName) {
        return echartService.hasExistEchartName(echartName);
    }

    @RequestMapping("/hasExistPanelName")
    @ResponseBody
    public Map<String, Object> hasExistPanelName(@RequestParam("panelName") String panelName) {
        return echartService.hasExistPanelName(panelName);
    }

}
