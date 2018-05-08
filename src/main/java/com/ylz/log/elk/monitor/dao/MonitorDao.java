package com.ylz.log.elk.monitor.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface MonitorDao {
    List<String> indexList();

    List<String> fieldList(String index);

    Map<String, Object> queryByEs(Integer page, Integer pageSize, String index);
}
