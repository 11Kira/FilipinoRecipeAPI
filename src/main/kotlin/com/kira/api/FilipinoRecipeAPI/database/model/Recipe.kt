package com.kira.api.FilipinoRecipeAPI.database.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("recipes")
data class Recipe(
    @Id
    val id: String? = null,
    val image: String = "",
    val name: String = "",
    val description: String = "",
    val ingredients: List<String> = emptyList(),
    val instructions: List<String> = emptyList(),
    val tips: List<String> = emptyList(),
    val createdAt: Instant
)