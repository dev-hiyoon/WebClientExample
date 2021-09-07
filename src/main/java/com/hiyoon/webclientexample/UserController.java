package com.hiyoon.webclientexample;

import com.hiyoon.webclientexample.vo.UserVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/wc/{name}")
    public UserVo getUserWithWebClient(@PathVariable("name") String name) {
        log.info("############# getUser. name: {}", name);
        return userService.getUserWithWebClient(name);
    }

    @GetMapping(value = "/rt/{name}")
    public UserVo getUserWithRestTemplate(@PathVariable("name") String name) {
        log.info("############# getUser. name: {}", name);
        return userService.getUserWithRestTemplate(name);
    }

}
