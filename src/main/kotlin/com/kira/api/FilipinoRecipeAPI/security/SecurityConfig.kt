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
            .csrf { csrf -> csrf.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { auth ->
                auth.requestMatchers("/api/auth/**").permitAll()
                auth.requestMatchers(HttpMethod.POST, "/api/users/favorites/**").authenticated()
                auth.requestMatchers(HttpMethod.GET, "/api/recipes/**").permitAll()
                auth.requestMatchers(HttpMethod.POST, "/api/recipes/**").hasRole("ADMIN")
                auth.requestMatchers(HttpMethod.PUT, "/api/recipes/**").hasRole("ADMIN")
                auth.requestMatchers(HttpMethod.PATCH, "/api/recipes/**").hasRole("ADMIN")
                auth.requestMatchers(HttpMethod.DELETE, "/api/recipes/**").hasRole("ADMIN")
                auth.anyRequest().authenticated()
            }
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }
}