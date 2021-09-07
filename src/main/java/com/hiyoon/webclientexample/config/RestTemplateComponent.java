package com.hiyoon.webclientexample.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.ArrayList;
import java.util.List;

import static com.hiyoon.webclientexample.codes.Const.GIT_BASE_URL;

@Configuration
public class RestTemplateComponent {

    @Bean
    public RestTemplate restTemplate() {
//        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
//        HttpClient client = HttpClientBuilder.create()
//                .setMaxConnTotal(50)
//                .setMaxConnPerRoute(20)
//                .build();
//
//        factory.setHttpClient(client);
//        factory.setConnectTimeout(3000);
//        factory.setReadTimeout(3000);
//        RestTemplate restTemplate = new RestTemplate(factory);
//        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(GIT_BASE_URL));

        // set ClientHttpRequestFactory for logging
        ClientHttpRequestFactory factory =
                new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(GIT_BASE_URL));

        // set interceptor
        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
        if (CollectionUtils.isEmpty(interceptors)) {
            interceptors = new ArrayList<>();
        }
        interceptors.add(new LoggingInterceptor());
        restTemplate.setInterceptors(interceptors);

        return restTemplate;
    }
}
