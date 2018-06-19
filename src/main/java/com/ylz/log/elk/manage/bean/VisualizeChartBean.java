package com.ylz.log.elk.manage.bean;

import lombok.Data;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@Entity
@Table(name = "cm_visualize_echart")
public class VisualizeChartBean {

    public static final Map<Integer, Object> typeMap;

    static {
        typeMap = new HashMap<>();

        /**
         * 1 折线；2：柱状；3：饼状
         */
        typeMap.put(1, "<i class='iconfont icon-61'></i>");
        typeMap.put(2, "<i class='iconfont icon-62'></i>");
        typeMap.put(3, "<i class='iconfont icon-slice33'></i>");
    }

    public static final Map<String, DateHistogramInterval> formataMap;

    static {
        formataMap = new HashMap<>();

        formataMap.put("yyyy-MM-dd HH:mm:ss", DateHistogramInterval.SECOND);
        formataMap.put("yyyy-MM-dd HH:mm", DateHistogramInterval.MINUTE);
        formataMap.put("yyyy-MM-dd HH", DateHistogramInterval.HOUR);
        formataMap.put("yyyy-MM-dd", DateHistogramInterval.DAY);
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
