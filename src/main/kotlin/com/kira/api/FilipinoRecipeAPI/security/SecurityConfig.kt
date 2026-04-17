package com.kira.api.FilipinoRecipeAPI.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthFilter: JwtAuthFilter
) {
    @Bean
    fun filterChain(
        httpSecurity: HttpSecurity
    ): SecurityFilterChain {
        return httpSecurity
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { auth ->
                // Public
                auth.requestMatchers("/api/auth/**").permitAll()
                auth.requestMatchers("/error").permitAll()

                // Needs authentication
                auth.requestMatchers(HttpMethod.GET, "/api/recipes/**").authenticated()

                // Admin only POST/PUT/PATCH/DELETE
                auth.requestMatchers("/api/recipes/**").hasRole("ADMIN")

                // Needs authentication
                auth.requestMatchers("/api/users/**").authenticated()

                // Else
                auth.anyRequest().authenticated()
            }
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }
}