package com.kira.api.FilipinoRecipeAPI.database.model

import com.kira.api.FilipinoRecipeAPI.models.enums.Role
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document("users") // Explicitly names the collection "users"
data class User(
    @Id
    val id: String? = null,

    @Indexed(unique = true) // This tells Spring to handle the unique index for you
    val username: String,
    val email: String,
    val hashedPassword: String,
    val roles: Role = Role.ROLE_ADMIN,
    val favoriteRecipeIds: List<String> = emptyList()
)