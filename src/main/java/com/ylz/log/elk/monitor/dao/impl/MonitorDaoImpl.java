package com.ylz.log.elk.monitor.dao.impl;

import com.ylz.log.elk.monitor.bean.EsIndexBean;
import com.ylz.log.elk.monitor.bean.MutilIndexBean;
import com.ylz.log.elk.monitor.dao.MonitorDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
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

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityManager entityManager;

    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(this.jdbcTemplate);
    }

    /**
     * 获取所有的index
     *
     * @return
     */
    @Override
    public List<String> listIndex() {

        Set<String> indexSet = client.admin().indices().stats(new IndicesStatsRequest().all()).actionGet()
                .getIndices().keySet();

        return new ArrayList<>(indexSet);
    }

    /**
     * 通过index获取type，以type获取其所有字段
     *
     * @param index
     * @param flag
     * @return
     */
    @Override
    public List<String> listField(String index, String flag) {
        log.info("listField: index = {}, flag = {}", index, flag);
        List<String> indexList = new ArrayList<>();
        Set<String> fieldSet = new HashSet<>();

        if ("1".equals(flag)) {
            indexList = this.getRelIndex(index);
        } else {
            indexList.add(index);
        }

        ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> mappings = client.admin().indices()
                .prepareGetMappings(indexList.toArray(new String[]{})).execute()
                .actionGet().getMappings();

        Iterator<String> indexIterator = mappings.keysIt();

        while (indexIterator.hasNext()) {
            String indexKey = indexIterator.next();

            Iterator<String> typeIterator = mappings.get(indexKey).keysIt();

            while (typeIterator.hasNext()) {
                String typeKey = typeIterator.next();

                Collection values = elasticsearchTemplate.getMapping(indexKey, typeKey).values();

                if (CollectionUtils.isNotEmpty(values)) {
                    Iterator iterator = values.iterator();
                    while (iterator.hasNext()) {
                        Map<String, ?> obj = (Map<String, ?>) iterator.next();

                        fieldSet.addAll(obj.keySet());
                    }
                }
            }
        }

        return new ArrayList<>(fieldSet);
    }

    // 通过multiIndex关联得到所有的es中的index
    public List<String> getRelIndex(String multiIndex) {
        String querySQL = "select c.*\n" +
                "  from cm_multi_index a\n" +
                "  left join cm_multi_es_index_rel b on a.id = b.multi_index_id\n" +
                "  left join cm_es_index c on c.id = b.es_index_id\n" +
                " where a.multi_index = :multiIndex";

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("multiIndex", multiIndex);

        log.info("getRelIndex--组合索引映射: \n" + querySQL + "\n参数: " + paramMap);

        List<Map<String, Object>> list = this.getNamedParameterJdbcTemplate().queryForList(querySQL, paramMap);

        List<String> indexList = new ArrayList<>();
        list.forEach((map) -> {
            indexList.add(MapUtils.getString(map, "index"));
        });

        return indexList;
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

    @Override
    public Object test() {
        String index = "hello*";
        List<Map<String, Object>> list = new ArrayList<>();

        ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> mappings = client.admin().indices()
                .prepareGetMappings(index).execute()
                .actionGet().getMappings();

        Iterator<String> indexIterator = mappings.keysIt();

        while (indexIterator.hasNext()) {
            String indexKey = indexIterator.next();

            System.out.println(indexKey);

            Iterator<String> typeIterator = mappings.get(indexKey).keysIt();

            while (typeIterator.hasNext()) {
                String typeKey = typeIterator.next();
                Map<String, Object> dataMap = new HashMap<>();

                Collection values = elasticsearchTemplate.getMapping(indexKey, typeKey).values();

                if (CollectionUtils.isNotEmpty(values)) {

                    Iterator iterator = values.iterator();
                    while (iterator.hasNext()) {
                        Map<String, ?> obj = (Map<String, ?>) iterator.next();

                        obj.forEach((key, value) -> {
                            dataMap.put(indexKey, key);

                            list.add(dataMap);
                        });

                    }
                }
            }
        }

        return list;
    }

    @Override
    public List<MutilIndexBean> listMultiIndex() {
        String querySQL = "select * from cm_multi_index";

        log.info("listMultiIndex--获取多重复合索引:\n" + querySQL);

        return this.jdbcTemplate.query(querySQL, new BeanPropertyRowMapper<>(MutilIndexBean.class));
    }

    @Override
    public List<Map<String, Object>> listReflectField(String index, String flag) {
        log.info("listReflectField获取对应列名: index = {}, flag = {}", index, flag);

        List<Map<String, Object>> list = new ArrayList<>();
        List<String> indexList = new ArrayList<>();
        if ("1".equals(flag)) {
            indexList = this.getRelIndex(index);
        } else {
            indexList.add(index);
        }

        ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> mappings = client.admin().indices()
                .prepareGetMappings(indexList.toArray(new String[]{})).execute()
                .actionGet().getMappings();

        Iterator<String> indexIterator = mappings.keysIt();

        while (indexIterator.hasNext()) {
            String indexKey = indexIterator.next();

            Iterator<String> typeIterator = mappings.get(indexKey).keysIt();

            while (typeIterator.hasNext()) {
                String typeKey = typeIterator.next();

                Collection values = elasticsearchTemplate.getMapping(indexKey, typeKey).values();

                if (CollectionUtils.isNotEmpty(values)) {
                    Iterator iterator = values.iterator();

                    while (iterator.hasNext()) {
                        Map<String, ?> obj = (Map<String, ?>) iterator.next();

                        obj.keySet().forEach((key) -> {
                            Map<String, Object> dataMap = new HashMap<>();

                            dataMap.put("index", indexKey);
                            dataMap.put("field", key);

                            list.add(dataMap);
                        });

                    }
                }
            }
        }

        return list;
    }

    @Override
    public boolean saveMultiIndex(String multiIndex, List<String> indexList) {
        log.info("saveMultiIndex: index = {}, indexList = {}", multiIndex, indexList);
        MutilIndexBean mutilIndexBean = new MutilIndexBean(multiIndex);
        entityManager.persist(mutilIndexBean);

        List<EsIndexBean> esIndexList = new EsIndexBean(indexList).getEsIndex();

        esIndexList.forEach((esIndexBean) -> {
            entityManager.persist(esIndexBean);
        });

        Integer length = esIndexList.size();
        String insertSQL = "insert into cm_multi_es_index_rel values";
        for (int i = 0; i < length; i++) {
            if (i == length - 1) {
                insertSQL += "(" + mutilIndexBean.getId() + ", " + esIndexList.get(i).getId() + ")";
            } else {
                insertSQL += "(" + mutilIndexBean.getId() + ", " + esIndexList.get(i).getId() + "),";
            }
        }

        log.info("saveMultiIndex--批量保存关系: " + insertSQL);

        boolean flag = false;
        Integer count =  this.jdbcTemplate.update(insertSQL);
        if (count > 0) {
            flag = true;
        }

        return flag;
    }

}
