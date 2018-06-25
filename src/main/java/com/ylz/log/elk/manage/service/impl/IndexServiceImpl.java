package com.ylz.log.elk.manage.service.impl;

import com.ylz.log.elk.base.util.LoginInfoUtil;
import com.ylz.log.elk.manage.bean.MultiIndexBean;
import com.ylz.log.elk.manage.bean.UserCollIndexBean;
import com.ylz.log.elk.manage.dao.IndexDao;
import com.ylz.log.elk.manage.service.IndexService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("indexService")
public class IndexServiceImpl implements IndexService {

    @Autowired
    private IndexDao indexDao;

    @Override
    public Map<String, Object> esFieldMap() {
        Map<String, Object> esFieldMap = new HashMap<>();

        List<Map<String, Object>> indexList = new ArrayList<>();

        List<MultiIndexBean> multiIndexList = indexDao.listMultiIndex();
        List<String> esIndexList = indexDao.listIndex();
        UserCollIndexBean userCollIndexBean = this.getUserCollIndexBean(LoginInfoUtil.getUserId());

        multiIndexList.forEach(multiIndexBean -> {
            Map<String, Object> map = new HashMap<>();

            map.put("index", multiIndexBean.getMultiIndex());
            map.put("type", multiIndexBean.getType());

            if (userCollIndexBean != null && multiIndexBean.getType().equals(userCollIndexBean.getType())
                    && multiIndexBean.getMultiIndex().equals(userCollIndexBean.getIndex())) {
                indexList.add(0, map);
            } else {
                indexList.add(map);
            }
        });

        esIndexList.forEach(str -> {
            Map<String, Object> map = new HashMap<>();

            map.put("index", str);
            map.put("type", "0");

            if (userCollIndexBean != null && "0".equals(userCollIndexBean.getType())
                    && str.equals(userCollIndexBean.getIndex())) {
                indexList.add(0, map);
            } else {
                indexList.add(map);
            }
        });

        List<String> fieldList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(indexList)) {
            String index = MapUtils.getString(indexList.get(0), "index", "");
            String type = MapUtils.getString(indexList.get(0), "type", "");
            fieldList = indexDao.listField(index, type);
        }

        esFieldMap.put("indexList", indexList);
        esFieldMap.put("fieldList", fieldList);

        return esFieldMap;
    }

    @Override
    public Map<String, Object> queryByEs(Integer page, Integer pageSize, String index, String type, String field,
                                         String logLevel, String searchContent) {

        return indexDao.queryByEs(page, pageSize, index, type, field, logLevel, searchContent);
    }

    @Override
    public List<String> listField(String index, String type) {
        return indexDao.listField(index, type);
    }

    @Override
    public List<String> listIndex() {
        return indexDao.listIndex();
    }

    @Override
    public List<MultiIndexBean> listMultiIndex() {
        return indexDao.listMultiIndex();
    }

    @Override
    public List<Map<String, Object>> listReflectField(String index, String type) {
        return indexDao.listReflectField(index, type);
    }

    @Override
    @Transactional
    public boolean saveMultiIndex(String multiIndex, List<String> indexList) {

        return indexDao.saveMultiIndex(multiIndex, indexList);
    }

    @Override
    public Map<String, Object> hasExist(String multiIndex) {
        return indexDao.hasExist(multiIndex);
    }

    @Override
    public Map<String, Object> dealNotIndex(String index, String type) {
        return indexDao.dealNotIndex(index, type);
    }

    @Override
    @Transactional
    public boolean delMultiRelIndex(List<String> indexList) {
        return indexDao.delMultiRelIndex(indexList);
    }

    @Override
    @Transactional
    public boolean delMultiIndex(String multiIndex) {
        boolean flag = indexDao.delMultiIndex(multiIndex);
        flag = (flag) ? indexDao.delMultiRelEchartAndPanel(multiIndex) : flag;

        return flag;
    }

    @Override
    public UserCollIndexBean getUserCollIndexBean(Integer userId) {
        return indexDao.getUserCollIndexBean(userId);
    }

    @Override
    @Transactional
    public boolean dealCollIndex(String index, String type, String action) {
        return indexDao.dealCollIndex(index, type, action);
    }

    @Override
    public List<String> getRelIndex(String index) {
        return indexDao.getRelIndex(index);
    }


}
