package com.kira.api.FilipinoRecipeAPI.controller

import com.kira.api.FilipinoRecipeAPI.controller.RecipeController.RecipeResponse
import com.kira.api.FilipinoRecipeAPI.database.model.Recipe
import com.kira.api.FilipinoRecipeAPI.database.repository.RecipeRepository
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.Instant

@RestController
@RequestMapping("/recipes")
class RecipeController(
    private val recipeRepository: RecipeRepository
) {
    data class RecipeRequest(
        @field:NotBlank(message = "Image can't be blank.")
        val image: String,
        @field:NotBlank(message = "Name can't be blank.")
        val name: String = "",
        val description: String = "",
        @field:NotBlank(message = "Ingredients can't be blank.")
        val ingredients: List<String> = emptyList(),
        @field:NotBlank(message = "Instructions can't be blank.")
        val instructions: List<String> = emptyList(),
        val tips: List<String> = emptyList(),
        val recipeId: Long = 0L
    )

    data class RecipeResponse(
        val image: String,
        val name: String,
        val description: String,
        val ingredients: List<String>,
        val instructions: List<String>,
        val tips: List<String>,
        val recipeId: Long,
        val createdAt: Instant
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

    @PostMapping
    fun save(
        @Valid @RequestBody body: RecipeRequest
    ): RecipeResponse {
        val recipe = recipeRepository.save(
            Recipe(
                image = body.image,
                name = body.name,
                description = body.description,
                ingredients = body.ingredients,
                tips = body.tips,
                recipeId = body.recipeId,
                createdAt = Instant.now()
            )
        )

        return recipe.toResponse()
    }
}

private fun Recipe.toResponse(): RecipeResponse {
    return RecipeResponse(
        image = image,
        name = name,
        description = description,
        ingredients = ingredients,
        instructions = instructions,
        tips = tips,
        createdAt = createdAt,
        recipeId = recipeId
    )
}