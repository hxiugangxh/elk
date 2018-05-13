package com.ylz.log.elk.manage.bean;

import lombok.Data;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Data
public class VisualizeChartBean {

    public static Map<Integer, Object> typeMap;

    static {
        typeMap = new HashMap<>();

        typeMap.put(1, "<i class='iconfont icon-61'></i>");
        typeMap.put(2, "<i class='iconfont icon-62'></i>");
        typeMap.put(3, "<i class='iconfont icon-slice33'></i>");
    }

    private Integer id;
    private String echartName;
    private Integer type;

    public String getType() {
        String str = MapUtils.getString(VisualizeChartBean.typeMap, type, "");
        if (StringUtils.isEmpty(str)) {
            Map<String, Object> map = new HashMap<>();

            map.put("1", "折线图");
            map.put("2", "柱状图");
            map.put("3", "饼状图");
            str = "type值出错: " + map;
        }

        return str;
    }

}
