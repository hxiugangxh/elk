package com.ylz.log.elk.manage.dao.impl;

import com.ylz.log.elk.base.util.EsUtil;
import com.ylz.log.elk.base.util.LoginInfoUtil;
import com.ylz.log.elk.manage.bean.*;
import com.ylz.log.elk.manage.dao.IndexDao;
import com.ylz.log.elk.manage.dao.mapper.EchartMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.*;

@Slf4j
@Repository("indexDao")
public class IndexDaoImpl implements IndexDao {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private Client client;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private EchartMapper echartMapper;

    @Autowired
    private EsUtil esUtil;

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
     * @param type
     * @return
     */
    @Override
    public List<String> listField(String index, String type) {
        log.info("listField: index = {}, type = {}", index, type);
        List<String> indexList = new ArrayList<>();
        Set<String> fieldSet = new HashSet<>();

        if ("1".equals(type)) {
            indexList = this.getRelIndex(index);
        } else {
            indexList.add(index);
        }

        boolean flag = esUtil.isExistsIndex(indexList);
        if (!flag) {
            log.error("有不存在的索引");
            return null;
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

                Object obj = elasticsearchTemplate.getMapping(indexKey, typeKey).get("properties");
                if (obj instanceof Map) {
                    Map<String, Object> map = (Map<String, Object>) obj;

                    fieldSet.addAll(map.keySet());
                }
            }
        }

        List<String> list = new ArrayList<>(fieldSet);

        list.remove("@version");
        list.remove("tags");

