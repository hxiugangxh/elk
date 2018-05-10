package com.ylz.log.elk.monitor.service;

import com.ylz.log.elk.monitor.bean.MultiIndexBean;

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
}
