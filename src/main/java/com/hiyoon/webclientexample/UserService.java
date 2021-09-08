package com.hiyoon.webclientexample;

import com.hiyoon.webclientexample.vo.UserVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final WebClient webClient;
    private final RestTemplate restTemplate;

//    public Mono<UserVo> getUserWithWebClientAndMono(String name) {
//        log.info("################# getUserWithWebClient");
//        return webClient.get()
//                .uri(name)
//                .retrieve()
//                .bodyToMono(UserVo.class);
//    }

    public UserVo getUserWithWebClient(String name) {
        log.info("################# getUserWithWebClient");
        return webClient.get()
                .uri(name)
                .retrieve()
                .bodyToMono(UserVo.class).block();
    }

    public UserVo getUserWithRestTemplate(String name) {
        log.info("################# getUserWithRestTemplate");
        return restTemplate.getForObject(name, UserVo.class);
    }

}
