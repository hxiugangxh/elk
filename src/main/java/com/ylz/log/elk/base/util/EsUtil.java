package com.ylz.log.elk.base.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EsUtil {

    @Autowired
    private TransportClient transportClient;

    /**
     * 判断指定的索引名是否存在
     *
     * @param indexName 索引名
     * @return 存在：true; 不存在：false;
     */
    public boolean isExistsIndex(String indexName) {

        IndicesExistsResponse response = transportClient
                .admin()
                .indices()
                .exists(new IndicesExistsRequest()
                        .indices(new String[]{indexName})).actionGet();
        return response.isExists();
    }

    public boolean isExistsIndex(List<String> indexList) {

        if (CollectionUtils.isEmpty(indexList)) {
            return false;
        }

        IndicesExistsResponse response = transportClient
                .admin()
                .indices()
                .exists(new IndicesExistsRequest()
                        .indices(indexList.toArray(new String[]{}))).actionGet();
        return response.isExists();
    }

    /**
     * 判断指定的索引的类型是否存在
     *
     * @param indexName 索引名
     * @param indexType 索引类型
     * @return 存在：true; 不存在：false;
     */
    public boolean isExistsType(String indexName, String indexType) {
        TypesExistsResponse response = transportClient
                .admin()
                .indices()
                .typesExists(
                        new TypesExistsRequest(new String[]{indexName},
                                indexType)).actionGet();
        return response.isExists();
    }
}
