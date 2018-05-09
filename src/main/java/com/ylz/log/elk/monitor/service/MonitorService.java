package com.ylz.log.elk.monitor.service;

import com.ylz.log.elk.monitor.bean.MutilIndexBean;

import java.util.List;
import java.util.Map;

public interface MonitorService {
    Map<String, Object> esFieldMap();

    Map<String, Object> queryByEs(Integer page, Integer pageSize, String index, String field, String searchContent);

    List<String> listField(String index, String flag);

    Object test();

    List<String> listIndex();

    List<MutilIndexBean> listMultiIndex();

    List<Map<String,Object>> listReflectField(String index, String flag);

    boolean saveMultiIndex(String multiIndex, List<String> indexList);
}
