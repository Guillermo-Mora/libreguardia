package com.libreguardia.api.config

import com.libreguardia.api.security.UserAppDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfiguration {
    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder(10)

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .authorizeHttpRequests {
                it
                    .requestMatchers("/login", "/error").permitAll()
                    .anyRequest().authenticated()
            }
            .formLogin {
                it
                    .defaultSuccessUrl("/home", true)
                    .permitAll()
            }
            .logout {
                it
                    .logoutSuccessUrl("/login?logout")
            }
            .build()
    }

    @Bean
    fun authenticationProvider(
        userDetailsService: UserAppDetailsService,
        passwordEncoder: PasswordEncoder
    ): DaoAuthenticationProvider {
        val provider = DaoAuthenticationProvider(userDetailsService)
        provider.setPasswordEncoder(passwordEncoder)
        return provider
    }
}