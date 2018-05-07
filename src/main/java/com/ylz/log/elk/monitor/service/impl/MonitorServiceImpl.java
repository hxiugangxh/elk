package com.ylz.log.elk.monitor.service.impl;

import com.ylz.log.elk.monitor.dao.MonitorDao;
import com.ylz.log.elk.monitor.service.MonitorService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.expression.Lists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("monitorService")
public class MonitorServiceImpl implements MonitorService {

    @Autowired
    private MonitorDao monitorDao;

    @Override
    public Map<String, Object> esFieldMap() {
        Map<String, Object> esFieldMap = new HashMap<>();

        List<String> indexList = monitorDao.indexList();
        List<String> fieldList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(indexList)) {
            String index = "";
            index = indexList.get(0);
            fieldList = monitorDao.fieldList(index);
        }

        esFieldMap.put("indexList", indexList);
        esFieldMap.put("fieldList", fieldList);

        return esFieldMap;
    }

    @Override
    public List<Map<String, Object>> queryByEs(String index) {

        return monitorDao.queryByEs(index);
    }
}
