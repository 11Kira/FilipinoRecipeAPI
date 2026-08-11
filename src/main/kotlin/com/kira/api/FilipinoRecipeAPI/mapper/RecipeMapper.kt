package com.kira.api.FilipinoRecipeAPI.mapper

import com.kira.api.FilipinoRecipeAPI.dto.response.RecipeResponse
import com.kira.api.FilipinoRecipeAPI.model.Recipe
import org.springframework.stereotype.Component

@Component
class RecipeMapper {
    fun toResponse(recipe: Recipe, isFavorited: Boolean): RecipeResponse {
        return RecipeResponse(
            id = recipe.id,
            title = recipe.title,
            description = recipe.description,
            image = recipe.image,
            estimatedMinutes = recipe.estimatedMinutes,
            difficulty = recipe.difficulty,
            category = recipe.category,
            protein = recipe.protein,
            mealTime = recipe.mealTime,
            ingredients = recipe.ingredients,
            steps = recipe.steps,
            cookingTips = recipe.cookingTips,
            variations = recipe.variations,
            servingSuggestions = recipe.servingSuggestions,
            isFavorited = isFavorited,
            createdAt = recipe.createdAt,
            updatedAt = recipe.updatedAt,
            published = recipe.published
        )
    }
}