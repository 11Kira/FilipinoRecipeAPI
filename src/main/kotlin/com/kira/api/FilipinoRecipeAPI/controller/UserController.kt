package com.kira.api.FilipinoRecipeAPI.controller

import com.kira.api.FilipinoRecipeAPI.models.enums.ResponseStatus
import com.kira.api.FilipinoRecipeAPI.models.response.ApiResponse
import com.kira.api.FilipinoRecipeAPI.models.response.PagingResponse
import com.kira.api.FilipinoRecipeAPI.models.response.RecipeResponse
import com.kira.api.FilipinoRecipeAPI.service.RecipeService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val recipeService: RecipeService
) {
    @PostMapping("/favorites/{recipeId}")
    fun toggleFavorite(
        @PathVariable recipeId: String,
        authentication: Authentication
    ): ResponseEntity<ApiResponse<Unit>> {
        val userId = authentication.principal as String

        val message = recipeService.toggleFavorite(recipeId, userId)

        return ResponseEntity.ok(
            ApiResponse(
                status = ResponseStatus.SUCCESS,
                message = message,
                data = null
            )
        )
    }

    @GetMapping("/favorites")
    fun getFavoriteRecipes(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false) query: String?,
        authentication: Authentication,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<List<RecipeResponse>>> {

        val userId = authentication.principal as String
        val pageable = PageRequest.of(page - 1, size)
        val pageResult = recipeService.getFavoriteRecipes(userId, query, pageable)

        val baseUrl = request.requestURL.toString()
        fun pageUrl(p: Int) = "$baseUrl?page=$p&size=$size" + (if (!query.isNullOrBlank()) "&query=$query" else "")

        return ResponseEntity.ok(
            ApiResponse(
                status = ResponseStatus.SUCCESS,
                message = "Favorite recipes retrieved successfully",
                data = pageResult.content,
                paging = PagingResponse(
                    page = page,
                    size = size,
                    total = pageResult.totalElements,
                    next = if (pageResult.hasNext()) pageUrl(page + 1) else null,
                    previous = if (pageResult.hasPrevious()) pageUrl(page - 1) else null
                )
            )
        )
    }
}