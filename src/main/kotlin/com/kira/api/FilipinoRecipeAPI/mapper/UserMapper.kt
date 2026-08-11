package com.kira.api.FilipinoRecipeAPI.mapper

import com.kira.api.FilipinoRecipeAPI.dto.response.UserResponse
import com.kira.api.FilipinoRecipeAPI.model.User
import org.springframework.stereotype.Component

@Component
class UserMapper {
    fun toResponse(user: User): UserResponse {
        return UserResponse(
            id = user.id,
            username = user.username,
            email = user.email,
            role = user.role.name
        )
    }
}