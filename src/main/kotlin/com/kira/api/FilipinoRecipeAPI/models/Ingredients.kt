package com.kira.api.FilipinoRecipeAPI.models

import jakarta.validation.constraints.NotEmpty

data class Ingredients(
    @field:NotEmpty(message = "Main ingredients must not be empty")
    val main: List<String>,

    val aromatics: List<String> = emptyList(),
    val liquidsAndSeasonings: List<String> = emptyList(),
    val vegetables: List<String> = emptyList(),
    val optionalAddons: List<String> = emptyList()
)