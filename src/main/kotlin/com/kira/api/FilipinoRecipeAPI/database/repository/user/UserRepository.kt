package com.kira.api.FilipinoRecipeAPI.database.repository.user

import com.kira.api.FilipinoRecipeAPI.database.model.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<User, String> {
    fun findByUsername(username: String): User?
}