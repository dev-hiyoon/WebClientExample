package com.hiyoon.webclientexample.example.usage;

import com.hiyoon.webclientexample.vo.UserVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequestMapping(value = "/usage")
@RequiredArgsConstructor
public class UsageController {

    private final UsageService usageService;

    @GetMapping(value = "/merge-user")
    public Flux<UserVo> mergeUser() {
        return usageService.mergeUser("1");
    }
}
