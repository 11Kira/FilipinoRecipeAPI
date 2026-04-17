package com.kira.api.FilipinoRecipeAPI.service

import com.kira.api.FilipinoRecipeAPI.database.model.Recipe
import com.kira.api.FilipinoRecipeAPI.database.repository.recipe.RecipeRepository
import com.kira.api.FilipinoRecipeAPI.database.repository.user.UserRepository
import com.kira.api.FilipinoRecipeAPI.models.exception.ResourceNotFoundException
import com.kira.api.FilipinoRecipeAPI.models.requests.RecipeRequest
import com.kira.api.FilipinoRecipeAPI.models.requests.patch.RecipePatchRequest
import com.kira.api.FilipinoRecipeAPI.models.response.RecipeResponse
import com.kira.api.FilipinoRecipeAPI.models.response.toResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.Instant

@Service
class RecipeService(
    private val recipeRepository: RecipeRepository,
    private val userRepository: UserRepository
) {
    fun getAllRecipes(
        pageable: PageRequest,
        query: String?,
        categoryList: List<String>?,
        proteinList: List<String>?,
        difficultyList: List<String>?,
        maxCookingTime: Int?,
        userId: String
    ): Page<RecipeResponse> {
        val pageResult = recipeRepository.searchRecipes(
            query = query,
            categoryList = categoryList,
            proteinList = proteinList,
            difficultyList = difficultyList,
            maxCookingTime = maxCookingTime,
            pageable = pageable,
        )

        val user = userRepository.findById(userId).orElseThrow { ResourceNotFoundException("User not found") }
        val favoriteIds = user.favoriteRecipeIds ?: emptySet()

        return pageResult.map { it.toResponse(isFavorited = favoriteIds.contains(it.id)) }
    }

    fun getRecipeById(recipeId: String, userId: String): RecipeResponse {
        val recipe = recipeRepository.findById(recipeId)
            .orElseThrow { ResourceNotFoundException("Recipe not found with id: $recipeId") }
        val user = userRepository.findById(userId)
            .orElseThrow { ResourceNotFoundException("User not found") }

        val isFavorited = user.favoriteRecipeIds?.contains(recipeId) ?: false

        return recipe.toResponse(isFavorited = isFavorited)
    }

    fun saveRecipe(body: RecipeRequest, ownerId: String): RecipeResponse {
        val recipe = Recipe(
            ownerId = ownerId,
            title = body.title,
            description = body.description,
            image = body.image,
            estimatedMinutes = body.estimatedMinutes,
            difficulty = body.difficulty,
            category = body.category,
            protein = body.protein,
            mealTime = body.mealTime,
            ingredients = body.ingredients,
            steps = body.steps,
            cookingTips = body.cookingTips,
            variations = body.variations,
            servingSuggestions = body.servingSuggestions,
            createdAt = Instant.now(),
            updatedAt = Instant.now(),
            published = true
        )
        return recipeRepository.save(recipe).toResponse()
    }

    fun updateRecipe(id: String, body: RecipeRequest, userId: String): RecipeResponse {
        val existingRecipe = recipeRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Recipe not found with id: $id") }

        if (existingRecipe.ownerId != userId) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "You do not own this recipe")
        }

        val updatedRecipe = existingRecipe.copy(
            title = body.title,
            description = body.description,
            image = body.image,
            estimatedMinutes = body.estimatedMinutes,
            difficulty = body.difficulty,
            category = body.category,
            protein = body.protein,
            mealTime = body.mealTime,
            ingredients = body.ingredients,
            steps = body.steps,
            cookingTips = body.cookingTips,
            variations = body.variations,
            servingSuggestions = body.servingSuggestions,
            updatedAt = Instant.now()
        )

        return recipeRepository.save(updatedRecipe).toResponse(isFavorited = false)
    }

    fun patchRecipe(id: String, body: RecipePatchRequest, userId: String): RecipeResponse {
        val existingRecipe = recipeRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Recipe not found with id: $id") }

        if (existingRecipe.ownerId != userId) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "You do not own this recipe")
        }

        // Merge logic for ingredients
        val updatedIngredients = body.ingredients?.let { patch ->
            existingRecipe.ingredients.copy(
                main = patch.main ?: existingRecipe.ingredients.main,
                aromatics = patch.aromatics ?: existingRecipe.ingredients.aromatics,
                liquidsAndSeasonings = patch.liquidsAndSeasonings ?: existingRecipe.ingredients.liquidsAndSeasonings,
                vegetables = patch.vegetables ?: existingRecipe.ingredients.vegetables,
                optionalAddons = patch.optionalAddons ?: existingRecipe.ingredients.optionalAddons
            )
        } ?: existingRecipe.ingredients

        val updatedRecipe = existingRecipe.copy(
            title = body.title ?: existingRecipe.title,
            description = body.description ?: existingRecipe.description,
            image = body.image ?: existingRecipe.image,
            estimatedMinutes = body.estimatedMinutes ?: existingRecipe.estimatedMinutes,
            difficulty = body.difficulty ?: existingRecipe.difficulty,
            category = body.category ?: existingRecipe.category,
            protein = body.protein ?: existingRecipe.protein,
            mealTime = body.mealTime ?: existingRecipe.mealTime,
            ingredients = updatedIngredients,
            steps = body.steps ?: existingRecipe.steps,
            cookingTips = body.cookingTips ?: existingRecipe.cookingTips,
            variations = body.variations ?: existingRecipe.variations,
            servingSuggestions = body.servingSuggestions ?: existingRecipe.servingSuggestions,
            updatedAt = Instant.now()
        )

        return recipeRepository.save(updatedRecipe).toResponse(isFavorited = false)
    }

    fun deleteRecipe(id: String, userId: String) {
        val recipe = recipeRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Recipe not found with id: $id") }

        if (recipe.ownerId != userId) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "You do not own this recipe")
        }

        recipeRepository.deleteById(id)
    }
}