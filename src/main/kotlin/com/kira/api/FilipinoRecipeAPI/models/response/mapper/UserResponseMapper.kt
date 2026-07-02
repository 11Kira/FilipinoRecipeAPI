package com.kira.api.FilipinoRecipeAPI.models.response.mapper

import com.kira.api.FilipinoRecipeAPI.database.model.User
import com.kira.api.FilipinoRecipeAPI.models.response.UserResponse

fun User.toResponse(): UserResponse =
    UserResponse(
        id = this.id ?: "",
        username = username,
        email = email,
        role = role.name
    )