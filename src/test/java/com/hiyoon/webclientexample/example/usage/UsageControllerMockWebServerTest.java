package com.hiyoon.webclientexample.example.usage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hiyoon.webclientexample.vo.RepoVo;
import com.hiyoon.webclientexample.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
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
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class UsageControllerMockWebServerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsageService usageService;

    public static MockWebServer mockBackEnd;
    private ObjectMapper mapper = new ObjectMapper();
    private static String user1VoResponse;
    private static String user2VoResponse;
    private static String repoVoResponse;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry r) {
        r.add("user.server.url", () -> "http://localhost:" + mockBackEnd.getPort() + "/");
    }

    @BeforeAll
    static void setUp() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        user1VoResponse = mapper.writeValueAsString(UserVo.builder().id("1").login("login1").build());
        user2VoResponse = mapper.writeValueAsString(UserVo.builder().id("2").login("login2").build());
        repoVoResponse = mapper.writeValueAsString(RepoVo.builder().id("3").fullName("fullName3").name("name3").build());

        mockBackEnd = new MockWebServer();
        mockBackEnd.setDispatcher(new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) {
                if (request.getPath().contains("users/v1")) {
                    return new MockResponse().setBody(user1VoResponse).addHeader("Content-Type", "application/json");
                } else if (request.getPath().contains("users/v2")) {
                    return new MockResponse().setBody(user2VoResponse).addHeader("Content-Type", "application/json");
                } else if (request.getPath().contains("repos")) {
                    return new MockResponse().setBody(repoVoResponse).addHeader("Content-Type", "application/json");
                }

                return new MockResponse().setResponseCode(404);
            }
        });
        mockBackEnd.start();
        configureFor("localhost", mockBackEnd.getPort());
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @BeforeEach
    public void beforeEach() throws JsonProcessingException {
    }

    @Test
    public void getUser() throws Exception {
        log.info("########### getUser started!");

        // given

        // when
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.get("/usage/user")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void getMonoUser() throws Exception {
        log.info("########### getMonoUser started!");

        // given
//        mockBackEnd.enqueue(new MockResponse()
//                .setBody(user1VoResponse)
//                .addHeader("Content-Type", "application/json"));

        // when
        Mono<UserVo> userVoMono = usageService.getUser("1");

        // then
        StepVerifier.create(userVoMono)
                .expectNextMatches(x -> x.getId().equals("2"))
                .verifyComplete();
    }

    @Test
    public void mergeUser() throws Exception {
        log.info("########### mergeUser started!");

        // given
//        mockBackEnd.enqueue(new MockResponse()
//                .setBody(user1VoResponse)
//                .addHeader("Content-Type", "application/json"));
//        mockBackEnd.enqueue(new MockResponse()
//                .setBody(repoVoResponse)
//                .addHeader("Content-Type", "application/json"));

        // when
        Mono<UserVo> userVoMono = usageService.getUser("1");

        // then
        StepVerifier.create(userVoMono)
                .expectNextMatches(x -> x.getId().equals("2"))
                .verifyComplete();
    }

    @Test
    public void fetchUser() {
    }

    @Test
    public void mixUser() {
    }

}