# Webclient

### WebClient란?

Spring Webflux에는 reactive, non-blocking하게 HTTP 요청을 처리할 수 있도록 WebClient라는 모듈을 제공한다. 기존의 RestTemplate과 같은 역할 하지만,
non-blocking하다라는 점에서 차이가 있다.

내부적으로 WebClient는 HTTP 클라이언트 라이브러리에 위임하는데, 디폴트로 Reactor Netty의 HttpClient를 사용한다. Reactor Netty 외에도, Jetty의 HttpClient를
지원하며, 다른 라이브러리도 ClientHttpConnector에 넣어주면 사용할 수 있다.
<img src="https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbmXoLk%2FbtqXXVBywK0%2F9jWK84VgwC4NqNWnTkY9H0%2Fimg.jpg"></img>

```gradle
dependencies {
    compile 'org.springframework.boot:spring-boot-starter-webflux'
}
```

### RestTemplate과의 비교

```console
>java -jar C:\Users\11102\Documents\01.workspace\02.hiyoon\WebClientExample\build\libs\WebClientExample-0.0.1-SNAPSHOT.jar --server.port=8081
>java -jar C:\Users\11102\Documents\01.workspace\02.hiyoon\WebClientExample\build\libs\WebClientExample-0.0.1-SNAPSHOT.jar --server.port=8082
>java -jar C:\Users\11102\Documents\01.workspace\02.hiyoon\SimpleRestApi\build\libs\SimpleRestApi-0.0.1-SNAPSHOT.jar --server.port=9091
>java -jar C:\Users\11102\Documents\01.workspace\02.hiyoon\SimpleRestApi\build\libs\SimpleRestApi-0.0.1-SNAPSHOT.jar --server.port=9092
>java -jar C:\Users\11102\Documents\01.workspace\02.hiyoon\ActuatorMonitor\build\libs\ActuatorMonitor-0.0.1-SNAPSHOT.jar
```

* [http://localhost:8000/applications](http://localhost:8000/applications)

![Response_Time_Graph](webclient_Response_time_graph.PNG)
![Summary_Report](webclient_summary_report.PNG)

### Schedulers

Project Reactor의 핵심 패키지 중 하나인 reactor.core.scheduler에는 Schedulers 라는 추상 클래스가 존재한다. 이 Schedulers는 Scheduler 인터페이스의 팩토리
클래스이고, publishOn과 subscribeOn 을 위한 여러가지 팩토리 메서드를 제공한다.

팩토리 메서드는 대표적으로 아래와 같다.

* parallel():  ExecutorService기반으로 단일 스레드 고정 크기(Fixed) 스레드 풀을 사용하여 병렬 작업에 적합함.
* single(): Runnable을 사용하여 지연이 적은 일회성 작업에 최적화
* elastic(): 스레드 갯수는 무한정으로 증가할 수 있고 수행시간이 오래걸리는 블로킹 작업에 대한 대안으로 사용할 수 있게 최적화 되어있다.
* boundedElastic(): 스레드 갯수가 정해져있고 elastic과 동일하게 수행시간이 오래걸리는 블로킹 작업에 대한 대안으로 사용할 수 있게 최적화 되어있다.
* immediate(): 호출자의 스레드를 즉시 실행한다.
* fromExecutorService(ExecutorService) : 새로운 Excutors 인스턴스를 생성한다.

### Examples

1. 간단한 호출

   WebClient의 GET 호출 예시입니다.

```java

private Mono<UserVo> getUser(String id){
    log.info("############# getUser. id: {}",id);
    return webClient.get()
    .uri("users/v1/"+id)
    .retrieve()
    .bodyToMono(UserVo.class)
    .log();
}

```

2. 다른 서비스, 같은 타입

   다른 서비스지만 반환 타입이 같은 경우에는 Flux.merge를 사용합니다.

```java

private Mono<UserVo> getUser(String id){
    log.info("############# getUser. id: {}",id);
    return webClient.get()
    .uri("users/v1/"+id)
    .retrieve()
    .bodyToMono(UserVo.class)
    .log();
}

private Mono<UserVo> getOtherUser(String id){
    log.info("############# getOtherUser. id: {}",id);
    return webClient.get()
    .uri("users/v2/"+id)
    .retrieve()
    .bodyToMono(UserVo.class);
}

public Flux<UserVo> mergeUser(String id){
    return Flux.merge(getUser(id),getOtherUser(id))
    .parallel()
    .runOn(Schedulers.elastic())
    .sequential()
    .log();
}

```

3. 다른 서비스, 다른 타입

   별개의 End-Point이면서 반환 타입도 다른 경우에는 각각의 Mono를 셋팅하여 Mono.zip으로 처리합니다.

```java
private Mono<UserVo> getUser(String id) {
    log.info("############# getUser. id: {}", id);
    return webClient.get()
    .uri("users/v1/" + id)
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
        
public Mono fetchUserAndItem(int userId,int itemId){
    Mono<User> user=getUser(userId).subscribeOn(Schedulers.elastic());
    Mono<Item> item=getItem(itemId).subscribeOn(Schedulers.elastic());

    return Mono.zip(user,item,UserWithItem::new);
}
```

4. 혼합

   동시호출과 순차호출이 Mix되어야하는 경우 Mono.then, Mono.zip으로 처리합니다. ex) 다른 서비스, 서비스마다 페이징이 있는 경우

