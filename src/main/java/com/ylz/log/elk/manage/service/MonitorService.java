package com.ylz.log.elk.manage.service;

import com.ylz.log.elk.manage.bean.MultiIndexBean;
import com.ylz.log.elk.manage.bean.UserCollIndexBean;

import java.util.List;
import java.util.Map;

public interface MonitorService {
    Map<String, Object> esFieldMap();

    Map<String, Object> queryByEs(Integer page, Integer pageSize, String index, String flag, String field, String
            searchContent);

    List<String> listField(String index, String flag);

    Object test();

    List<String> listIndex();

    List<MultiIndexBean> listMultiIndex();

    List<Map<String, Object>> listReflectField(String index, String flag);

    boolean saveMultiIndex(String multiIndex, List<String> indexList);

    Map<String, Object> hasExist(String multiIndex);

    Map<String, Object> dealNotIndex(String index, String flag);

    boolean delMultiRelIndex(List<String> indexList);

    boolean delMultiIndex(String multiIndex);

    UserCollIndexBean getUserCollIndexBean(Integer userId);

    boolean dealCollIndex(String index, String flag, String action);
}