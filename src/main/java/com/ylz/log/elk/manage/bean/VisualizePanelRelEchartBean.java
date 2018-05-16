package com.ylz.log.elk.manage.bean;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "cm_visualize_panel_rel_echart")
public class VisualizePanelRelEchartBean {
    private Integer panelId;
    private Integer echartId;
    private Integer sort;
}
