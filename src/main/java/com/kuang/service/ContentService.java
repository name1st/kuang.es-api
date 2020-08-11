package com.kuang.service;

import com.alibaba.fastjson.JSON;
import com.kuang.pojo.Content;
import com.kuang.pojo.MContent;
import com.kuang.utils.HtmlParseUtil;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by wonders on 2020-07-17 09:39
 * 编写业务层
 */
@Service
public class ContentService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    //解析数据放入 es 索引中
    public Boolean parseContent(String keyword) throws Exception {
        List<Content> contents = new HtmlParseUtil().parseJD(keyword);
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("2m");
        for (int i = 0; i < contents.size(); i++) {

            System.out.println(JSON.toJSONString(contents.get(i)));

            bulkRequest.add(new IndexRequest("jd_goods")
                    .source(JSON.toJSONString(contents.get(i)), XContentType.JSON));
        }
        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        return !bulk.hasFailures();
    }


    //解析数据放入 es 索引中
    public Boolean parseMContent(String keyword) throws Exception {
        List<MContent> contents = new HtmlParseUtil().parseMT(keyword);
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("2m");
        for (int i = 0; i < contents.size(); i++) {

            System.out.println(JSON.toJSONString(contents.get(i)));

            bulkRequest.add(new IndexRequest("mt_goods")
                    .source(JSON.toJSONString(contents.get(i)), XContentType.JSON));
        }
        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        return !bulk.hasFailures();
    }

    //获取数据实现搜索功能
    public List<Map<String,Object>> searchPageList(String keyword,int pageNo,int pageSize) throws IOException {
        if(pageNo<=1){
            pageNo = 1;
        }
        SearchRequest searchRequest = new SearchRequest("accept0001");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();


        //分页
        sourceBuilder.from(pageNo);
        sourceBuilder.size(pageSize);

        //精准查询
//        sourceBuilder.query(QueryBuilders.termQuery("title",keyword));
        sourceBuilder.query(QueryBuilders.matchAllQuery());
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        //执行搜索
        searchRequest.source(sourceBuilder);
        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        //解析结果
        ArrayList<Map<String,Object>> list = new ArrayList<>();
        for (SearchHit hit : response.getHits().getHits()) {
            list.add(hit.getSourceAsMap());
        }
        return list;
    }


    //获取数据实现搜索功能
    public List<Map<String,Object>> highlightSearchPageList(String keyword,int pageNo,int pageSize) throws IOException {
        if(pageNo<=1){
            pageNo = 1;
        }
        SearchRequest searchRequest = new SearchRequest("jd_goods");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();


        //分页
        sourceBuilder.from(pageNo);
        sourceBuilder.size(pageSize);

        //精准查询
        sourceBuilder.query(QueryBuilders.termQuery("title",keyword));
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        //高亮查询构建
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title");
        highlightBuilder.preTags("<span style = 'color:red'>");
        highlightBuilder.postTags("</span>");
        highlightBuilder.requireFieldMatch(false);
        sourceBuilder.highlighter(highlightBuilder);

        //执行搜索
        searchRequest.source(sourceBuilder);
        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        //解析结果
        ArrayList<Map<String,Object>> list = new ArrayList<>();
        for (SearchHit hit : response.getHits().getHits()) {

            //解析高亮的字段
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField title = highlightFields.get("title");
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();//原来的结果
            if(title!=null){
                Text[] fragments = title.fragments();
                String name = "";
                for (Text fragment : fragments) {
                    name += fragment;
                }
                sourceAsMap.put("title",name);
            }

            list.add(hit.getSourceAsMap());
        }
        return list;
    }
}
