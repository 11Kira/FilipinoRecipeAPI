package com.kira.api.FilipinoRecipeAPI.database.repository.password

import com.kira.api.FilipinoRecipeAPI.database.model.PasswordResetOtp
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface PasswordResetOtpRepository : MongoRepository<PasswordResetOtp, String> {
    fun findByEmail(email: String): PasswordResetOtp?
    fun deleteByEmail(email: String)
}