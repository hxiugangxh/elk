package com.ylz.log.elk.manage.constants;

import org.apache.commons.lang.time.DateUtils;

import java.util.*;

public class Constants {
    // ES可以汇聚的字段类型
    public static List<String> converList = new ArrayList<>();

    static {
        converList.add("date");
        converList.add("keyword");
        converList.add("integer");
    }

    public static Map<String, String> formateMap = new LinkedHashMap<>();

    static {
        formateMap.put("1", "yyyy-MM-dd HH:mm:ss");
        formateMap.put("2", "yyyy-MM-dd HH:mm");
        formateMap.put("3", "yyyy-MM-dd HH");
        formateMap.put("4", "yyyy-MM-dd");
    }

    public static Map<Integer, Object> timeFieldMap =new LinkedHashMap<>();

    static {
        timeFieldMap.put(Calendar.DAY_OF_WEEK, "天");
        timeFieldMap.put(Calendar.HOUR, "时");
        timeFieldMap.put(Calendar.MINUTE, "分");
        timeFieldMap.put(Calendar.SECOND, "秒");
    }
}
