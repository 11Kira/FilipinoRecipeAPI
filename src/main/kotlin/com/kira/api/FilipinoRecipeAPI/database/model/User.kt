package com.kira.api.FilipinoRecipeAPI.database.model

import com.kira.api.FilipinoRecipeAPI.models.enums.Role
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document("users")
data class User(
    @Id
    val id: String? = null,

    @Indexed(unique = true)
    val username: String,
    val email: String,
    val hashedPassword: String,
    val role: Role,
    val favoriteRecipeIds: List<String>? = emptyList()
)