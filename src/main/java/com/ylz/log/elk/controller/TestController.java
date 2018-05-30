package com.ylz.log.elk.controller;

import com.ylz.log.elk.manage.service.IndexService;
import org.elasticsearch.action.bulk.BulkAction;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
public class TestController {

    @Autowired
    private Client client;

    @Autowired
    private IndexService indexService;

    // git提交
    @GetMapping("/test")
    public String index() {

        List<String> indexList = indexService.getRelIndex("易联众日志");

        Integer page = 1;
        Integer pageSize = 6000;

        SearchRequestBuilder searchRequestBuilder = this.client.prepareSearch("ylz--2018.05.16")
                .setFrom(10000).setSize(2341);

        searchRequestBuilder.setQuery(QueryBuilders.matchAllQuery());
        SearchHits hits = searchRequestBuilder
                .setExplain(true).execute().actionGet()
                .getHits();

        List<String> idList = new ArrayList<>();
        for (SearchHit hit : hits) {
            idList.add(hit.getId());
        }

        for (String id : idList) {
            UpdateRequest updateRequest = new UpdateRequest("ylz--2018.05.16", "doc", id);

            try {
                XContentBuilder builder = XContentFactory.jsonBuilder().startObject();

                builder.field("host", "rhel8");
                builder.endObject();
                updateRequest.doc(builder);
                UpdateResponse updateResponse = this.client.update(updateRequest).get();
                System.out.println(updateResponse);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return "1232131";
    }

}
