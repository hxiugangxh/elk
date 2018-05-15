package com.ylz.log.elk.manage.dao.mapper;

import com.ylz.log.elk.manage.bean.VisualizeChartBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
public interface EchartMapper {

    List<VisualizeChartBean> pageVisualizeEchart(@Param("echartName") String echartName);

    int saveVisualizeEchart(VisualizeChartBean visualizeChartBean);

    int delVisualizeEchart(@Param("idList") List<String> idList);

    VisualizeChartBean getVisualizeEchart(Integer id);

    boolean modifyVisualizeEchart(VisualizeChartBean visualizeChartBean);
}
