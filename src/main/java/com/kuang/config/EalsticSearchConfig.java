package com.kuang.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by wonders on 2020-07-16 14:48
 * spring 两步骤
 * <p>
 * 1.找对象
 * 2.放到spring中待用
 */
@Configuration
public class EalsticSearchConfig {

    //spring <bean id= class=>
    @Bean
    public RestHighLevelClient restHighLevelClient() {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("10.2.8.123", 9200, "http")));
        return client;

    }
}
