package com.example.adk.config;

import com.example.adk.agent.HelloAgent;
import com.google.adk.runner.InMemoryRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdkConfig {

    @Bean
    public InMemoryRunner adkRunner() {
        return new InMemoryRunner(HelloAgent.ROOT_AGENT);
    }


}

