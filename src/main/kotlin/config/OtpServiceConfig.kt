package org.example.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@EntityScan(basePackages = ["org.example.entity"])
@Configuration
class OtpServiceConfig {

    @Bean
    fun webClient(): WebClient {
        println("Creating WebClient Bean")
        return WebClient.builder().build()
    }

}
