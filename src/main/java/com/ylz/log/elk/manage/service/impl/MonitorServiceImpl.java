package com.ylz.log.elk.manage.service.impl;

import com.ylz.log.elk.base.util.LoginInfoUtil;
import com.ylz.log.elk.manage.bean.MultiIndexBean;
import com.ylz.log.elk.manage.bean.UserCollIndexBean;
import com.ylz.log.elk.manage.dao.MonitorDao;
import com.ylz.log.elk.manage.service.MonitorService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
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

        List<Map<String, Object>> indexList = new ArrayList<>();

        List<MultiIndexBean> multiIndexList = monitorDao.listMultiIndex();
        List<String> esIndexList = monitorDao.listIndex();
        UserCollIndexBean userCollIndexBean = this.getUserCollIndexBean(LoginInfoUtil.getUserId());

        if (userCollIndexBean != null) {
            String flag = userCollIndexBean.getFlag();

            multiIndexList.forEach(multiIndexBean -> {
                Map<String, Object> map = new HashMap<>();

                map.put("index", multiIndexBean.getMultiIndex());
                map.put("flag", "1");

                if ("1".equals(flag) && multiIndexBean.getMultiIndex().equals(userCollIndexBean.getIndex())) {
                    indexList.add(0, map);
                } else {
                    indexList.add(map);
                }
            });

            esIndexList.forEach(str -> {
                Map<String, Object> map = new HashMap<>();

                map.put("index", str);
                map.put("flag", "0");

                if ("0".equals(flag) && str.equals(userCollIndexBean.getIndex())) {
                    indexList.add(0, map);
                } else {
                    indexList.add(map);
                }
            });
        }

        List<String> fieldList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(indexList)) {
            String index = MapUtils.getString(indexList.get(0), "index", "");
            String flag = MapUtils.getString(indexList.get(0), "flag", "");
            fieldList = monitorDao.listField(index, flag);
        }

        esFieldMap.put("indexList", indexList);
        esFieldMap.put("fieldList", fieldList);

        return esFieldMap;
    }

    @Override
    public Map<String, Object> queryByEs(Integer page, Integer pageSize, String index, String type, String field, String
            searchContent) {

        return monitorDao.queryByEs(page, pageSize, index, type, field, searchContent);
    }

    @Override
    public List<String> listField(String index, String type) {
        return monitorDao.listField(index, type);
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
    public List<MultiIndexBean> listMultiIndex() {
        return monitorDao.listMultiIndex();
    }

    @Override
    public List<Map<String, Object>> listReflectField(String index, String type) {
        return monitorDao.listReflectField(index, type);
    }

    @Override
    @Transactional
    public boolean saveMultiIndex(String multiIndex, List<String> indexList) {

        return monitorDao.saveMultiIndex(multiIndex, indexList);
    }

    @Override
    public Map<String, Object> hasExist(String multiIndex) {
        return monitorDao.hasExist(multiIndex);
    }

    @Override
    public Map<String, Object> dealNotIndex(String index, String type) {
        return monitorDao.dealNotIndex(index, type);
    }

    @Override
    @Transactional
    public boolean delMultiRelIndex(List<String> indexList) {
        return monitorDao.delMultiRelIndex(indexList);
    }

    @Override
    @Transactional
    public boolean delMultiIndex(String multiIndex) {
        return monitorDao.delMultiIndex(multiIndex);
    }

    @Override
    public UserCollIndexBean getUserCollIndexBean(Integer userId) {
        return monitorDao.getUserCollIndexBean(userId);
    }

    @Override
    @Transactional
    public boolean dealCollIndex(String index, String type, String action) {
        return monitorDao.dealCollIndex(index, type, action);
    }
}
