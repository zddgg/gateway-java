package com.gateway;

import com.gateway.bootstrap.GatewayBootstrapApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@SpringBootTest(classes = GatewayBootstrapApplication.class)
public class TestApplication {

   @Test
    void test() {
       WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec = WebClient.create("http://localhost:9195/http/test/findByUserId?userId=12").get();
       Mono<Object> objectMono = requestHeadersUriSpec.retrieve().bodyToMono(Object.class);
       Object block = objectMono.block();
       System.out.println(block);
   }
}
