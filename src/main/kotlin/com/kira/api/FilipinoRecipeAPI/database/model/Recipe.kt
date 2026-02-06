package com.kira.api.FilipinoRecipeAPI.database.model

import com.kira.api.FilipinoRecipeAPI.models.Ingredients
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("recipes")
data class Recipe(
    @Id
    val id: String? = null,
    val title: String = "",
    val description: String = "",
    val image: String = "",
    val ingredients: Ingredients,
    val steps: List<String> = emptyList(),
    val cookingTips: List<String> = emptyList(),
    val variations: List<String> = emptyList(),
    val servingSuggestions: List<String> = emptyList(),
    val createdAt: Instant,
    val updatedAt: Instant,
    val published: Boolean = false
)