package com.kira.api.FilipinoRecipeAPI.controller

import com.kira.api.FilipinoRecipeAPI.models.enums.ResponseStatus
import com.kira.api.FilipinoRecipeAPI.models.requests.LoginRequest
import com.kira.api.FilipinoRecipeAPI.models.requests.LogoutRequest
import com.kira.api.FilipinoRecipeAPI.models.requests.RefreshRequest
import com.kira.api.FilipinoRecipeAPI.models.requests.RegistrationRequest
import com.kira.api.FilipinoRecipeAPI.models.response.ApiResponse
import com.kira.api.FilipinoRecipeAPI.security.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService,
) {


    @PostMapping("/register")
    fun register(@RequestBody body: RegistrationRequest): ResponseEntity<ApiResponse<Unit>> {
        authService.registerUser(body.email, body.password, body.username)
        return ResponseEntity.ok(
            ApiResponse(ResponseStatus.SUCCESS, "User registered successfully", null)
        )
    }

    @PostMapping("/login")
    fun login(@RequestBody body: LoginRequest): ResponseEntity<ApiResponse<AuthService.TokenPair>> {
        val tokens = authService.loginUser(body.email, body.password)
        return ResponseEntity.ok(
            ApiResponse(ResponseStatus.SUCCESS, "Login successful", tokens)
        )
    }

    @PostMapping("/refresh")
    fun refresh(@RequestBody body: RefreshRequest): ResponseEntity<ApiResponse<AuthService.TokenPair>> {
        val tokens = authService.refresh(body.refreshToken)
        return ResponseEntity.ok(
            ApiResponse(ResponseStatus.SUCCESS, "Token refreshed", tokens)
        )
    }

    @PostMapping("/logout")
    fun logout(@RequestBody request: LogoutRequest): ResponseEntity<ApiResponse<Unit>> {
        authService.revokeToken(request.refreshToken)
        return ResponseEntity.ok(
            ApiResponse(ResponseStatus.SUCCESS, "Logout successful", null)
        )
    }
}