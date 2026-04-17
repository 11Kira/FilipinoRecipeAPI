package com.kira.api.FilipinoRecipeAPI.controller

import com.kira.api.FilipinoRecipeAPI.database.repository.recipe.RecipeRepository
import com.kira.api.FilipinoRecipeAPI.database.repository.user.UserRepository
import com.kira.api.FilipinoRecipeAPI.models.enums.ResponseStatus
import com.kira.api.FilipinoRecipeAPI.models.exception.ResourceNotFoundException
import com.kira.api.FilipinoRecipeAPI.models.response.ApiResponse
import com.kira.api.FilipinoRecipeAPI.models.response.PagingResponse
import com.kira.api.FilipinoRecipeAPI.models.response.RecipeResponse
import com.kira.api.FilipinoRecipeAPI.models.response.toResponse
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userRepository: UserRepository,
    private val recipeRepository: RecipeRepository
) {
    @PostMapping("/favorites/{recipeId}")
    fun toggleFavorite(
        @PathVariable recipeId: String,
        authentication: Authentication
    ): ResponseEntity<ApiResponse<Unit>> {
        val currentUserId = authentication.principal as String

        // Fetch the actual user document from MongoDB
        val user = userRepository.findById(currentUserId)
            .orElseThrow { ResourceNotFoundException("User not found") }

        if (!recipeRepository.existsById(recipeId)) {
            throw ResourceNotFoundException("Recipe not found with id: $recipeId")
        }

        val updatedFavorites = user.favoriteRecipeIds.toMutableList()
        val message = if (updatedFavorites.contains(recipeId)) {
            updatedFavorites.remove(recipeId)
            "Recipe removed from favorites"
        } else {
            updatedFavorites.add(recipeId)
            "Recipe added to favorites"
        }

        userRepository.save(user.copy(favoriteRecipeIds = updatedFavorites))

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
        @RequestParam(required = false) query: String?,
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        authentication: Authentication
    ): ResponseEntity<ApiResponse<List<RecipeResponse>>> {

        val currentUserId = authentication.principal as String
        val user = userRepository.findById(currentUserId)
            .orElseThrow { ResourceNotFoundException("User not found") }
        val pageable = PageRequest.of(page - 1, size)
        val recipesPage = recipeRepository.findAllByIdIn(user.favoriteRecipeIds, pageable)
        val data = recipesPage.content.map { it.toResponse() }

        return ResponseEntity.ok(
            ApiResponse(
                status = ResponseStatus.SUCCESS,
                message = "Favorite recipes retrieved successfully",
                data = data,
                paging = PagingResponse(
                    page = page,
                    size = size,
                    total = recipesPage.totalElements,
                    next = if (recipesPage.hasNext()) "Next page logic here" else null,
                    previous = if (recipesPage.hasPrevious()) "Previous page logic here" else null
                )
            )
        )
    }
}