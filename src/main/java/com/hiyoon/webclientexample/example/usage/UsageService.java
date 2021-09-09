package com.hiyoon.webclientexample.example.usage;

import com.hiyoon.webclientexample.vo.RepoVo;
import com.hiyoon.webclientexample.vo.UserVo;
import com.hiyoon.webclientexample.vo.UserWithItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsageService {

    private final WebClient webClient;

    public Mono<UserVo> getUser(String id) {
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
                .bodyToMono(UserVo.class)
                .log();
    }

    private Mono<RepoVo> getRepo(String repoId) {
        log.info("############# getRepo. repoId: {}", repoId);
        return webClient.get()
                .uri("repos/" + repoId)
                .retrieve()
                .bodyToMono(RepoVo.class)
                .log();
    }

    public Flux<UserVo> mergeUser(String id) {
        return Flux.merge(getUser(id), getUser(id))
                .parallel()
                .runOn(Schedulers.elastic())
                .sequential()
                .log();
    }

    public Mono<UserWithItem> fetchUser(String id, String repoId) {
        Mono<UserVo> user = getUser(id).subscribeOn(Schedulers.elastic());
        Mono<RepoVo> item = getRepo(repoId).subscribeOn(Schedulers.elastic());
        return Mono.zip(user, item, UserWithItem::new)
                .log();
    }

    public Mono<UserWithItem> mixUser(String id, String repoId) {
        Mono<List<UserVo>> users = getUser(id).subscribeOn(Schedulers.elastic())
                .expand(response -> {
                    if (StringUtils.isEmpty(response.getNextPageYn()) || response.getNextPageYn().equals("N")) {
                        return Mono.empty();
                    }

                    return getUser(String.valueOf(Integer.valueOf(response.getId()) + 1)).subscribeOn(Schedulers.elastic());
                }).collectList();
        Mono<List<RepoVo>> item = getRepo(repoId).subscribeOn(Schedulers.elastic())
                .expand(response -> {
                    if (StringUtils.isEmpty(response.getNextPageYn()) || response.getNextPageYn().equals("N")) {
                        return Mono.empty();
                    }

                    return getRepo(String.valueOf(Integer.valueOf(response.getId()) + 1)).subscribeOn(Schedulers.elastic());
                }).collectList();
        return Mono.zip(users, item, UserWithItem::new)
                .log();
    }
}
