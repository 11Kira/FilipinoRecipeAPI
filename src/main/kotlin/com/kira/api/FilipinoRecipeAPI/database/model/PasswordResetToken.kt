package com.kira.api.FilipinoRecipeAPI.database.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "password_reset_otps")
data class PasswordResetOtp(
    @Id val id: String? = null,
    @Indexed val email: String,
    val otp: String,
    val resetToken: String? = null,

    @Indexed(expireAfter = "5m")
    val createdAt: Instant = Instant.now()
)