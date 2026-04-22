package com.kira.api.FilipinoRecipeAPI.service

import com.kira.api.FilipinoRecipeAPI.database.repository.user.UserRepository
import com.kira.api.FilipinoRecipeAPI.models.exception.ResourceNotFoundException
import com.kira.api.FilipinoRecipeAPI.models.response.UserResponse
import com.kira.api.FilipinoRecipeAPI.models.response.mapper.toResponse
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {
    fun getUser(userId: String): UserResponse {
        val user = userRepository.findById(userId)
            .orElseThrow { ResourceNotFoundException("User not found") }
        return user.toResponse()
    }
}