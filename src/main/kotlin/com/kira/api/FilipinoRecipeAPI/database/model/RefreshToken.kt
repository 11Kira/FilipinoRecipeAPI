package com.kira.api.FilipinoRecipeAPI.database.model

import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("refresh_tokens")
data class RefreshToken(
    val userId: String,
    @Indexed(expireAfter = "0s")
    val expiresAt: Instant,
    val hashedToken: String,
    val createdAt: Instant = Instant.now(),
)