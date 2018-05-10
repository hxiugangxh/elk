package com.ylz.log.elk.monitor.dao;

import com.ylz.log.elk.monitor.bean.MutilIndexBean;

import java.util.List;
import java.util.Map;

public interface MonitorDao {
    List<String> listIndex();

    List<String> listField(String index, String flag);

    Map<String, Object> queryByEs(Integer page, Integer pageSize, String index, String field, String searchContent);

    Object test();

    List<MutilIndexBean> listMultiIndex();

    List<Map<String,Object>> listReflectField(String index, String flag);

    boolean saveMultiIndex(String multiIndex, List<String> indexList);

    Map<String,Object> hasExist(String multiIndex);

    Map<String,Object> dealNotIndex(String index, String flag);

    boolean delMultiRelIndex(List<String> indexList);
}
