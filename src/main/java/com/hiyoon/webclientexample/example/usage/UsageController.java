package com.hiyoon.webclientexample.example.usage;

import com.hiyoon.webclientexample.vo.UserVo;
import com.hiyoon.webclientexample.vo.UserWithItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping(value = "/usage")
@RequiredArgsConstructor
public class UsageController {

    private final UsageService usageService;

    @GetMapping(value = "/user")
    public UserVo getUser() {
        return usageService.getUser("1").block();
    }

    @GetMapping(value = "/mono-user")
    public Mono<UserVo> getMonoUser() {
        return usageService.getUser("1");
    }

    @GetMapping(value = "/merge-user")
    public Flux<UserVo> mergeUser() {
        return usageService.mergeUser("1");
    }

    @GetMapping(value = "/fetch-user")
    public Mono<UserWithItem> fetchUser() {
        return usageService.fetchUser("1", "2");
    }

    @GetMapping(value = "/mix-user")
    public Mono<UserWithItem> mixUser() {
        return usageService.mixUser("1", "2");
    }
}
