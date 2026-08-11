package com.kira.api.FilipinoRecipeAPI.dto.response

import com.kira.api.FilipinoRecipeAPI.model.Ingredients
import com.kira.api.FilipinoRecipeAPI.model.enums.Category
import com.kira.api.FilipinoRecipeAPI.model.enums.Difficulty
import com.kira.api.FilipinoRecipeAPI.model.enums.MealTime
import com.kira.api.FilipinoRecipeAPI.model.enums.Protein
import java.time.Instant

data class RecipeResponse(
    val id: String,
    val title: String,
    val description: String = "",
    val image: String,
    val estimatedMinutes: Int,
    val difficulty: Difficulty,
    val category: Category,
    val protein: Protein,
    val mealTime: MealTime,
    val ingredients: Ingredients,
    val steps: List<String>,
    val cookingTips: List<String> = emptyList(),
    val variations: List<String> = emptyList(),
    val servingSuggestions: List<String> = emptyList(),
    val isFavorited: Boolean = false,
    val createdAt: Instant,
    val updatedAt: Instant,
    val published: Boolean
)