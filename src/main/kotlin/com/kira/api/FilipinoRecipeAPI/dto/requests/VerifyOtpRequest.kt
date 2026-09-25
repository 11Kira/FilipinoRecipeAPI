package com.kira.api.FilipinoRecipeAPI.dto.requests

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class VerifyOtpRequest(
    @field:Email val email: String,
    @field:NotBlank val otp: String
)