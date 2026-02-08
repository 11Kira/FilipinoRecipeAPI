package com.kira.api.FilipinoRecipeAPI.database.model

import com.kira.api.FilipinoRecipeAPI.models.Ingredients
import com.kira.api.FilipinoRecipeAPI.models.enums.Category
import com.kira.api.FilipinoRecipeAPI.models.enums.Difficulty
import com.kira.api.FilipinoRecipeAPI.models.enums.MealTime
import com.kira.api.FilipinoRecipeAPI.models.enums.Protein
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("recipes")
data class Recipe(
    @Id
    val id: String? = null,
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
    val createdAt: Instant,
    val updatedAt: Instant,
    val published: Boolean = false
)