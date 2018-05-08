package com.ylz.log.elk.monitor.dao.impl;

import com.ylz.log.elk.monitor.dao.MonitorDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

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
    public Map<String, Object> queryByEs(Integer page, Integer pageSize, String index, String field, String
            searchContent) {
        Map<String, Object> dataMap = new HashMap<>();

        SearchRequestBuilder searchRequestBuilder = this.client.prepareSearch(index)
                .setFrom(page).setSize(pageSize);

        if (StringUtils.isEmpty(searchContent)) {
            searchRequestBuilder.setQuery(matchAllQuery());
        } else {
            searchRequestBuilder.setQuery(queryStringQuery(searchContent));
        }

        if (StringUtils.isNotEmpty(field)) {
            searchRequestBuilder.setFetchSource(field.split(","), null);
        }


        log.info("\nqueryByEs--查询es数据: index = {}, page = {}, pageSize = {}\n查询DSL: {}",
                index, page, pageSize, searchRequestBuilder);

        SearchHits hits = searchRequestBuilder
                .setExplain(true).execute().actionGet()
                .getHits();

        long total = hits.getTotalHits();
        long totalPages = (total % pageSize == 0) ? total / pageSize : total / pageSize + 1;
        long currentPage = page + 1;
        if (totalPages != 0 && currentPage > totalPages) {
            log.info("该页面无数据，处理page后再次查询");
            currentPage = totalPages;

            return this.queryByEs((int) (currentPage - 1), pageSize, index, field, searchContent);
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (SearchHit hit : hits) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", hit.getId());
            map.putAll(hit.getSource());

            result.add(map);
        }

        dataMap.put("currentPage", currentPage);
        dataMap.put("total", total);
        dataMap.put("totalPages", totalPages);
        dataMap.put("source", result);

        return dataMap;
    }
}
