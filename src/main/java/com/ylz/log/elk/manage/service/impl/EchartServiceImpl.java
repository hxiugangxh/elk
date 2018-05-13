package com.ylz.log.elk.manage.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.ylz.log.elk.manage.bean.VisualizeChartBean;
import com.ylz.log.elk.manage.dao.mapper.EchartMapper;
import com.ylz.log.elk.manage.service.EchartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("echartService")
public class EchartServiceImpl implements EchartService {

    @Autowired
    private EchartMapper echartMapper;

    @Override
    public Map<String, Object> pageVisualizeEchart(Integer pn, Integer pageSize, String echartName) {
        PageHelper.startPage(pn, pageSize);
        List<VisualizeChartBean> list = echartMapper.pageVisualizeEchart(echartName);

        PageInfo pageInfo = new PageInfo(list);

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("total", pageInfo.getTotal());
        dataMap.put("rows", pageInfo.getList());

        return dataMap;
    }
}
