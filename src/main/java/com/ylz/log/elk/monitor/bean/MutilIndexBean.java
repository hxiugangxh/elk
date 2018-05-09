package com.ylz.log.elk.monitor.bean;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "cm_multi_index")
public class MutilIndexBean {

    public MutilIndexBean() {}

    public MutilIndexBean(String multiIndex) {
        this.multiIndex = multiIndex;
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String multiIndex;

    // 挂载一个证明是组合索引的标致
    @Transient
    private String flag = "1";
}
