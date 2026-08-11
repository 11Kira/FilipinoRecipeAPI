package com.kira.api.FilipinoRecipeAPI.controller

import com.kira.api.FilipinoRecipeAPI.dto.requests.LoginRequest
import com.kira.api.FilipinoRecipeAPI.dto.requests.LogoutRequest
import com.kira.api.FilipinoRecipeAPI.dto.requests.RefreshTokenRequest
import com.kira.api.FilipinoRecipeAPI.dto.requests.RegistrationRequest
import com.kira.api.FilipinoRecipeAPI.dto.response.ApiResponse
import com.kira.api.FilipinoRecipeAPI.dto.response.AuthResponse
import com.kira.api.FilipinoRecipeAPI.dto.response.RefreshTokenResponse
import com.kira.api.FilipinoRecipeAPI.model.enums.ResponseStatus
import com.kira.api.FilipinoRecipeAPI.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/register")
    fun register(@Valid @RequestBody request: RegistrationRequest): ResponseEntity<ApiResponse<AuthResponse>> {
        val response = authService.registerUser(request)
        return ResponseEntity.ok(
            ApiResponse(ResponseStatus.SUCCESS, "User registered successfully", response)
        )
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<ApiResponse<AuthResponse>> {
        val response = authService.loginUser(request)
        return ResponseEntity.ok(
            ApiResponse(ResponseStatus.SUCCESS, "Login successful", response)
        )
    }

    @PostMapping("/refresh")
    fun refresh(@Valid @RequestBody request: RefreshTokenRequest): ResponseEntity<ApiResponse<RefreshTokenResponse>> {
        val response = authService.refreshToken(request)
        return ResponseEntity.ok(
            ApiResponse(ResponseStatus.SUCCESS, "Token refreshed", response)
        )
    }

    @PostMapping("/logout")
    fun logout(@RequestBody request: LogoutRequest): ResponseEntity<ApiResponse<Unit>> {
        authService.revokeToken(request.refreshToken)
        return ResponseEntity.ok(
            ApiResponse(ResponseStatus.SUCCESS, "Logout successful", Unit)
        )
    }
}