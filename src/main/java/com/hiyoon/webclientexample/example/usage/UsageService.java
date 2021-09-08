package com.hiyoon.webclientexample.example.usage;

import com.hiyoon.webclientexample.vo.UserVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsageService {

    private final WebClient webClient;

    private Mono<UserVo> getUser(String id) {
        log.info("############# getUser. id: {}", id);
        return webClient.get()
                .uri("users/v1/" + id)
                .retrieve()
                .bodyToMono(UserVo.class)
                .log();
    }

    private Mono<UserVo> getOtherUser(String id) {
        log.info("############# getOtherUser. id: {}", id);
        return webClient.get()
                .uri("users/v2/" + id)
                .retrieve()
                .bodyToMono(UserVo.class);
    }

    public Flux<UserVo> mergeUser(String id) {
        return Flux.merge(getUser(id), getOtherUser(id))
                .parallel()
                .runOn(Schedulers.elastic())
                .sequential()
                .log();
    }

}
