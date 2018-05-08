package com.ylz.log.elk.monitor.dao.impl;

import com.ylz.log.elk.monitor.dao.MonitorDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository("monitorDao")
public class MonitorDaoImpl implements MonitorDao {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private Client client;

    /**
     * 获取所有的index
     *
     * @return
     */
    @Override
    public List<String> indexList() {

        Set<String> indexSet = client.admin().indices().stats(new IndicesStatsRequest().all()).actionGet()
                .getIndices().keySet();

        return new ArrayList<>(indexSet);
    }

    /**
     * 通过index获取type，以type获取其所有字段
     *
     * @param index
     * @return
     */
    @Override
    public List<String> fieldList(String index) {
        List<String> fieldList = new ArrayList<>();

        ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> mappings = client.admin().indices()
                .prepareGetMappings(index).execute()
                .actionGet().getMappings();

        if (mappings.get(index).keys().size() > 0) {
            Iterator<String> stringIterator = mappings.get(index).keysIt();

            //type已经只有一个
            String type = stringIterator.next();

            Collection values = elasticsearchTemplate.getMapping(index, type).values();

            if (CollectionUtils.isNotEmpty(values)) {
                Iterator iterator = values.iterator();
                while (iterator.hasNext()) {
                    Map<String, ?> obj = (Map<String, ?>) iterator.next();

                    fieldList.addAll(obj.keySet());
                }
            }
        }

        return fieldList;
    }

    @Override
    public Map<String, Object> queryByEs(Integer page, Integer pageSize, String index) {

        log.info("queryByEs--查询es数据: index = {}", index);

        Map<String, Object> dataMap = new HashMap<>();

        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();

        SearchResponse searchResponse = this.client.prepareSearch(index)
                .setQuery(matchAllQueryBuilder).setFrom(page).setSize(pageSize).setExplain(true).execute().actionGet();

        SearchHits hits = searchResponse.getHits();

        List<Map<String, Object>> result = new ArrayList<>();
        for (SearchHit hit : hits) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", hit.getId());
            map.putAll(hit.getSource());
            result.add(map);
        }

        long total = hits.getTotalHits();
        dataMap.put("total", hits.getTotalHits());
        dataMap.put("totalPages", (total % pageSize == 0) ? total / pageSize : total / pageSize + 1);
        dataMap.put("source", result);

        return dataMap;
    }
}
