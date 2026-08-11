package com.kira.api.FilipinoRecipeAPI.dto.response

import com.kira.api.FilipinoRecipeAPI.model.enums.ResponseStatus

data class ApiResponse<T>(
    val status: ResponseStatus,
    val message: String,
    val data: T?,
    val paging: PagingResponse? = null
)