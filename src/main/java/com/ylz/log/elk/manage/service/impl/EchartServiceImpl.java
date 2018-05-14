package com.ylz.log.elk.manage.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ylz.log.elk.manage.bean.VisualizeChartBean;
import com.ylz.log.elk.manage.constants.Constants;
import com.ylz.log.elk.manage.dao.MonitorDao;
import com.ylz.log.elk.manage.dao.mapper.EchartMapper;
import com.ylz.log.elk.manage.service.EchartService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service("echartService")
public class EchartServiceImpl implements EchartService {

    @Autowired
    private EchartMapper echartMapper;

    @Autowired
    private Client client;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private MonitorDao monitorDao;

    @Override
    public Map<String, Object> pageVisualizeEchart(Integer pn, Integer pageSize, String echartName) {
        PageHelper.startPage(pn, pageSize);
        List<VisualizeChartBean> list = echartMapper.pageVisualizeEchart(echartName);

        PageInfo pageInfo = new PageInfo(list);

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("total", pageInfo.getTotal());
        dataMap.put("rows", pageInfo.getList());

        return dataMap;
    }

    @Override
    @Transactional
    public boolean saveVisualizeEchart(VisualizeChartBean visualizeChartBean) {
        int count = echartMapper.saveVisualizeEchart(visualizeChartBean);

        if (count > 0) {
            return true;
        }

        log.error("saveVisualizeEchart: 保存失败，有效行为0");

        return false;
    }

    @Override
    public Map<String, List<String>> listConverField(String index) {
        log.info("listField: index = {}", index);
        Map<String, List<String>> converMap = new HashMap<>();
        List<String> indexList = monitorDao.getRelIndex(index);

        ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> mappings = client.admin().indices()
                .prepareGetMappings(indexList.toArray(new String[]{})).execute()
                .actionGet().getMappings();

        Iterator<String> indexIterator = mappings.keysIt();

        while (indexIterator.hasNext()) {
            String indexKey = indexIterator.next();

            Iterator<String> typeIterator = mappings.get(indexKey).keysIt();

            while (typeIterator.hasNext()) {
                String typeKey = typeIterator.next();

                Object obj = elasticsearchTemplate.getMapping(indexKey, typeKey).get("properties");
                if (obj instanceof Map) {
                    Map<String, Object> map = (Map<String, Object>) obj;

                    for (String key : map.keySet()) {
                        String type = MapUtils.getString((Map) map.get(key), "type", "");

                        if (Constants.converList.contains(type)) {
                            if (converMap.containsKey(key)) {
                                List<String> list = converMap.get(key);
                                list.add(indexKey);
                            } else {
                                List<String> list = new ArrayList<>();
                                list.add(indexKey);
                                converMap.put(key, list);
                            }
                        } else {
                            Object fieldsObj = ((Map) map.get(key)).get("fields");

                            if (fieldsObj instanceof Map) {
                                Map<String, Object> fieldsMap = (Map<String, Object>) fieldsObj;
                                if (fieldsMap.containsKey("keyword")) {
                                    key = key + ".keyword";
                                    if (converMap.containsKey(key)) {
                                        List<String> list = converMap.get(key);
                                        list.add(indexKey);
                                    } else {
                                        List<String> list = new ArrayList<>();
                                        list.add(indexKey);
                                        converMap.put(key, list);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return converMap;
    }

    @Override
    public Map<String, Object> generatEchart(String index, String field, String converMethod) {


        return new HashMap<>();
    }
}
