package com.kira.api.FilipinoRecipeAPI.database.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("recipes")
data class Recipe(
    @Id
    val id: ObjectId = ObjectId(),
    val image: String = "",
    val name: String = "",
    val description: String = "",
    val ingredients: List<String> = emptyList(),
    val instructions: List<String> = emptyList(),
    val notes: List<String> = emptyList()
)