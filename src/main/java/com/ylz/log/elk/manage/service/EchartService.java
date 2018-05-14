package com.ylz.log.elk.manage.service;

import com.github.pagehelper.PageInfo;
import com.ylz.log.elk.manage.bean.VisualizeChartBean;

import java.util.Map;

public interface EchartService {
    Map<String, Object> pageVisualizeEchart(Integer pn, Integer pageSize, String echartName);

    boolean saveVisualizeEchart(VisualizeChartBean visualizeChartBean);
}
