package com.kira.api.FilipinoRecipeAPI.models.requests.patch

import com.kira.api.FilipinoRecipeAPI.models.enums.Category
import com.kira.api.FilipinoRecipeAPI.models.enums.Difficulty

data class RecipePatchRequest(
    val title: String? = null,
    val description: String? = null,
    val image: String? = null,
    val estimatedMinutes: Int? = null,
    val difficulty: Difficulty? = null,
    val category: Category? = null,
    val ingredients: IngredientsPatchRequest? = null,
    val steps: List<String>? = null,
    val cookingTips: List<String>? = null,
    val variations: List<String>? = null,
    val servingSuggestions: List<String>? = null,
)