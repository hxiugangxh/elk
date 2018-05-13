package com.ylz.log.elk.manage.service;

import com.github.pagehelper.PageInfo;

import java.util.Map;

public interface EchartService {
    Map<String, Object> pageVisualizeEchart(Integer pn, Integer pageSize, String echartName);
}
