package com.kira.api.FilipinoRecipeAPI.models.requests.patch

data class IngredientsPatchRequest(
    val main: List<String>? = null,
    val aromatics: List<String>? = null,
    val liquidsAndSeasonings: List<String>? = null,
    val vegetables: List<String>? = null,
    val optionalAddons: List<String>? = null
)