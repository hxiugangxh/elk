package com.ylz.log.elk.controller;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.hibernate.bytecode.internal.javassist.BulkAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TestController {

    @Autowired
    private Client client;

    @GetMapping("/modifyEsData")
    public String modifyEsData(
            @RequestParam(value = "index",defaultValue = "") String index,
            @RequestParam(value = "from", defaultValue = "0") Integer from,
            @RequestParam(value = "size", defaultValue = "100") Integer size,
            @RequestParam(value = "hostName", defaultValue = "localhost") String hostName
            ) {

        index = "ylz--2018.05.16";
        from = 13000;
        size = 21231;
        hostName = "relh5";

        SearchRequestBuilder searchRequestBuilder = this.client.prepareSearch(index)
                .setFrom(from).setSize(size);

        searchRequestBuilder.setQuery(QueryBuilders.matchAllQuery());
        SearchHits hits = searchRequestBuilder
                .setExplain(true).execute().actionGet()
                .getHits();

        List<String> idList = new ArrayList<>();
        for (SearchHit hit : hits) {
            idList.add(hit.getId());
        }

        BulkRequestBuilder bulkRequestBuilder = this.client.prepareBulk();
        for (String id : idList) {
            UpdateRequest updateRequest = new UpdateRequest(index, "doc", id);

            try {
                XContentBuilder builder = XContentFactory.jsonBuilder().startObject();

                builder.field("host", hostName);
                builder.endObject();
                updateRequest.doc(builder);
                bulkRequestBuilder.add(updateRequest);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        bulkRequestBuilder.execute().actionGet();

        return "update success";
    }

}
