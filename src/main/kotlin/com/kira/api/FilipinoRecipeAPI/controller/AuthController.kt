package com.kira.api.FilipinoRecipeAPI.controller

import com.kira.api.FilipinoRecipeAPI.security.AuthService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService,
) {
    data class RegistrationRequest(val email: String, val password: String, val username: String)

    data class LoginRequest(val email: String, val password: String)

    data class RefreshRequest(val refreshToken: String)

    @PostMapping("/register")
    fun register(
        @RequestBody body: RegistrationRequest,
    ) {
        authService.registerUser(body.email, body.password, body.username)
    }

    @PostMapping("/login")
    fun login(
        @RequestBody body: LoginRequest,
    ): AuthService.TokenPair {
        return authService.loginUser(body.email, body.password)
    }

    @PostMapping("/refresh")
    fun refresh(
        @RequestBody body: RefreshRequest,
    ): AuthService.TokenPair {
        return authService.refresh(body.refreshToken)
    }
}