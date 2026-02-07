package com.kira.api.FilipinoRecipeAPI.controller

import com.kira.api.FilipinoRecipeAPI.database.model.Recipe
import com.kira.api.FilipinoRecipeAPI.database.repository.RecipeRepository
import com.kira.api.FilipinoRecipeAPI.models.*
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import org.jetbrains.annotations.NotNull
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
        @field:NotBlank(message = "Title can't be blank.")
        val title: String,
        val description: String = "",
        @field:NotBlank(message = "Image can't be blank.")
        val image: String,
        @field:NotNull
        val estimatedMinutes: Int,
        @field:NotNull
        val difficulty: Difficulty,
        @field:NotNull
        val category: Category,
        val ingredients: Ingredients,
        @field:NotEmpty(message = "Steps can't be blank.")
        val steps: List<String>,
        val cookingTips: List<String> = emptyList(),
        val variations: List<String> = emptyList(),
        val servingSuggestions: List<String> = emptyList()
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
                title = body.title,
                description = body.description,
                image = body.image,
                estimatedMinutes = body.estimatedMinutes,
                difficulty = body.difficulty,
                category = body.category,
                ingredients = body.ingredients,
                steps = body.steps,
                cookingTips = body.cookingTips,
                variations = body.variations,
                servingSuggestions = body.servingSuggestions,
                createdAt = Instant.now(),
                updatedAt = Instant.now(),
                published = true
            )
        )
        return recipe.toResponse()
    }

    @PatchMapping("/{id}")
    fun patchRecipeById(
        @PathVariable id: String,
        @RequestBody body: RecipePatchRequest
    ): RecipeResponse {
        val existingRecipe = recipeRepository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found with id: $id")
        }

        if (
            body.title == null &&
            body.description == null &&
            body.image == null &&
            body.estimatedMinutes == null &&
            body.difficulty == null &&
            body.category == null &&
            body.ingredients == null &&
            body.steps == null &&
            body.cookingTips == null &&
            body.variations == null &&
            body.servingSuggestions == null
        ) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "At least one field must be provided for update"
            )
        }

        val updatedIngredients = body.ingredients?.let { patch ->
            existingRecipe.ingredients.copy(
                main = patch.main ?: existingRecipe.ingredients.main,
                aromatics = patch.aromatics ?: existingRecipe.ingredients.aromatics,
                liquidsAndSeasonings = patch.liquidsAndSeasonings ?: existingRecipe.ingredients.liquidsAndSeasonings,
                vegetables = patch.vegetables ?: existingRecipe.ingredients.vegetables,
                optionalAddons = patch.optionalAddons ?: existingRecipe.ingredients.optionalAddons
            )
        } ?: existingRecipe.ingredients

        val updatedRecipe = existingRecipe.copy(
            title = body.title ?: existingRecipe.title,
            description = body.description ?: existingRecipe.description,
            image = body.image ?: existingRecipe.image,
            estimatedMinutes = body.estimatedMinutes ?: existingRecipe.estimatedMinutes,
            difficulty = body.difficulty ?: existingRecipe.difficulty,
            category = body.category ?: existingRecipe.category,
            ingredients = updatedIngredients,
            steps = body.steps ?: existingRecipe.steps,
            cookingTips = body.cookingTips ?: existingRecipe.cookingTips,
            variations = body.variations ?: existingRecipe.variations,
            servingSuggestions = body.servingSuggestions ?: existingRecipe.servingSuggestions,
            updatedAt = Instant.now()
        )

        val savedRecipe = recipeRepository.save(updatedRecipe)
        return savedRecipe.toResponse()
    }

    @PutMapping("/{id}")
    fun updateRecipeById(
        @PathVariable id: String,
        @Valid @RequestBody body: RecipeRequest
    ): RecipeResponse {
        val existingRecipe = recipeRepository.findById(id).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found with id: $id")
        }
        val updatedRecipe = existingRecipe.copy(
            title = body.title,
            description = body.description,
            image = body.image,
            estimatedMinutes = body.estimatedMinutes,
            difficulty = body.difficulty,
            category = body.category,
            ingredients = body.ingredients,
            steps = body.steps,
            cookingTips = body.cookingTips,
            variations = body.variations,
            servingSuggestions = body.servingSuggestions,
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
        title = title,
        description = description,
        image = image,
        estimatedMinutes = estimatedMinutes,
        difficulty = difficulty,
        category = category,
        ingredients = ingredients,
        steps = steps,
        cookingTips = cookingTips,
        variations = variations,
        servingSuggestions = servingSuggestions,
        createdAt = createdAt,
        updatedAt = updatedAt,
        published = published
    )