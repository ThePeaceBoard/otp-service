package org.example.config
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity.AuthorizeExchangeSpec
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebFluxSecurity
class SecurityConfig {

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http
            .csrf { csrf: CsrfSpec -> csrf.disable() } // Disable CSRF for stateless services
            .authorizeExchange { exchanges: AuthorizeExchangeSpec ->
                exchanges
                    .pathMatchers("/api/otp/**").permitAll()  // Allow all OTP API calls
                    .anyExchange().authenticated()
            } // Require authentication for all requests

            .httpBasic(withDefaults()) // Enable Basic Auth with defaults
            .build()
    }

    @Bean
    fun userDetailsService(): MapReactiveUserDetailsService {
        val encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()

        // Define a user with username 'user' and password 'password'
        val user: UserDetails = User.withUsername("admin")
            .password(encoder.encode("password"))
            .roles("USER")
            .build()

        // Add more users as needed
        return MapReactiveUserDetailsService(user)
    }
}