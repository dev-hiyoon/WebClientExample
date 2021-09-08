package com.hiyoon.webclientexample.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

import static com.hiyoon.webclientexample.codes.Const.GIT_BASE_URL_2;

@Configuration
public class WebClientComponent {

    @Bean
    public WebClient webClient() {
//        HttpClient httpClient = HttpClient.create()
//                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
//                .responseTimeout(Duration.ofMillis(5000))
//                .doOnConnected(conn ->
//                        conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
//                                .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)));
//
//        return WebClient.builder()
//                .baseUrl(GIT_BASE_URL_2)
//                .clientConnector(new ReactorClientHttpConnector(httpClient))
//                .build();

        return WebClient
                .builder()
                .baseUrl(GIT_BASE_URL_2)
                .clientConnector(
                        new ReactorClientHttpConnector(HttpClient
                                .create()
                                .responseTimeout(Duration.ofSeconds(10)))
                )
                .build();
    }
}
