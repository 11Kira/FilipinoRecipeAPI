package com.kira.api.FilipinoRecipeAPI.models.requests

import jakarta.validation.constraints.NotBlank

data class RegistrationRequest(
    @field:NotBlank(message = "Username can't be blank.")
    val username: String,
    @field:NotBlank(message = "Email can't be blank.")
    val email: String,
    @field:NotBlank(message = "Password can't be blank.")
    val password: String,
)