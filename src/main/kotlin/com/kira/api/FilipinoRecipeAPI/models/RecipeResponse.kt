package com.kira.api.FilipinoRecipeAPI.models

import java.time.Instant

data class RecipeResponse(
    val id: String,
    val title: String,
    val description: String,
    val image: String,
    val estimatedMinutes: Int,
    val difficulty: Difficulty,
    val category: Category,
    val ingredients: Ingredients,
    val steps: List<String>,
    val cookingTips: List<String>,
    val variations: List<String>,
    val servingSuggestions: List<String>,
    val createdAt: Instant,
    val updatedAt: Instant,
    val published: Boolean
)