package com.ylz.log.elk.manage.service;

import com.ylz.log.elk.manage.bean.VisualizeChartBean;

import java.util.List;
import java.util.Map;

public interface EchartService {
    Map<String, Object> pageVisualizeEchart(Integer pn, Integer pageSize, String echartName);

    boolean saveVisualizeEchart(VisualizeChartBean visualizeChartBean);

    Map<String,Object> generatEchart(String index, String field, String converMethod);

    Map<String, List<String>> listConverField(String index);
}
