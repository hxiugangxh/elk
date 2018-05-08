package com.ylz.log.elk.monitor.service;

import java.util.List;
import java.util.Map;

public interface MonitorService {
    Map<String, Object> esFieldMap();

    Map<String, Object> queryByEs(Integer page, Integer pageSize, String index, String field, String searchContent);

    List<String> changeIndex(String index);

    List<Map<String, Object>> test();
}
