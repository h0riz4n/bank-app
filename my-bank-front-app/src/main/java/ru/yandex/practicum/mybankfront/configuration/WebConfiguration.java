package ru.yandex.practicum.mybankfront.configuration;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class WebConfiguration {

    @Bean
    public RestClient restClient(@Value("${my-front-app.base-path}") String basePath) {
        return RestClient.builder()
            .baseUrl(basePath)
            .build();
    }
}
