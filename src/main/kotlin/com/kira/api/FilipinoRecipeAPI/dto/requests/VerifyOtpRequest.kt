package com.kira.api.FilipinoRecipeAPI.dto.requests

data class VerifyOtpRequest(
    val email: String,
    val otp: String
)