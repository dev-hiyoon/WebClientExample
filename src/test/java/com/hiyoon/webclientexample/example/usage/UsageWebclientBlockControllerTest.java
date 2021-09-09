package com.hiyoon.webclientexample.example.usage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.hiyoon.webclientexample.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class UsageWebclientBlockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static WireMockServer wireMockServer;
    private ObjectMapper mapper = new ObjectMapper();
    private String userVoResponse;
    private UserVo userVo = UserVo.builder().id("2").login("login").build();

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry r) {
        r.add("user.server.url", () -> "http://localhost:" + wireMockServer.port());
    }

    @BeforeAll
    static void beforeAll() throws IOException {
        wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());
    }

    @AfterAll
    static void afterAll() throws IOException {
        wireMockServer.shutdown();
    }

    @BeforeEach
    public void beforeEach() throws JsonProcessingException {
        userVoResponse = mapper.writeValueAsString(userVo);
    }

    @Test
    public void getUser() throws Exception {
        log.info("########### getUser started!");

        // given
        stubFor(get(urlEqualTo("/users/v1/1")).willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(userVoResponse)));

        // when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.get("/usage/user")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void mergeUser() {
    }

    @Test
    public void fetchUser() {
    }

    @Test
    public void mixUser() {
    }

}