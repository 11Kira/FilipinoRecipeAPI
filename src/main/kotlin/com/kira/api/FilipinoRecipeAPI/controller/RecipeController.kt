package com.kira.api.FilipinoRecipeAPI.controller

import com.kira.api.FilipinoRecipeAPI.database.model.Recipe
import com.kira.api.FilipinoRecipeAPI.database.repository.RecipeRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/recipes")
class RecipeController(
    private val recipeRepository: RecipeRepository
) {

    @GetMapping
    fun getCount(): Int {
        return recipeRepository.findAll().count()
    }

    @GetMapping("/{id}")
    fun getRecipeById(@PathVariable("id") id: Long): Recipe? {
        return recipeRepository.findByRecipeId(id)
    }
}