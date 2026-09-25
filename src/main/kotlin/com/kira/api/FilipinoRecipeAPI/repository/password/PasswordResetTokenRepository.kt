package com.kira.api.FilipinoRecipeAPI.repository.password

import com.kira.api.FilipinoRecipeAPI.model.PasswordResetToken
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface PasswordResetTokenRepository : MongoRepository<PasswordResetToken, String> {
    fun findByEmail(email: String): Optional<PasswordResetToken>
    fun findByResetToken(resetToken: String): Optional<PasswordResetToken>
}