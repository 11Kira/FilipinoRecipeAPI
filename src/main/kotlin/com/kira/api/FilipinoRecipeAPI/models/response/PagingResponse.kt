package com.kira.api.FilipinoRecipeAPI.models.response

data class PagingResponse(
    val page: Int,
    val size: Int,
    val total: Long,
    val next: String?,
    val previous: String?,
)
