# Webclient

### WebClient란?

Spring WebFlux includes a reactive, non-blocking WebClient for HTTP requests. The client has a functional, fluent API with reactive types for declarative composition, see webflux-reactive-libraries. WebFlux client and server rely on the same non-blocking codecs to encode and decode request and response content.

Internally WebClient delegates to an HTTP client library. By default, it uses Reactor Netty, there is built-in support for the Jetty reactive HttpClient, and others can be plugged in through a ClientHttpConnector.

<img src="https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbmXoLk%2FbtqXXVBywK0%2F9jWK84VgwC4NqNWnTkY9H0%2Fimg.jpg"></img>

### vs RestTemplate

```console
PS C:\Users\11102>java -jar D:\03.작업공간\02.hiyoon\WebClientExample\build\libs\WebClientExample-0.0.1-SNAPSHOT.jar --server.port=8081
PS C:\Users\11102>java -jar D:\03.작업공간\02.hiyoon\WebClientExample\build\libs\WebClientExample-0.0.1-SNAPSHOT.jar --server.port=8082
PS C:\Users\11102>java -jar D:\03.작업공간\02.hiyoon\SimpleRestApi\build\libs\SimpleRestApi-0.0.1-SNAPSHOT.jar --server.port=9091
PS C:\Users\11102>java -jar D:\03.작업공간\02.hiyoon\SimpleRestApi\build\libs\SimpleRestApi-0.0.1-SNAPSHOT.jar --server.port=9092
PS C:\Users\11102>java -jar D:\03.작업공간\02.hiyoon\ActuatorMonitor\build\libs\ActuatorMonitor-0.0.1-SNAPSHOT.jar
```

* [Local Actuator Admin](http://localhost:8000/actuator)


![Response_Time_Graph](webclient_Response_time_graph.PNG)
![Summary_Report](webclient_summary_report.PNG)


### Schedulers

Schedulers provides various Scheduler flavors usable by publishOn or subscribeOn

* parallel(): Optimized for fast Runnable non-blocking executions
* single(): Optimized for low-latency Runnable one-off executions
* elastic(): Optimized for longer executions, an alternative for blocking tasks where the number of active tasks (and
  threads) can grow indefinitely
* boundedElastic(): Optimized for longer executions, an alternative for blocking tasks where the number of active
  tasks (and threads) is capped
* immediate(): to immediately run submitted Runnable instead of scheduling them (somewhat of a no-op or "null object"
  Scheduler)
* fromExecutorService(ExecutorService) to create new instances around Executors

### Examples

1. 간단한 호출

WebClient의 GET 호출 예시입니다.

```java

public Mono<User> getUser(int id){
        LOG.info(String.format("Calling getUser(%d)",id));

        return webClient.get()
        .uri("/user/{id}",id)
        .retrieve()
        .bodyToMono(User.class);
}

```

2. 다른 서비스, 같은 타입

다른 서비스지만 반환 타입이 같은 경우에는 Flux.merge를 사용합니다.

```java

public Mono<User> getUser(int id){
        return webClient.get()
        .uri("/user/{id}",id)
        .retrieve()
        .bodyToMono(User.class);
}

public Mono<User> getOtherUser(int id){
        return webClient.get()
        .uri("/otheruser/{id}",id)
        .retrieve()
        .bodyToMono(User.class);
}

public Flux<User> fetchUserAndOtherUser(int id){
        return Flux.merge(getUser(id),getOtherUser(id))
        .parallel()
        .runOn(Schedulers.elastic())
        .ordered((u1,u2)->u2.id()-u1.id());
}
```

3. 다른 서비스, 다른 타입

별개의 End-Point이면서 반환 타입도 다른 경우에는 각각의 Mono를 셋팅하여 Mono.zip으로 처리합니다.

```java

public Mono fetchUserAndItem(int userId,int itemId){
        Mono<User> user=getUser(userId).subscribeOn(Schedulers.elastic());
        Mono<Item> item=getItem(itemId).subscribeOn(Schedulers.elastic());

        return Mono.zip(user,item,UserWithItem::new);
}
```

4. One More Thing...

동시호출과 순차호출이 Mix되어야하는 경우 Mono.then, Mono.zip으로 처리합니다.



### Test


### Additional Links

* [Spring WebClient 쉽게 이해하기](https://happycloud-lee.tistory.com/220)
* [Simultaneous Spring WebClient Calls](https://www.baeldung.com/spring-webclient-simultaneous-calls)
* [reactor.core.scheduler.Schedulers](https://projectreactor.io/docs/core/release/api/reactor/core/scheduler/Schedulers.html#elastic--)

