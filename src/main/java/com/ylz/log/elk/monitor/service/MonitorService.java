package com.ylz.log.elk.monitor.service;

import java.util.List;
import java.util.Map;

public interface MonitorService {
    Map<String, Object> esFieldMap();

    List<Map<String, Object>> queryByEs(String index);
}
