package com.ylz.log.elk.manage.bean;

import lombok.Data;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@Entity
@Table(name = "cm_visualize_echart")
public class VisualizeChartBean {

    public static Map<Integer, Object> typeMap;

    static {
        typeMap = new HashMap<>();

        /**
         * 1 折线；2：柱状；3：饼状
         */
        typeMap.put(1, "<i class='iconfont icon-61'></i>");
        typeMap.put(2, "<i class='iconfont icon-62'></i>");
        typeMap.put(3, "<i class='iconfont icon-slice33'></i>");
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String echartName;
    private Integer type;
    private String multiIndex;
    private String field;
    private String relIndex;
    private String filterStr;
    private Integer last;
    private Integer timeField;
    private String formate;

    @Transient
    private String typePo;

    public String getTypePo() {
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
