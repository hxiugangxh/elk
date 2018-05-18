package com.ylz.log.elk.manage.dao.mapper;

import com.ylz.log.elk.manage.bean.VisualizeChartBean;
import com.ylz.log.elk.manage.bean.VisualizePanelEchartBean;
import com.ylz.log.elk.manage.bean.VisualizePanelRelEchartBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

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

    int delVisualizePanelEchart(@Param("idList") List<String> idList);

    int delVisualizePanelRelEchart(@Param("idList") List<String> id);

    VisualizePanelEchartBean getVisualizePanel(@Param("id") Integer id);

    List<VisualizePanelRelEchartBean> listPanelRelEchart(Integer id);

    int savePanelRelEchart(@Param("panel") Integer panel, @Param("echartIdList") List<String> echartIdList);

    int modifyVisualizePanelEchart(VisualizePanelEchartBean visualizePanelEchartBean);

    int hasExistPanelName(@Param("panelName") String panelName);

    int hasExistEchartName(@Param("echartName") String echartName);

    int valiteMutilIndex(@Param("id") Integer id);
}
