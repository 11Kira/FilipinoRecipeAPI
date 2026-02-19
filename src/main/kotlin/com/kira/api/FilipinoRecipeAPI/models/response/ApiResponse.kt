package com.kira.api.FilipinoRecipeAPI.models.response

import com.kira.api.FilipinoRecipeAPI.models.enums.ResponseStatus

data class ApiResponse<T>(
    val status: ResponseStatus,
    val message: String,
    val data: T?,
    val paging: PagingResponse? = null
)