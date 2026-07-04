package com.kira.api.FilipinoRecipeAPI.models.requests

data class VerifyOtpRequest(
    val email: String,
    val otp: String
)