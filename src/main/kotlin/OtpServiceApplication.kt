package org.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.reactive.config.EnableWebFlux


@EnableWebFlux  // Ensures WebFlux is enabled
@SpringBootApplication(scanBasePackages = ["org.example"])
class OtpServiceApplication

fun main(args: Array<String>) {
    runApplication<OtpServiceApplication>(*args)
}
