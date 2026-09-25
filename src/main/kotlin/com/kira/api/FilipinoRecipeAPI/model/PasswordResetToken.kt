package com.kira.api.FilipinoRecipeAPI.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "password_reset_tokens")
data class PasswordResetToken(
    @Id val id: String? = null,
    val email: String,
    val otp: String,
    val resetToken: String? = null,
    val isVerified: Boolean = false,

    @Indexed(expireAfter = "15m")
    val createdAt: Instant
)