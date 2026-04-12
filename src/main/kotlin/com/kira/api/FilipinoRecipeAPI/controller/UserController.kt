package com.kira.api.FilipinoRecipeAPI.controller

import com.kira.api.FilipinoRecipeAPI.database.model.User
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
        val currentUser = authentication.principal as User
        if (!recipeRepository.existsById(recipeId)) {
            throw ResourceNotFoundException("Recipe not found")
        }
        val updatedFavorites = currentUser.favoriteRecipeIds.toMutableList()
        if (updatedFavorites.contains(recipeId)) {
            updatedFavorites.remove(recipeId) // Un-favorite if already there
        } else {
            updatedFavorites.add(recipeId)
        }
        userRepository.save(currentUser.copy(favoriteRecipeIds = updatedFavorites))

        return ResponseEntity.ok(
            ApiResponse(
                status = ResponseStatus.SUCCESS,
                message = "Favorites updated",
                data = null
            )
        )
    }

    @GetMapping("/favorites")
    fun getFavoriteRecipes(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        authentication: Authentication
    ): ResponseEntity<ApiResponse<List<RecipeResponse>>> {

        val currentUserId = authentication.principal as String
        val user = userRepository.findById(currentUserId)
            .orElseThrow { ResourceNotFoundException("User not found") }

        val pageable = PageRequest.of(page - 1, size)

        // Use the list of IDs from the User document to query the Recipe collection
        val recipesPage = recipeRepository.findAllByIdIn(user.favoriteRecipeIds, pageable)

        val data = recipesPage.content.map { it.toResponse() }

        return ResponseEntity.ok(
            ApiResponse(
                status = ResponseStatus.SUCCESS,
                message = "Favorite recipes retrieved",
                data = data,
                paging = PagingResponse(
                    page = page,
                    size = size,
                    total = recipesPage.totalElements,
                    next = if (recipesPage.hasNext()) "..." else null,
                    previous = if (recipesPage.hasPrevious()) "..." else null
                )
            )
        )
    }
}