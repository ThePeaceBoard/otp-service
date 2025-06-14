package org.example.config

import dev.miku.r2dbc.mysql.MySqlConnectionConfiguration
import dev.miku.r2dbc.mysql.MySqlConnectionFactory
import io.r2dbc.spi.ConnectionFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import java.time.Duration
import java.time.ZoneId

@Configuration
class R2dbcConfiguration(
    @Value("\${spring.r2dbc.host}") private val host: String,
    @Value("\${spring.r2dbc.port}") private val port: Int,
    @Value("\${spring.r2dbc.scheme}") private val scheme: String,
    @Value("\${spring.r2dbc.username}") private val username: String,
    @Value("\${spring.r2dbc.password}") private val password: String,
    ) : AbstractR2dbcConfiguration() {

    @Bean
    override fun connectionFactory(): ConnectionFactory {
        return MySqlConnectionFactory.from(
            MySqlConnectionConfiguration.builder()
                .host(host)
                .port(port)
                .username(username)
                .password(password)
                .database(scheme)
                .serverZoneId(ZoneId.of("UTC"))
                .connectTimeout(Duration.ofSeconds(3))
                .useServerPrepareStatement()
                .build()
        )
    }
}