package ca.gbc.comp3095.event.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient wellnessWebClient() {
        // base URL inside Docker network (service name from docker-compose)
        return WebClient.builder()
                .baseUrl("http://wellness-resource-service:8081")
                .build();
    }
}
