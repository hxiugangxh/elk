package com.ylz.log.elk.monitor.dao;

import com.ylz.log.elk.monitor.bean.MutilIndexBean;

import java.util.List;
import java.util.Map;

public interface MonitorDao {
    List<String> listIndex();

    List<String> listField(String index);

    Map<String, Object> queryByEs(Integer page, Integer pageSize, String index, String field, String searchContent);

    Object test();

    List<MutilIndexBean> listMultiIndex();
}
