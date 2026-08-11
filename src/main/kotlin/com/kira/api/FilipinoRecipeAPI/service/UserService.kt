package com.kira.api.FilipinoRecipeAPI.service

import com.kira.api.FilipinoRecipeAPI.exception.ResourceNotFoundException
import com.kira.api.FilipinoRecipeAPI.model.response.UserResponse
import com.kira.api.FilipinoRecipeAPI.model.response.mapper.toResponse
import com.kira.api.FilipinoRecipeAPI.repository.user.UserRepository
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