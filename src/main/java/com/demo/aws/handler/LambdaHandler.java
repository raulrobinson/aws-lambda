package com.demo.aws.handler;

import com.demo.aws.service.LambdaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class LambdaHandler {

    private final LambdaService lambdaService;

    public Mono<ServerResponse> getParameter(ServerRequest request) {
        String name = request.pathVariable("name");

        return lambdaService.invokeGetParameter(name)
                .flatMap(json -> ServerResponse.ok().bodyValue(json));
    }
}

