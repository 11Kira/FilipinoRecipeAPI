package com.kira.api.FilipinoRecipeAPI.database.repository.token

import com.kira.api.FilipinoRecipeAPI.database.model.RefreshToken
import org.springframework.data.mongodb.repository.MongoRepository

interface RefreshTokenRepository : MongoRepository<RefreshToken, String> {
    fun findByUserIdAndHashedToken(userId: String, hashedToken: String): RefreshToken?
    fun deleteByUserIdAndHashedToken(userId: String, hashedToken: String)
    fun deleteByUserId(userId: String)
}