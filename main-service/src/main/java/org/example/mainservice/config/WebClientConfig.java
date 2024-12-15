package org.example.mainservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${kudago.url}")
    private String kudagoUrl;
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(kudagoUrl)
                .build();
    }
}