        return list;
    }

    // 通过multiIndex关联得到所有的es中的index
    public List<String> getRelIndex(String multiIndex) {
        String querySQL = "select c.*\n" +
                "  from cm_multi_index a\n" +
                "  left join cm_multi_rel_es_index b on a.id = b.multi_index_id\n" +
                "  left join cm_es_index c on c.id = b.es_index_id\n" +
                " where c.index is not null\n" +
                "  and a.multi_index = :multiIndex";

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("multiIndex", multiIndex);

        log.info("getRelIndex--组合索引映射: \n{}\n参数: {}", querySQL, paramMap);

        List<Map<String, Object>> list = this.getNamedParameterJdbcTemplate().queryForList(querySQL, paramMap);

        List<String> indexList = new ArrayList<>();
        list.forEach((map) -> {
            indexList.add(MapUtils.getString(map, "index", ""));
        });

        return indexList;
    }

    @Override
    public Map<String, Object> queryByEs(Integer page, Integer pageSize, String index, String type,
                                         String field, String logLevel, String searchContent) {
        Map<String, Object> dataMap = new HashMap<>();

        List<String> indexList = new ArrayList<>();
        if ("1".equals(type)) {
            indexList = this.getRelIndex(index);
        } else {
            indexList.add(index);
        }

        if (CollectionUtils.isEmpty(indexList)) {
            return dataMap;
        }

        SearchRequestBuilder searchRequestBuilder = this.client.prepareSearch(indexList.toArray(new String[]{}))
                .setFrom(page * pageSize).setSize(pageSize);

        List<String> fieldList = new ArrayList<>();
        if (StringUtils.isEmpty(searchContent) && StringUtils.isEmpty(logLevel)) {
            searchRequestBuilder.setQuery(matchAllQuery());
        } else {
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

            // 暂时先让message可以like全文搜索
            if (StringUtils.isNotEmpty(searchContent)) {
                for (String content : searchContent.toLowerCase().split(" ")) {
                    WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders.wildcardQuery
                            ("message", content + "*");

                    boolQueryBuilder.should(wildcardQueryBuilder);
                }

                // 全文分词搜索
                boolQueryBuilder.should(queryStringQuery(StringUtils.join(searchContent.split(" "), " or ")));
            }

            // 日志级别搜索
            if (StringUtils.isNotEmpty(logLevel)) {
                boolQueryBuilder.must(matchQuery("logLevel", logLevel));
            }

            searchRequestBuilder.setQuery(boolQueryBuilder);

            HighlightBuilder highlightBuilder = new HighlightBuilder();

            if (StringUtils.isNotEmpty(field)) {
                fieldList = Arrays.asList(field.split(","));
            } else {
                fieldList = this.listField(index, type);
            }

            for (String tmpField : fieldList) {
                highlightBuilder.field(tmpField);
            }
            searchRequestBuilder.highlighter(highlightBuilder);
        }

        if (StringUtils.isNotEmpty(field)) {
            searchRequestBuilder.setFetchSource(field.split(","), null);
        }

        log.info("queryByEs--查询es数据: index = {}, relIndex = {}, page = {}, pageSize = {}\n查询DSL: {}",
                index, indexList, page, pageSize, searchRequestBuilder);

        SearchHits hits = searchRequestBuilder
                .setExplain(true).execute().actionGet()
                .getHits();

        long total = hits.getTotalHits();
        long totalPages = (total % pageSize == 0) ? total / pageSize : total / pageSize + 1;
        long currentPage = page + 1;
        if (totalPages != 0 && currentPage > totalPages) {
            log.info("该页面无数据，处理page后再次查询");
            log.info("queryByEs--查询es数据: index = {}, relIndex = {}, page = {}, pageSize = {}\n查询DSL: {}",
                    index, indexList, page, pageSize, searchRequestBuilder);
            currentPage = totalPages;

            return this.queryByEs((int) (currentPage - 1), pageSize, index, type, field, "", searchContent);
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (SearchHit hit : hits) {
            Map<String, Object> source = hit.getSource();
            source.remove("@version");
            source.remove("tags");
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();

            for (String tmpField : fieldList) {
                HighlightField highlightField = highlightFields.get(tmpField);
                if (null != highlightField) {
                    Text[] fragments = highlightField.fragments();
                    String nameTmp = "";
                    for (Text text : fragments) {
                        nameTmp += text;
                    }
                    //将高亮片段组装到结果中去
                    source.put(tmpField, nameTmp);
                }
            }

            result.add(source);
        }

        dataMap.put("currentPage", currentPage);
        dataMap.put("total", total);
        dataMap.put("totalPages", totalPages);
        dataMap.put("source", result);

        return dataMap;
    }

    @Override
    public List<MultiIndexBean> listMultiIndex() {
        String querySQL = "select * from cm_multi_index";

        log.info("listMultiIndex--获取多重复合索引: {}", querySQL);

        return this.jdbcTemplate.query(querySQL, new BeanPropertyRowMapper<>(MultiIndexBean.class));
    }

    @Override
    public List<Map<String, Object>> listReflectField(String index, String type) {
        log.info("listReflectField--获取对应列名: index = {}, type = {}", index, type);

        List<Map<String, Object>> list = new ArrayList<>();
        List<String> indexList = new ArrayList<>();
        if ("1".equals(type)) {
            indexList = this.getRelIndex(index);

            if (CollectionUtils.isEmpty(indexList)) {
                return list;
            }
        } else {
            indexList.add(index);
        }

        log.info("listReflectField: indexList = {}", indexList);

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

                    map.keySet().forEach((key) -> {
                        Map<String, Object> dataMap = new HashMap<>();

                        if (!("@version".equals(key) || "tags".equals(key))) {
                            dataMap.put("index", indexKey);
                            dataMap.put("field", key);

                            list.add(dataMap);
                        }
                    });
                }
            }
        }

        return list;
    }

    @Override
    public boolean saveMultiIndex(String multiIndex, List<String> indexList) {
        log.info("saveMultiIndex: index = {}, indexList = {}", multiIndex, indexList);
        MultiIndexBean mutilIndexBean = new MultiIndexBean(multiIndex);
        entityManager.persist(mutilIndexBean);

        List<EsIndexBean> esIndexList = new EsIndexBean(indexList).getEsIndex();

        esIndexList.forEach((esIndexBean) -> {
            entityManager.persist(esIndexBean);
        });

        Integer length = esIndexList.size();
        String insertSQL = "insert into cm_multi_rel_es_index values";
        for (int i = 0; i < length; i++) {
            if (i == length - 1) {
                insertSQL += "(" + mutilIndexBean.getId() + ", " + esIndexList.get(i).getId() + ")";
            } else {
                insertSQL += "(" + mutilIndexBean.getId() + ", " + esIndexList.get(i).getId() + "),";
            }
        }

        log.info("saveMultiIndex--批量保存关系: {}", insertSQL);

        Integer count = this.jdbcTemplate.update(insertSQL);
        if (count > 0) {
            return true;
        }

        log.error("saveMultiIndex--保存失败，有效行数为0");

        return false;
    }

    @Override
    public Map<String, Object> hasExist(String multiIndex) {
        Map<String, Object> dataMap = new HashMap<>();
        String querySQL = "select count(1) from cm_multi_index where multi_index = '" + multiIndex + "'";

        log.info("hasExist: {}", querySQL);
        Integer count = this.jdbcTemplate.queryForObject(querySQL, Integer.class);

        if (count > 0) {
            dataMap.put("flag", true);
        } else {
            dataMap.put("flag", false);
        }

        return dataMap;
    }

    @Override
    public Map<String, Object> dealNotIndex(String index, String type) {
        Map<String, Object> dataMap = new HashMap<>();
        List<String> indexList = new ArrayList<>();

        if ("1".equals(type)) {
            log.info("dealNotIndex: getRelIndex");
            indexList = this.getRelIndex(index);
        } else {
            String errorMsg = "系统异常，请联系管理员";
            dataMap.put("flag", false);
            dataMap.put("errorMsg", errorMsg);

            log.info("dealNotIndex: {}", errorMsg);
        }

        List<String> notExistList = new ArrayList<>();
        indexList.forEach((indexName) -> {
            if (!esUtil.isExistsIndex(indexName)) {
                notExistList.add(indexName);
            }
        });

        if (notExistList.size() > 0) {
            String errorMsg = "错误信息：" + notExistList + " 已经不存在了";
            dataMap.put("flag", false);
            dataMap.put("errorMsg", errorMsg);
            dataMap.put("indexList", indexList);
            dataMap.put("notExistList", notExistList);

            log.info("dealNotIndex: 映射索引：{}", indexList);
            log.info("dealNotIndex: 不存在索引：{}", notExistList);
            log.info("dealNotIndex: {}", errorMsg);
        }
        dataMap.put("indexList", indexList);
        dataMap.put("notExistList", notExistList);

        return dataMap;
    }

    /**
     * 删除数据库管控的ES索引，同时删除与其关联的组合索引关联关系
     *
     * @param indexList
     * @return
     */
    @Override
    public boolean delMultiRelIndex(List<String> indexList) {
        String querySQL = "select * from cm_es_index where `index` in (:indexList)";

        Map<String, Object> paramMap = new HashMap<>();

        paramMap.put("indexList", indexList);

        log.info("delMultiRelIndex--获取索引内容:\n{}\n参数: {}", querySQL, paramMap);
        List<Map<String, Object>> list = this.getNamedParameterJdbcTemplate().queryForList(querySQL, paramMap);

        String delSQL = "delete from cm_es_index where `index` in (:indexList)";
        log.info("delMultiRelIndex--删除ES索引:\n{},\n参数: {}", delSQL, paramMap);
        Integer count = this.getNamedParameterJdbcTemplate().update(delSQL, paramMap);

        paramMap.clear();
        List<String> esIndexIdList = new ArrayList<>();

        list.forEach((map) -> {
            esIndexIdList.add(MapUtils.getString(map, "id", ""));
        });
        paramMap.put("esIndexIdList", esIndexIdList);
        delSQL = "delete from cm_multi_rel_es_index where es_index_id in (:esIndexIdList)";
        log.info("delMultiRelIndex--删除索引与目录映射关系:\n{},\n参数: {}", delSQL, paramMap);
        this.getNamedParameterJdbcTemplate().update(delSQL, paramMap);

        paramMap.clear();
        paramMap.put("indexList", indexList);
        delSQL = "delete from cm_visualize_echart_rel_index where `index` in (:indexList)";
        log.info("delMultiRelIndex--删除索引与图表映射关系:\n{},\n参数: {}", delSQL, paramMap);
        this.getNamedParameterJdbcTemplate().update(delSQL, paramMap);

        if (count > 0) {
            return true;
        }

        log.error("delMultiRelIndex--删除失败，有效行数为0");

        return false;
    }

    @Override
    public boolean delMultiIndex(String multiIndex) {
        Map<String, Object> paramMap = new HashMap<>();

        paramMap.put("multiIndex", multiIndex);
        String querySQL = "select * from cm_multi_index WHERE multi_index = :multiIndex";

        log.info("delMultiIndex--获取组合索引数据:\n{}\n参数：{}", querySQL, paramMap);

        List<MultiIndexBean> multiIndexList = this.getNamedParameterJdbcTemplate().query(querySQL, paramMap, new
                BeanPropertyRowMapper(MultiIndexBean.class));

        // 防止前端传递意外数据
        if (CollectionUtils.isEmpty(multiIndexList)) {
            log.error("delMultiIndex--没有找到组合索引数据");

            return false;
        }

        MultiIndexBean multiIndexBean = multiIndexList.get(0);

        String delSQL = "delete from cm_es_index\n" +
                " where id in (select *\n" +
                "                from (select b.id\n" +
                "                        from cm_multi_rel_es_index a\n" +
                "                        left join cm_es_index b on a.es_index_id = b.id\n" +
                "                       where a.multi_index_id = :multiIndexId) t)";

        paramMap.clear();
        paramMap.put("multiIndexId", multiIndexBean.getId());

        log.info("delMultiIndex--删除数据库管理的es的index值:\n{},\n参数: {}", delSQL, paramMap);
        this.getNamedParameterJdbcTemplate().update(delSQL, paramMap);

        delSQL = "delete from cm_multi_rel_es_index where multi_index_id = :multiIndexId";
        log.info("delMultiIndex--删除组合索引与es索引的关系表:\n{},\n参数: {}", delSQL, paramMap);
        this.getNamedParameterJdbcTemplate().update(delSQL, paramMap);

        delSQL = "delete from cm_user_coll_index where user_name = '" + LoginInfoUtil.getUserName() + "'";
        this.jdbcTemplate.update(delSQL);

        delSQL = "delete from cm_multi_index where id = :multiIndexId";
        log.info("delMultiIndex--删除组合索引:\n{},\n参数: {}", delSQL, paramMap);
        Integer count = this.getNamedParameterJdbcTemplate().update(delSQL, paramMap);

        if (count > 0) {
            return true;
        }

        log.error("delMultiIndex--删除失败，有效行数为0");

        return false;
    }

    @Override
    public UserCollIndexBean getUserCollIndexBean(String userName) {
        String querySQL = "select * from cm_user_coll_index where user_name = '" + userName + "'";

        log.info("getUserCollIndexBean: {}", querySQL);

        List<UserCollIndexBean> list = this.jdbcTemplate.query(querySQL,
                new BeanPropertyRowMapper(UserCollIndexBean.class));

        return (list.size() > 0) ? list.get(0) : null;
    }

    @Override
    public boolean dealCollIndex(String index, String type, String action) {
        Map<String, Object> paramMap = new HashMap<>();

        paramMap.put("userName", LoginInfoUtil.getUserName());
        String delSQL = "delete from cm_user_coll_index where user_name = :userName";
        log.info("dealCollIndex--删除原有收藏: {}, \n参数: {}", delSQL, paramMap);
        Integer delCount = this.getNamedParameterJdbcTemplate().update(delSQL, paramMap);
        Integer saveCount = 0;

        if ("save".equals(action)) {
            String saveSQL = "insert into cm_user_coll_index values(:userName, :index, :type)";

            paramMap.put("index", index);
            paramMap.put("type", type);

            log.info("dealCollIndex--保存收藏: {}, \n参数: {}", saveSQL, paramMap);
            saveCount = this.getNamedParameterJdbcTemplate().update(saveSQL, paramMap);

            if (saveCount > 0) {
                return true;
            }
        } else {
            if (delCount > 0) {
                return true;
            }
        }

        log.error("dealCollIndex--保存失败，有效行为0");

        return false;
    }

    /**
     * 删除所有基于组合索引的图标与面板
     * 步骤一：获取组合索引关联的图标，获得应该删除的echartIdDelList
     * 步骤二：获取应该删除的图标所关联的面板，且记为面板A
     * 步骤三：获取面板A对应的所有的图表的panelRelEchartList并且过滤得到不需要删除的visualizePanelRelEchartList
     * 步骤四：删除所有面板对应的关联关系，保存不需要删除的visualizePanelRelEchartList
     * 步骤五：删除应该删除的图表数据
     * 步骤六：检测面板关联数据是否已经变为空，若为空则删除
     *
     * @param multiIndex
     * @return
     */
    @Override
    public boolean delMultiRelEchartAndPanel(String multiIndex) {
        Map<String, Object> paramMap = new HashMap<>();
        String querySQL = "select * from cm_visualize_echart where multi_index = :multiIndex";

        paramMap.put("multiIndex", multiIndex);

        log.info("delMultiRelEchartAndPanel:获取组合索引关联的图表:\n{}\n参数：{}", querySQL, paramMap);
        List<MultiIndexBean> multiIndexList = this.getNamedParameterJdbcTemplate().query(querySQL, paramMap,
                new BeanPropertyRowMapper(MultiIndexBean.class));

        List<Integer> echartIdDelList = multiIndexList.stream()
                .map(MultiIndexBean::getId)
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(echartIdDelList)) {
            return true;
        }

        querySQL = "select *\n" +
                "  from cm_visualize_panel_echart\n" +
                " where id in (select panel_id\n" +
                "                from cm_visualize_panel_rel_echart\n" +
                "               where echart_id in (:echartIdDelList))";

        paramMap.clear();
        paramMap.put("echartIdDelList", echartIdDelList);

        log.info("delMultiRelEchartAndPanel:获取需要删除的图表对应的面板数据:\n{}\n参数：{}", querySQL, paramMap);
        List<VisualizeChartBean> visualizeChartList = this.getNamedParameterJdbcTemplate()
                .query(querySQL, paramMap, new BeanPropertyRowMapper(VisualizeChartBean.class));

        List<Integer> panelIdList = visualizeChartList.stream()
                .map(VisualizeChartBean::getId)
                .collect(Collectors.toList());

        List<VisualizePanelRelEchartBean> panelRelEchartList = new ArrayList<>();
        paramMap.clear();
        paramMap.put("panelIdList", panelIdList);
        if (CollectionUtils.isNotEmpty(panelIdList)) {

            querySQL = "select * from cm_visualize_panel_rel_echart where panel_id in (:panelIdList)";
            log.info("delMultiRelEchartAndPanel:获取面板对应的图表关系数据:\n{}\n参数：{}", querySQL, paramMap);

            panelRelEchartList = this.getNamedParameterJdbcTemplate()
                    .query(querySQL, paramMap, new BeanPropertyRowMapper(VisualizePanelRelEchartBean.class));
        }

        // 获取不需要删除的图标ID的集合
        List<VisualizePanelRelEchartBean> visualizePanelRelEchartList = panelRelEchartList.stream()
                .filter(panelRelEchart ->
                        !echartIdDelList.contains(panelRelEchart.getEchartId()) && panelRelEchart.getEchartId() != -1
                )
                .collect(Collectors.toList());

        // 删除关联图表
        echartMapper.delVisualizePanelRelEchart(panelIdList);
        if (CollectionUtils.isNotEmpty(visualizePanelRelEchartList)) {
            Integer tmp = visualizePanelRelEchartList.get(0).getPanelId();
            List<String> echartIdList = new ArrayList<>();
            for (VisualizePanelRelEchartBean visualizePanelRelEchartBean : visualizePanelRelEchartList) {
                Integer panelId = visualizePanelRelEchartBean.getPanelId();
                if (tmp != panelId) {
                    int length = (echartIdList.size() < 4) ? 4 - echartIdList.size() : 0;
                    for (int i = 0; i < length; i++) {
                        echartIdList.add("-1");
                    }
                    echartMapper.savePanelRelEchart(tmp, echartIdList);
                    echartIdList = new ArrayList<>();
                    tmp = panelId;
                }

                echartIdList.add(visualizePanelRelEchartBean.getEchartId() + "");
            }
            int length = (echartIdList.size() < 4) ? 4 - echartIdList.size() : 0;
            for (int i = 0; i < length; i++) {
                echartIdList.add("-1");
            }
            echartMapper.savePanelRelEchart(tmp, echartIdList);
        }

        String delSQL = "delete from cm_visualize_echart where multi_index = :multiIndex";
        paramMap.clear();
        paramMap.put("multiIndex", multiIndex);
        log.info("delMultiRelEchartAndPanel:删除关联图表:\n{}\n参数：{}", delSQL, paramMap);
        this.getNamedParameterJdbcTemplate().update(delSQL, paramMap);

        // 获取需要删除的面板panel
        List<VisualizePanelEchartBean> visualizePanelEchartList = echartMapper.getVisualizePanelEchartRelNull();

        List<Integer> delPanelIdList = visualizePanelEchartList.stream()
                .map(VisualizePanelEchartBean::getId)
                .collect(Collectors.toList());

        // 如果有需要删除的panel那么就删除
        if (CollectionUtils.isNotEmpty(delPanelIdList)) {
            echartMapper.delVisualizePanelEchartRelNull(delPanelIdList);
        }

        return true;
    }
}
