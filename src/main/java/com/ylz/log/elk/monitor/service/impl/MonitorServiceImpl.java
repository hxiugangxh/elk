package com.ylz.log.elk.monitor.service.impl;

import com.ylz.log.elk.monitor.bean.MutilIndexBean;
import com.ylz.log.elk.monitor.dao.MonitorDao;
import com.ylz.log.elk.monitor.service.MonitorService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        List<String> indexList = monitorDao.listIndex();
        List<String> fieldList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(indexList)) {
            String index = "";
            index = indexList.get(0);
            fieldList = monitorDao.listField(index, "0");
        }

        esFieldMap.put("indexList", indexList);
        esFieldMap.put("fieldList", fieldList);

        return esFieldMap;
    }

    @Override
    public Map<String, Object> queryByEs(Integer page, Integer pageSize, String index, String field, String
            searchContent) {

        return monitorDao.queryByEs(page, pageSize, index, field, searchContent);
    }

    @Override
    public List<String> listField(String index, String flag) {
        return monitorDao.listField(index, flag);
    }

    @Override
    public Object test() {
        return monitorDao.test();
    }

    @Override
    public List<String> listIndex() {
        return monitorDao.listIndex();
    }

    @Override
    public List<MutilIndexBean> listMultiIndex() {
        return monitorDao.listMultiIndex();
    }

    @Override
    public List<Map<String, Object>> listReflectField(String index, String flag) {
        return monitorDao.listReflectField(index, flag);
    }

    @Override
    @Transactional
    public boolean saveMultiIndex(String multiIndex, List<String> indexList) {

        return monitorDao.saveMultiIndex(multiIndex, indexList);
    }
}
