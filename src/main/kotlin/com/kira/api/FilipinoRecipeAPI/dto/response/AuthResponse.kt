package com.kira.api.FilipinoRecipeAPI.dto.response

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
)