package com.hiyoon.webclientexample.example.compare;

import com.hiyoon.webclientexample.vo.UserVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping(value = "/compare")
@RequiredArgsConstructor
public class CompareController {

    private final CompareService compareService;

//    @GetMapping(value = "/wc/{name}")
//    public Mono<UserVo> getUserWithWebClient(@PathVariable("name") String name) {
//        log.info("############# getUser. name: {}", name);
//        return compareService.getUserWithWebClientAndMono(name);
//    }

    @GetMapping(value = "/wc/{name}")
    public UserVo getUserWithWebClient(@PathVariable("name") String name) {
        log.info("############# getUser. name: {}", name);
        return compareService.getUserWithWebClient(name);
    }

    @GetMapping(value = "/rt/{name}")
    public UserVo getUserWithRestTemplate(@PathVariable("name") String name) {
        log.info("############# getUser. name: {}", name);
        return compareService.getUserWithRestTemplate(name);
    }

}
