package com.hiyoon.webclientexample.config;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import static com.hiyoon.webclientexample.codes.Const.GIT_BASE_URL_1;

@Configuration
public class RestTemplateComponent {

    @Bean
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        HttpClient client = HttpClientBuilder.create()
                .setMaxConnTotal(120) // maxConnTotal은 연결을 유지할 최대 숫자
                .setMaxConnPerRoute(50) // maxConnPerRoute는 특정 경로당 최대 숫자
                .build();

        factory.setHttpClient(client);
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(5000);
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(GIT_BASE_URL_1));

        // set ClientHttpRequestFactory for logging
//        ClientHttpRequestFactory factory =
//                new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
//        RestTemplate restTemplate = new RestTemplate(factory);
//        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(GIT_BASE_URL_1));

        // set interceptor
//        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
//        if (CollectionUtils.isEmpty(interceptors)) {
//            interceptors = new ArrayList<>();
//        }
//        interceptors.add(new LoggingInterceptor());
//        restTemplate.setInterceptors(interceptors);

        return restTemplate;
    }
}
