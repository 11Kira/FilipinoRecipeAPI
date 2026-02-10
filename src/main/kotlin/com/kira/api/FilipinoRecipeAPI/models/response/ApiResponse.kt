package com.kira.api.FilipinoRecipeAPI.models.response

data class ApiResponse<T>(
    val status: String,
    val message: String,
    val data: T?,
    val paging: PagingResponse? = null
)