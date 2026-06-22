package com.kira.api.FilipinoRecipeAPI.models.response

data class PagingResponse(
    val page: Int,
    val size: Int,
    val totalPages: Int,
    val totalElements: Long,
    val hasNext: Boolean
)
