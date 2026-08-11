package com.kira.api.FilipinoRecipeAPI.repository.password

import com.kira.api.FilipinoRecipeAPI.model.PasswordResetOtp
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface PasswordResetOtpRepository : MongoRepository<PasswordResetOtp, String> {
    fun findByEmail(email: String): PasswordResetOtp?
    fun deleteByEmail(email: String)
}