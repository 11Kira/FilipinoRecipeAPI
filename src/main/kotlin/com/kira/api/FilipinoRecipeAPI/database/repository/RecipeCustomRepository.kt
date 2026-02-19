package com.kira.api.FilipinoRecipeAPI.database.repository

import com.kira.api.FilipinoRecipeAPI.database.model.Recipe
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface RecipeCustomRepository {
    fun searchRecipes(
        query: String?,
        categoryList: List<String>?,
        proteinList: List<String>?, // accept list
        difficultyList: List<String>?,
        maxCookingTime: Int?,
        pageable: Pageable
    ): Page<Recipe>
}