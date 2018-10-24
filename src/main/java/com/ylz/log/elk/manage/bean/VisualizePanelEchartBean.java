package com.ylz.log.elk.manage.bean;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "cm_visualize_panel_echart")
public class VisualizePanelEchartBean {

    @Id/* @GeneratedValue(strategy = GenerationType.IDENTITY)*/
    private Integer id;
    private String panelName;
}
