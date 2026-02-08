package com.kira.api.FilipinoRecipeAPI.models.requests

import com.kira.api.FilipinoRecipeAPI.models.Ingredients
import com.kira.api.FilipinoRecipeAPI.models.enums.Category
import com.kira.api.FilipinoRecipeAPI.models.enums.Difficulty
import com.kira.api.FilipinoRecipeAPI.models.enums.MealTime
import com.kira.api.FilipinoRecipeAPI.models.enums.Protein
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import org.jetbrains.annotations.NotNull

data class RecipeRequest(
    @field:NotBlank(message = "Title can't be blank.")
    val title: String,
    val description: String = "",
    @field:NotBlank(message = "Image can't be blank.")
    val image: String,
    @field:NotNull
    val estimatedMinutes: Int,
    @field:NotNull
    val difficulty: Difficulty,
    @field:NotNull
    val category: Category,
    @field:NotNull
    val protein: Protein,
    @field:NotNull
    val mealTime: MealTime,
    val ingredients: Ingredients,
    @field:NotEmpty(message = "Steps can't be blank.")
    val steps: List<String>,
    val cookingTips: List<String> = emptyList(),
    val variations: List<String> = emptyList(),
    val servingSuggestions: List<String> = emptyList()
)