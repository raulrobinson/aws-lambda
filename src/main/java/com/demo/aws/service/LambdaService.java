package com.demo.aws.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.lambda.LambdaAsyncClient;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class LambdaService {

    private final LambdaAsyncClient lambdaAsyncClient;
    private final ObjectMapper objectMapper;

    public Mono<Map<String, Object>> invokeGetParameter(String name) {

        String payload = "{\"param\":\"" + name + "\"}";

        InvokeRequest request = InvokeRequest.builder()
                .functionName("ban-xrs-get-parameter")
                .payload(SdkBytes.fromUtf8String(payload))
                .build();

        return Mono.fromFuture(lambdaAsyncClient.invoke(request))
                .flatMap(response -> {
                    String payloadString = response.payload().asUtf8String();

                    return Mono.fromCallable(() ->
                            objectMapper.readValue(payloadString,
                                    new TypeReference<Map<String, Object>>() {})
                    );
                })
                .flatMap(lambdaResponse -> Mono.fromCallable(() -> {
                    if (lambdaResponse.containsKey("body")) {
                        String bodyString = (String) lambdaResponse.get("body");
                        Map<String, Object> bodyJson = objectMapper.readValue(
                                bodyString,
                                new TypeReference<>() {}
                        );
                        lambdaResponse.put("body", bodyJson);
                    }

                    return lambdaResponse;
                }));
    }
}

