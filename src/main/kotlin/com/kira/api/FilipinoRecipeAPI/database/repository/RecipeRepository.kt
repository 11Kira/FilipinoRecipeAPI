package com.kira.api.FilipinoRecipeAPI.database.repository

import com.kira.api.FilipinoRecipeAPI.database.model.Recipe
import org.springframework.data.mongodb.repository.MongoRepository

interface RecipeRepository : MongoRepository<Recipe, String>