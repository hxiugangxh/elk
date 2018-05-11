package com.ylz.log.elk.monitor.dao;

import com.ylz.log.elk.monitor.bean.MultiIndexBean;
import com.ylz.log.elk.monitor.bean.UserCollIndexBean;

import java.util.List;
import java.util.Map;

public interface MonitorDao {
    List<String> listIndex();

    List<String> listField(String index, String flag);

    Map<String, Object> queryByEs(Integer page, Integer pageSize, String index, String flag, String field, String
            searchContent);

    Object test();

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
