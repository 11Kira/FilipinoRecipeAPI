package com.kira.api.FilipinoRecipeAPI.controller

import com.kira.api.FilipinoRecipeAPI.database.model.User
import com.kira.api.FilipinoRecipeAPI.database.repository.recipe.RecipeRepository
import com.kira.api.FilipinoRecipeAPI.database.repository.user.UserRepository
import com.kira.api.FilipinoRecipeAPI.models.enums.ResponseStatus
import com.kira.api.FilipinoRecipeAPI.models.exception.ResourceNotFoundException
import com.kira.api.FilipinoRecipeAPI.models.response.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
}