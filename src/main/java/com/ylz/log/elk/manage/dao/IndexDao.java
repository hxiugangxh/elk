package com.ylz.log.elk.manage.dao;

import com.ylz.log.elk.manage.bean.MultiIndexBean;
import com.ylz.log.elk.manage.bean.UserCollIndexBean;

import java.util.List;
import java.util.Map;

public interface IndexDao {
    List<String> listIndex();

    List<String> listField(String index, String type);

    Map<String, Object> queryByEs(Integer page, Integer pageSize, String index, String type, String field, String
            searchContent);

    Object test();

    List<MultiIndexBean> listMultiIndex();

    List<Map<String, Object>> listReflectField(String index, String type);

    boolean saveMultiIndex(String multiIndex, List<String> indexList);

    Map<String, Object> hasExist(String multiIndex);

    Map<String, Object> dealNotIndex(String index, String type);

    boolean delMultiRelIndex(List<String> indexList);

    boolean delMultiIndex(String multiIndex);

    UserCollIndexBean getUserCollIndexBean(Integer userId);

    boolean dealCollIndex(String s, String type, String index);

    List<String> getRelIndex(String index);

    boolean delMultiRelEchartAndPanel(String multiIndex);
}
