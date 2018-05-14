package com.ylz.log.elk.manage.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constants {
    // ES可以汇聚的字段类型
    public static List<String> converList;
    static {
        converList = new ArrayList<>();

        converList.add("date");
        converList.add("keyword");
        converList.add("integer");
    }
}
