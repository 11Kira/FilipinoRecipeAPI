package com.kira.api.FilipinoRecipeAPI.database.repository.recipe

import com.kira.api.FilipinoRecipeAPI.database.model.Recipe
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

interface RecipeRepository : MongoRepository<Recipe, String>, RecipeCustomRepository {
    override fun findAll(pageable: Pageable): Page<Recipe>

    // Fetches all recipes whose ID exists in the provided list
    fun findAllByIdIn(ids: List<String>, pageable: Pageable): Page<Recipe>

    fun findByTitleContainingIgnoreCase(
        title: String,
        pageable: Pageable
    ): Page<Recipe>
}