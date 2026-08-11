package com.kira.api.FilipinoRecipeAPI.repository.token

import com.kira.api.FilipinoRecipeAPI.model.RefreshToken
import org.springframework.data.mongodb.repository.MongoRepository

interface RefreshTokenRepository : MongoRepository<RefreshToken, String> {
    fun findByHashedToken(hashedToken: String): RefreshToken?
    fun findByUserIdAndHashedToken(userId: String, hashedToken: String): RefreshToken?
    fun deleteByUserIdAndHashedToken(userId: String, hashedToken: String)
    fun deleteByUserId(userId: String)
    fun deleteByHashedToken(hashedToken: String)
}