package com.kira.api.FilipinoRecipeAPI.models.response

import com.kira.api.FilipinoRecipeAPI.database.model.Recipe

fun Recipe.toResponse(isFavorited: Boolean = false): RecipeResponse =
    RecipeResponse(
        id = this.id ?: "",
        title = title,
        description = description,
        image = image,
        estimatedMinutes = estimatedMinutes,
        difficulty = difficulty,
        category = category,
        protein = protein,
        mealTime = mealTime,
        ingredients = ingredients,
        steps = steps,
        cookingTips = cookingTips,
        variations = variations,
        servingSuggestions = servingSuggestions,
        isFavorited = isFavorited,
        createdAt = createdAt,
        updatedAt = updatedAt,
        published = published
    )