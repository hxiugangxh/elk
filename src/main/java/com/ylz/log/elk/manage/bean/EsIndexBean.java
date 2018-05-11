package com.ylz.log.elk.manage.bean;

import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "cm_es_index")
public class EsIndexBean {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "\"index\"")
    private String index;

    public EsIndexBean() {}

    public EsIndexBean(String index) {
        this.index = index;
    }

    public EsIndexBean(List<String> indexList) {
        this.indexList = indexList;
    }

    @Transient
    private List<String> indexList;

    public List<EsIndexBean> getEsIndex() {
        List<EsIndexBean> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(indexList)) {
            indexList.forEach((index) -> {
                list.add(new EsIndexBean(index));
            });
        }

        return list;
    }
}
