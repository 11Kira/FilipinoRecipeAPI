package com.kira.api.FilipinoRecipeAPI.controller

import com.kira.api.FilipinoRecipeAPI.dto.requests.ForgotPasswordRequest
import com.kira.api.FilipinoRecipeAPI.dto.requests.ResetPasswordRequest
import com.kira.api.FilipinoRecipeAPI.dto.requests.VerifyOtpRequest
import com.kira.api.FilipinoRecipeAPI.dto.response.ApiResponse
import com.kira.api.FilipinoRecipeAPI.dto.response.OtpVerificationResponse
import com.kira.api.FilipinoRecipeAPI.model.enums.ResponseStatus
import com.kira.api.FilipinoRecipeAPI.service.PasswordResetService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/auth")
class PasswordResetController(private val passwordResetService: PasswordResetService) {

    @PostMapping("/forgot-password")
    fun forgotPassword(@Valid @RequestBody request: ForgotPasswordRequest): ResponseEntity<ApiResponse<Unit>> {
        passwordResetService.initiatePasswordReset(request)
        return ResponseEntity.ok(
            ApiResponse(
                status = ResponseStatus.SUCCESS,
                message = "If an account exists with this email, reset instructions have been sent.",
                data = Unit
            )
        )
    }

    @PostMapping("/verify-otp")
    fun verifyOtp(@Valid @RequestBody request: VerifyOtpRequest): ResponseEntity<ApiResponse<OtpVerificationResponse>> {
        val response = passwordResetService.validateOtpCode(request)
        return ResponseEntity.ok(
            ApiResponse(
                status = ResponseStatus.SUCCESS,
                message = "Code verified successfully.",
                data = response
            )
        )
    }

    @PostMapping("/reset-password")
    fun resetPassword(@Valid @RequestBody request: ResetPasswordRequest): ResponseEntity<ApiResponse<Unit>> {
        passwordResetService.completePasswordReset(request)
        return ResponseEntity.ok(
            ApiResponse(
                status = ResponseStatus.SUCCESS,
                message = "Your password has been successfully reset.",
                data = Unit
            )
        )
    }
}