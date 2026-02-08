package com.kira.api.FilipinoRecipeAPI.models.requests.patch

import com.kira.api.FilipinoRecipeAPI.models.enums.Category
import com.kira.api.FilipinoRecipeAPI.models.enums.Difficulty
import com.kira.api.FilipinoRecipeAPI.models.enums.MealTime
import com.kira.api.FilipinoRecipeAPI.models.enums.Protein

data class RecipePatchRequest(
    val title: String? = null,
    val description: String? = null,
    val image: String? = null,
    val estimatedMinutes: Int? = null,
    val difficulty: Difficulty? = null,
    val category: Category? = null,
    val protein: Protein? = null,
    val mealTime: MealTime? = null,
    val ingredients: IngredientsPatchRequest? = null,
    val steps: List<String>? = null,
    val cookingTips: List<String>? = null,
    val variations: List<String>? = null,
    val servingSuggestions: List<String>? = null,
)