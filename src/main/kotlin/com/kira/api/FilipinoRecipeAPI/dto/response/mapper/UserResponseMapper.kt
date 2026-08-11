package com.kira.api.FilipinoRecipeAPI.dto.response.mapper

import com.kira.api.FilipinoRecipeAPI.model.User
import com.kira.api.FilipinoRecipeAPI.model.response.UserResponse

fun User.toResponse(): UserResponse =
    UserResponse(
        id = this.id ?: "",
        username = username,
        email = email,
        role = role.name
    )