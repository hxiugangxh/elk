package com.ylz.log.elk.manage.dao.mapper;

import com.ylz.log.elk.manage.bean.VisualizeChartBean;
import com.ylz.log.elk.manage.bean.VisualizePanelEchartBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Component
public interface EchartMapper {

    List<VisualizeChartBean> pageVisualizeEchart(
            @Param("echartName") String echartName,
            @Param("sortName") String sortName,
            @Param("sortOrder") String sortOrder);

    int saveVisualizeEchart(VisualizeChartBean visualizeChartBean);

    int delVisualizeEchart(@Param("idList") List<String> idList);

    VisualizeChartBean getVisualizeEchart(Integer id);

    boolean modifyVisualizeEchart(VisualizeChartBean visualizeChartBean);

    List<VisualizePanelEchartBean> pageVisualizePanelEchart(
            @Param("panelName") String panelName,
            @Param("sortName") String sortName,
            @Param("sortOrder") String sortOrder);
}