```java
private Mono<UserVo> getUser(String id) {
    log.info("############# getUser. id: {}", id);
    return webClient.get()
    .uri("users/v1/" + id)
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
public Mono<UserWithItem> mixUser(String id,String repoId){
   Mono<List<UserVo>>users=getUser(id).subscribeOn(Schedulers.elastic())
      .expand(response->{
          if(StringUtils.isEmpty(response.getNextPageYn())||response.getNextPageYn().equals("N")){
              return Mono.empty();
          }
   
          return getUser(String.valueOf(Integer.valueOf(response.getId())+1)).subscribeOn(Schedulers.elastic());
      }).collectList();
   Mono<List<RepoVo>>item=getRepo(repoId).subscribeOn(Schedulers.elastic())
      .expand(response->{
          if(StringUtils.isEmpty(response.getNextPageYn())||response.getNextPageYn().equals("N")){
              return Mono.empty();
          }
   
          return getRepo(String.valueOf(Integer.valueOf(response.getId())+1)).subscribeOn(Schedulers.elastic());
      }).collectList();
   
   return Mono.zip(users,item,UserWithItem::new)
   .log();
}

```

### JUnit Test

WebClient 테스트에서 Mockito를 사용하기에는 너무 설정하는 값이 많아서 MockWebServer나 WireMockServer를 사용하는 것을 추천한다. 아래 예제는 WebClient의 URL을 동적으로 셋팅하는 설정도 포함한다.

```gradle
dependencies {
    testImplementation group: 'com.squareup.okhttp3', name: 'okhttp', version: '4.9.1'
    testImplementation group: 'com.squareup.okhttp3', name: 'mockwebserver', version: '4.9.1'
    testImplementation("com.squareup.okhttp3:mockwebserver:4.9.1")
    testImplementation group: 'io.projectreactor', name: 'reactor-test', version: '3.4.9'
}
```

```java
@DynamicPropertySource
static void properties(DynamicPropertyRegistry r) {
    r.add("user.server.url", () -> "http://localhost:" + mockBackEnd.getPort() + "/");
}

@Value("${user.server.url}")
private String userServiceUrl;

@Bean
public WebClient webClient() {    
   return WebClient.builder()
   .baseUrl(StringUtils.isEmpty(userServiceUrl) ? GIT_BASE_URL_2 : userServiceUrl)
   .build();
}
```

1. UsageControllerMockWebServerTest
    * MockWebServer를 사용하여 테스트한다. 
    * /usage/user는 UserVo이기 때문에 response body확인하여 검증하였다.
2. UsageControllerWireMockServerTest
    * MockWebServer를 사용하여 테스트한다.
    * /usage/user는 UserVo이기 때문에 response body확인하여 검증하였다.
    * /usage/mono-user Mono<UserVo> 이기 때문에 response body를 확인할 수가 없다. 따라서 service endpoint에서 반환되는 Mono<UserVo>의 값을 StepVerifier로 검증한다.
    * 나머지는 /usage/mono-user와 동일하다.


### 질문

1. 실제서비스에서 Reactive, Non-Blocking하게만 사용할 수 있는지?
2. Blocking처리가 꼭 필요한 경우(거의 대부분...) 어떤 식으로 처리하는게 나을지?

### Additional Links

* [spring-5-webclient](https://www.baeldung.com/spring-5-webclient)
* [Spring WebClient 쉽게 이해하기](https://happycloud-lee.tistory.com/220)
* [Simultaneous Spring WebClient Calls](https://www.baeldung.com/spring-webclient-simultaneous-calls)
* [reactor.core.scheduler.Schedulers](https://projectreactor.io/docs/core/release/api/reactor/core/scheduler/Schedulers.html#elastic--)
* [Schedulers-정리](https://devsh.tistory.com/entry/Schedulers-정리)
* [Spring WebClient](https://dreamchaser3.tistory.com/11)
* [spring-mocking-webclient](https://www.baeldung.com/spring-mocking-webclient)