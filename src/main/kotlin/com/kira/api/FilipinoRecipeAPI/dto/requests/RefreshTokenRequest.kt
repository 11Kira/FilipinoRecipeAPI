package com.kira.api.FilipinoRecipeAPI.dto.requests

import jakarta.validation.constraints.NotBlank

data class RefreshTokenRequest(
    @field:NotBlank val refreshToken: String
)