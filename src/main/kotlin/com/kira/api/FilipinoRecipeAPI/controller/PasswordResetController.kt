package com.kira.api.FilipinoRecipeAPI.controller

import com.kira.api.FilipinoRecipeAPI.models.requests.ForgotPasswordRequest
import com.kira.api.FilipinoRecipeAPI.models.requests.ResetPasswordRequest
import com.kira.api.FilipinoRecipeAPI.models.requests.VerifyOtpRequest
import com.kira.api.FilipinoRecipeAPI.models.response.ApiResponse
import com.kira.api.FilipinoRecipeAPI.models.response.OtpVerificationResponse
import com.kira.api.FilipinoRecipeAPI.service.PasswordResetService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/auth")
class PasswordResetController(private val resetService: PasswordResetService) {

    @PostMapping("/forgot-password")
    fun forgotPassword(@RequestBody request: ForgotPasswordRequest): ResponseEntity<ApiResponse<Unit>> {
        val result = resetService.initiatePasswordReset(request)
        return ResponseEntity.ok(result)
    }

    @PostMapping("/verify-otp")
    fun verifyOtp(@RequestBody request: VerifyOtpRequest): ResponseEntity<ApiResponse<OtpVerificationResponse>> {
        val result = resetService.validateOtpCode(request)
        return ResponseEntity.ok(result)
    }

    @PostMapping("/reset-password")
    fun resetPassword(@RequestBody request: ResetPasswordRequest): ResponseEntity<ApiResponse<Unit>> {
        val result = resetService.completePasswordReset(request)
        return ResponseEntity.ok(result)
    }
}