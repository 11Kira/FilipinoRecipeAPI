package com.kira.api.FilipinoRecipeAPI.models.response

import com.kira.api.FilipinoRecipeAPI.models.Ingredients
import com.kira.api.FilipinoRecipeAPI.models.enums.Category
import com.kira.api.FilipinoRecipeAPI.models.enums.Difficulty
import java.time.Instant

data class RecipeResponse(
    val id: String,
    val title: String,
    val description: String = "",
    val image: String,
    val estimatedMinutes: Int,
    val difficulty: Difficulty,
    val category: Category,
    val ingredients: Ingredients,
    val steps: List<String>,
    val cookingTips: List<String> = emptyList(),
    val variations: List<String> = emptyList(),
    val servingSuggestions: List<String> = emptyList(),
    val createdAt: Instant,
    val updatedAt: Instant,
    val published: Boolean
)