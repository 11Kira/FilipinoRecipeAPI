package com.kira.api.FilipinoRecipeAPI.controller

import com.kira.api.FilipinoRecipeAPI.database.model.Recipe
import com.kira.api.FilipinoRecipeAPI.database.repository.RecipeRepository
import jakarta.validation.constraints.NotBlank
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/recipes")
class RecipeController(
    private val recipeRepository: RecipeRepository
) {
    data class RecipeRequest(
        val image: String?,
        @field:NotBlank(message = "Name can't be blank.")
        val name: String = "",
        val description: String = "",
        @field:NotBlank(message = "Ingredients can't be blank.")
        val ingredients: List<String> = emptyList(),
        @field:NotBlank(message = "Instructions can't be blank.")
        val instructions: List<String> = emptyList(),
        val notes: List<String> = emptyList(),
    )

    @GetMapping
    fun getCount(): Int {
        return recipeRepository.findAll().count()
    }

    @GetMapping("/{id}")
    fun getRecipeById(@PathVariable("id") id: Long): ResponseEntity<Recipe> {
        val recipe = recipeRepository.findByRecipeId(id)
        return if (recipe != null) ResponseEntity.ok(recipe) else ResponseEntity.notFound().build()
    }
}