package com.salemanager.application.config;

import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(50);
        connectionManager.setDefaultMaxPerRoute(20);

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(
                HttpClientBuilder.create().setConnectionManager(connectionManager).build());
        requestFactory.setConnectionRequestTimeout(5000);
        requestFactory.setConnectTimeout(5000);
        return new RestTemplate(requestFactory);
    }
}
