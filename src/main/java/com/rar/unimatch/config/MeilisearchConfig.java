package com.rar.unimatch.config;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MeilisearchConfig {
    @Value("${meilisearch.host}")
    private String host;
    
    @Value("${meilisearch.api-key}")
    private String apiKey;
    
    @Bean
    public Client meilisearchClient() {
        Config config = new Config(host, apiKey);
        return new Client(config);
    }
}
