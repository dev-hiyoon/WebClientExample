package com.hiyoon.webclientexample.example.compare;

import com.hiyoon.webclientexample.vo.UserVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.hiyoon.webclientexample.codes.Const.GIT_ENDPOINT_SIMPLE;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompareService {

    private final WebClient webClient;
    private final RestTemplate restTemplate;

    public Mono<UserVo> getUserWithWebClientAndMono(String name) {
        log.info("################# getUserWithWebClientAndMono");
        return webClient.get()
                .uri(name)
                .retrieve()
                .bodyToMono(UserVo.class);
    }

    public UserVo getUserWithWebClient(String name) {
        log.info("################# getUserWithWebClient");
        return webClient.get()
                .uri(GIT_ENDPOINT_SIMPLE + name)
                .retrieve()
                .bodyToMono(UserVo.class).block();
    }

    public UserVo getUserWithRestTemplate(String name) {
        log.info("################# getUserWithRestTemplate");
        return restTemplate.getForObject(GIT_ENDPOINT_SIMPLE + name, UserVo.class);
    }

}
