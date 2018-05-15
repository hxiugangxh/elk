package com.ylz.log.elk.manage.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ylz.log.elk.manage.bean.VisualizeChartBean;
import com.ylz.log.elk.manage.bean.VisualizePanelEchartBean;
import com.ylz.log.elk.manage.constants.Constants;
import com.ylz.log.elk.manage.dao.IndexDao;
import com.ylz.log.elk.manage.dao.mapper.EchartMapper;
import com.ylz.log.elk.manage.service.EchartService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
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
    private IndexDao indexDao;


    @Override
    public Map<String, Object> pageVisualizeEchart(
            Integer pn, Integer pageSize, String echartName, String sortName, String sortOrder) {
        PageHelper.startPage(pn, pageSize);
        List<VisualizeChartBean> list = echartMapper.pageVisualizeEchart(echartName, sortName, sortOrder);

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
        List<String> indexList = indexDao.getRelIndex(index);

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
                    Map<String, Object> map = (LinkedHashMap<String, Object>) obj;

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
    public Map<String, Object> generatEchart(String relIndex, String field, Integer lastDay) {
        Map<String, Object> dataMap = new HashMap<>();

        List<String> xAxisDataList = new ArrayList<>();
        List<Long> seriesDataList = new ArrayList<>();
        List<Map<String, Object>> pieSeriesDataList = new ArrayList<>();

        TermsAggregationBuilder termsAgg = AggregationBuilders.terms(field).field(field).size(Integer.MAX_VALUE);

        SearchResponse searchResponse = this.client.prepareSearch(relIndex.split(","))
                .addAggregation(termsAgg)
                .execute().actionGet();

        Aggregations aggregations = searchResponse.getAggregations();

        Object obj = aggregations.get(field);
        if (obj instanceof LongTerms) {
            LongTerms teamAgg = (LongTerms) aggregations.get(field);
            Iterator<LongTerms.Bucket> teamBucketIt = teamAgg.getBuckets().iterator();
            while (teamBucketIt.hasNext()) {
                LongTerms.Bucket bucket = teamBucketIt.next();
                String key = bucket.getKeyAsString() + "";
                long count = bucket.getDocCount();

                xAxisDataList.add(key);
                seriesDataList.add(count);

                Map<String, Object> map = new HashMap<>();

                map.put("name", key);
                map.put("value", count);

                pieSeriesDataList.add(map);
            }
        } else if (obj instanceof StringTerms) {
            StringTerms teamAgg = (StringTerms) aggregations.get(field);
            Iterator<StringTerms.Bucket> teamBucketIt = teamAgg.getBuckets().iterator();
            while (teamBucketIt.hasNext()) {
                StringTerms.Bucket bucket = teamBucketIt.next();
                String key = bucket.getKey() + "";
                long count = bucket.getDocCount();

                xAxisDataList.add(key);
                seriesDataList.add(count);

                Map<String, Object> map = new HashMap<>();

                map.put("name", key);
                map.put("value", count);

                pieSeriesDataList.add(map);
            }
        }


        dataMap.put("xAxisDataList", xAxisDataList);
        dataMap.put("seriesDataList", seriesDataList);
        dataMap.put("pieSeriesDataList", pieSeriesDataList);

        return dataMap;
    }

    @Override
    @Transactional
    public boolean delVisualizeEchart(List<String> idList) {

        int count = echartMapper.delVisualizeEchart(idList);

        if (count > 0) {
            return true;
        }

        log.error("saveVisualizeEchart: 保存失败，有效行为0");

        return false;
    }

    @Override
    public VisualizeChartBean getVisualizeEchart(Integer id) {
        return echartMapper.getVisualizeEchart(id);
    }

    @Override
    @Transactional
    public boolean modifyVisualizeEchart(VisualizeChartBean visualizeChartBean) {
        return echartMapper.modifyVisualizeEchart(visualizeChartBean);
    }

    @Override
    public Map<String, Object> pageVisualizePanelEchart(Integer pn, Integer pageSize, String panelName, String
            sortName, String sortOrder) {
        PageHelper.startPage(pn, pageSize);
        List<VisualizePanelEchartBean> list = echartMapper.pageVisualizePanelEchart(panelName, sortName, sortOrder);

        PageInfo pageInfo = new PageInfo(list);

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("total", pageInfo.getTotal());
        dataMap.put("rows", pageInfo.getList());

        return dataMap;
    }
}
