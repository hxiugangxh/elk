package com.ylz.log.elk.manage.service;

import com.ylz.log.elk.manage.bean.VisualizeChartBean;
import com.ylz.log.elk.manage.bean.VisualizePanelEchartBean;

import java.util.List;
import java.util.Map;

public interface EchartService {
    Map<String, Object> pageVisualizeEchart(Integer pn, Integer pageSize, String echartName, String sortName, String
            sortOrder);

    boolean saveVisualizeEchart(VisualizeChartBean visualizeChartBean);

    Map<String, Object> generatEchart(String relIndex, String field, Integer lastDay);

    Map<String, List<String>> listConverField(String index);

    boolean delVisualizeEchart(List<String> idList);

    VisualizeChartBean getVisualizeEchart(Integer id);

    boolean modifyVisualizeEchart(VisualizeChartBean visualizeChartBean);

    Map<String, Object> pageVisualizePanelEchart(Integer pn, Integer pageSize, String panelName, String sortName,
                                                 String sortOrder);

    Map<String,Object> pageSelectEchart(Integer pn, Integer pageSize, String echartName);

    boolean saveVisualizePanelEchart(VisualizePanelEchartBean visualizePanelEchartBean, List<String> echartIdList);

    boolean delVisualizePanelEchart(Integer id);

    Map<String,Object> editVisualizePanelEchart(Integer id);

    boolean modifyVisualizePanelEchart(VisualizePanelEchartBean visualizePanelEchartBean, List<String> echartIdList);
}
