package com.kira.api.FilipinoRecipeAPI.dto.requests

data class ResetPasswordRequest(
    val email: String,
    val resetToken: String,
    val newPassword: CharSequence
)