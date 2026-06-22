package com.kira.api.FilipinoRecipeAPI.controller

import com.kira.api.FilipinoRecipeAPI.models.enums.ResponseStatus
import com.kira.api.FilipinoRecipeAPI.models.response.ApiResponse
import com.kira.api.FilipinoRecipeAPI.models.response.PagingResponse
import com.kira.api.FilipinoRecipeAPI.models.response.RecipeResponse
import com.kira.api.FilipinoRecipeAPI.models.response.UserResponse
import com.kira.api.FilipinoRecipeAPI.service.RecipeService
import com.kira.api.FilipinoRecipeAPI.service.UserService
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val recipeService: RecipeService,
    private val userService: UserService
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

    @GetMapping("/profile")
    fun getUserProfile(
        authentication: Authentication
    ): ResponseEntity<ApiResponse<UserResponse>> {
        val userId = authentication.principal as String

        return ResponseEntity.ok(
            ApiResponse(
                status = ResponseStatus.SUCCESS,
                message = "User profile retrieved successfully",
                data = userService.getUser(userId),
                paging = null
            )
        )
    }

    @GetMapping("/favorites")
    fun getFavoriteRecipes(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false) query: String?,
        authentication: Authentication,
    ): ResponseEntity<ApiResponse<List<RecipeResponse>>> {

        val userId = authentication.principal as String
        val pageable = PageRequest.of(page - 1, size)
        val pageResult = recipeService.getFavoriteRecipes(userId, query, pageable)

        return ResponseEntity.ok(
            ApiResponse(
                status = ResponseStatus.SUCCESS,
                message = "Favorite recipes retrieved successfully",
                data = pageResult.content,
                paging = PagingResponse(
                    page = page,
                    size = size,
                    totalPages = pageResult.totalPages,
                    totalElements = pageResult.totalElements,
                    hasNext = pageResult.hasNext()
                )
            )
        )
    }
}