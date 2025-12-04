package com.demo.aws.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.regions.providers.DefaultAwsRegionProviderChain;
import software.amazon.awssdk.services.lambda.LambdaAsyncClient;

import java.net.URI;

@Configuration
public class AwsConfig {

    @Bean
    @Profile({"local", "test"})
    public LambdaAsyncClient lambdaAsyncClient(
            @Value("${aws.region}") String region
    ) {
        return LambdaAsyncClient.builder()
                .region(Region.of(region))
                .endpointOverride(URI.create("http://localhost:4566"))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create("test", "test")
                ))
                .build();
    }

    @Bean
    @Profile({"poc", "dev"})
    public LambdaAsyncClient lambdaAsyncClientPoc(
            @Value("${aws.profile.name}") String awsProfile,
            @Value("${aws.region}") String region
    ) {
        return LambdaAsyncClient.builder()
                .region(Region.of(region))
                .credentialsProvider(ProfileCredentialsProvider.builder()
                        .profileName(awsProfile)
                        .build())
                .build();
    }

    @Bean
    @Profile({"prod"})
    public LambdaAsyncClient lambdaAsyncClientProd() {
        Region region = DefaultAwsRegionProviderChain.builder().build().getRegion();
        return LambdaAsyncClient.builder()
                .region(region)
                .build();
    }
}
