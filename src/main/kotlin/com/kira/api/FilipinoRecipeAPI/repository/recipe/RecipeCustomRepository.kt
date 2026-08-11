package com.kira.api.FilipinoRecipeAPI.repository.recipe

import com.kira.api.FilipinoRecipeAPI.model.Recipe
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface RecipeCustomRepository {
    fun searchRecipes(
        query: String?,
        categoryList: List<String>?,
        proteinList: List<String>?,
        difficultyList: List<String>?,
        maxCookingTime: Int?,
        pageable: Pageable,
        recipeIds: List<String>? = null,
    ): Page<Recipe>
}