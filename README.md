# Webclient

### What Is the WebClient?
Simply put, WebClient is an interface representing the main entry point for performing web requests.
It was created as part of the Spring Web Reactive module, and will be replacing the classic RestTemplate in these scenarios. In addition, the new client is a reactive, non-blocking solution that works over the HTTP/1.1 protocol.
It's important to note that even though it is, in fact, a non-blocking client and it belongs to the spring-webflux library, the solution offers support for both synchronous and asynchronous operations, making it suitable also for applications running on a Servlet Stack.
This can be achieved by blocking the operation to obtain the result. Of course, this practice is not suggested if we're working on a Reactive Stack.
Finally, the interface has a single implementation, the DefaultWebClient class, which we'll be working with.

https://happycloud-lee.tistory.com/220

### RestTemplate vs WebClient


java -jar D:\03.작업공간\02.hiyoon\WebClientExample\build\libs\WebClientExample-0.0.1-SNAPSHOT.jar --server.port=8081
java -jar D:\03.작업공간\02.hiyoon\WebClientExample\build\libs\WebClientExample-0.0.1-SNAPSHOT.jar --server.port=8082


### Reference Documentation

For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.5.4/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.5.4/gradle-plugin/reference/html/#build-image)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/2.5.4/reference/htmlsingle/#using-boot-devtools)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.5.4/reference/htmlsingle/#boot-features-developing-web-applications)

### Guides

The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)

### Additional Links

These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

