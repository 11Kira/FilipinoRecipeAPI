package com.kira.api.FilipinoRecipeAPI.controller

import com.kira.api.FilipinoRecipeAPI.controller.RecipeController.RecipeResponse
import com.kira.api.FilipinoRecipeAPI.database.model.Recipe
import com.kira.api.FilipinoRecipeAPI.database.repository.RecipeRepository
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.time.Instant

@RestController
@RequestMapping("/api/recipes")
class RecipeController(
    private val recipeRepository: RecipeRepository
) {
    data class RecipeRequest(
        @field:NotBlank(message = "Image can't be blank.")
        val image: String,
        @field:NotBlank(message = "Name can't be blank.")
        val name: String = "",
        val description: String = "",
        @field:NotEmpty(message = "Ingredients can't be blank.")
        val ingredients: List<String> = emptyList(),
        @field:NotEmpty(message = "Instructions can't be blank.")
        val instructions: List<String> = emptyList(),
        val tips: List<String> = emptyList(),
    )

    data class RecipeResponse(
        val id: String,
        val image: String,
        val name: String,
        val description: String,
        val ingredients: List<String>,
        val instructions: List<String>,
        val tips: List<String>,
        val createdAt: Instant,
        val updatedAt: Instant
    )

    @GetMapping
    fun getAllRecipes(): List<Recipe> {
        return recipeRepository.findAll()
    }

    @GetMapping("/{id}")
    fun getRecipeById(@PathVariable("id") id: String): ResponseEntity<Recipe> {
        val recipe = recipeRepository.findById(id)
        return if (recipe.isPresent) ResponseEntity.ok(recipe.get()) else ResponseEntity.notFound().build()
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
                instructions = body.instructions,
                tips = body.tips,
                createdAt = Instant.now(),
                updatedAt = Instant.now()
            )
        )
        return recipe.toResponse()
    }

    @PutMapping("/{id}")
    fun updateRecipeById(
        @PathVariable id: String,
        @Valid @RequestBody body: RecipeRequest
    ): RecipeResponse {
        val existingRecipe = recipeRepository.findById(id).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found with id: $id") }
        val updatedRecipe = existingRecipe.copy(
            image = body.image,
            name = body.name,
            description = body.description,
            ingredients = body.ingredients,
            instructions = body.instructions,
            tips = body.tips,
            updatedAt = Instant.now()
        )

        val savedRecipe = recipeRepository.save(updatedRecipe)
        return savedRecipe.toResponse()
    }

    @DeleteMapping("/{id}")
    fun deleteRecipeById(@PathVariable("id") id: String) {
        recipeRepository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found with id: $id")
        }.apply {
            recipeRepository.deleteById(id)
        }
    }
}

private fun Recipe.toResponse(): RecipeResponse =
    RecipeResponse(
        id = this.id ?: "",
        image = image,
        name = name,
        description = description,
        ingredients = ingredients,
        instructions = instructions,
        tips = tips,
        createdAt = createdAt,
        updatedAt = updatedAt
    )