package com.kira.api.FilipinoRecipeAPI.service

import com.kira.api.FilipinoRecipeAPI.dto.response.UserResponse
import com.kira.api.FilipinoRecipeAPI.exception.ResourceNotFoundException
import com.kira.api.FilipinoRecipeAPI.mapper.UserMapper
import com.kira.api.FilipinoRecipeAPI.repository.user.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper
) {
    fun getUser(userId: String): UserResponse {
        val user = userRepository.findById(userId)
            .orElseThrow { ResourceNotFoundException("User not found") }
        return userMapper.toResponse(user)
    }
}