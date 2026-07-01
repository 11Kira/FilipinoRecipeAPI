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
            .exceptionHandling { exception ->
                exception.authenticationEntryPoint { request, response, authException ->
                    response.sendError(
                        jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED,
                        authException.message
                    )
                }
            }
            .authorizeHttpRequests { auth ->
                // Public: Everyone can view recipes
                auth.requestMatchers("/v1/auth/**").permitAll()
                auth.requestMatchers("/error").permitAll()
                auth.requestMatchers(HttpMethod.GET, "/v1/recipes/**").permitAll()

                // Admin only for modifications
                auth.requestMatchers(HttpMethod.POST, "/v1/recipes/**").hasRole("ADMIN")
                auth.requestMatchers(HttpMethod.PUT, "/v1/recipes/**").hasRole("ADMIN")
                auth.requestMatchers(HttpMethod.PATCH, "/v1/recipes/**").hasRole("ADMIN")
                auth.requestMatchers(HttpMethod.DELETE, "/v1/recipes/**").hasRole("ADMIN")

                // Users and authenticated routes
                auth.requestMatchers("/v1/users/**").authenticated()
                auth.anyRequest().authenticated()
            }
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }
}