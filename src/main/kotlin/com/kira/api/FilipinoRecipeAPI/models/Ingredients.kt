package com.kira.api.FilipinoRecipeAPI.models

import jakarta.validation.constraints.NotEmpty

class Ingredients (
    @field:NotEmpty(message = "Main ingredients must not be empty")
    val main: List<String>,

    val aromatics: List<String>,
    val liquidsAndSeasonings: List<String>,
    val vegetables: List<String>,
    val optionalAddons: List<String>
)