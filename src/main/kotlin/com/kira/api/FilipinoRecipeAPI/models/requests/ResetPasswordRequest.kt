package com.kira.api.FilipinoRecipeAPI.models.requests

data class ResetPasswordRequest(
    val email: String,
    val resetToken: String,
    val newPassword: CharSequence
)