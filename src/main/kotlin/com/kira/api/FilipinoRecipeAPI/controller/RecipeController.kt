package com.kira.api.FilipinoRecipeAPI.controller

import com.kira.api.FilipinoRecipeAPI.database.model.Recipe
import com.kira.api.FilipinoRecipeAPI.database.repository.RecipeRepository
import com.kira.api.FilipinoRecipeAPI.models.enums.ResponseStatus
import com.kira.api.FilipinoRecipeAPI.models.exception.ResourceNotFoundException
import com.kira.api.FilipinoRecipeAPI.models.requests.RecipeRequest
import com.kira.api.FilipinoRecipeAPI.models.requests.patch.RecipePatchRequest
import com.kira.api.FilipinoRecipeAPI.models.response.ApiResponse
import com.kira.api.FilipinoRecipeAPI.models.response.PagingResponse
import com.kira.api.FilipinoRecipeAPI.models.response.RecipeResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.data.domain.PageRequest
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
    @GetMapping
    fun getAllRecipes(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<List<RecipeResponse>>> {
        return runCatching {
            val pageable = PageRequest.of(page - 1, size)
            val pageResult = recipeRepository.findAll(pageable)
            val data = pageResult.content.map { it.toResponse() }
            val baseUrl = request.requestURL.toString()
            fun pageUrl(page: Int) =
                "$baseUrl?page=$page&size=${pageable.pageSize}"

            val next = if (pageResult.hasNext())
                pageUrl(pageable.pageNumber + 1)
            else
                null

            val previous = if (pageResult.hasPrevious())
                pageUrl(pageable.pageNumber - 1)
            else
                null

            ResponseEntity.ok(
                ApiResponse(
                    status = ResponseStatus.SUCCESS,
                    message = "Recipes fetched successfully",
                    data = data,
                    paging = PagingResponse(
                        page = pageable.pageNumber + 1,
                        size = pageable.pageSize,
                        total = pageResult.totalElements,
                        next = next,
                        previous = previous
                    )
                )
            )
        }.getOrElse { exception ->
            ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                    ApiResponse(
                        status = ResponseStatus.FAILED,
                        message = exception.message ?: "Failed to fetch recipes",
                        data = null,
                        paging = null
                    )
                )
        }
    }

    @GetMapping("/{id}")
    fun getRecipeById(@PathVariable("id") id: String): ResponseEntity<ApiResponse<RecipeResponse>> {
        val recipe =
            recipeRepository.findById(id).orElseThrow { ResourceNotFoundException("Recipe not found with id: $id") }
        return ResponseEntity.ok(
            ApiResponse(
                status = ResponseStatus.SUCCESS,
                message = "Recipe fetched successfully",
                data = recipe.toResponse(),
                paging = null
            )
        )
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
                protein = body.protein,
                mealTime = body.mealTime,
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
        val existingRecipe =
            recipeRepository.findById(id).orElseThrow { ResourceNotFoundException("Recipe not found with id: $id") }

        if (
            body.title == null &&
            body.description == null &&
            body.image == null &&
            body.estimatedMinutes == null &&
            body.difficulty == null &&
            body.category == null &&
            body.protein == null &&
            body.mealTime == null &&
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
            protein = body.protein ?: existingRecipe.protein,
            mealTime = body.mealTime ?: existingRecipe.mealTime,
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
        val existingRecipe =
            recipeRepository.findById(id).orElseThrow { ResourceNotFoundException("Recipe not found with id: $id") }
        val updatedRecipe = existingRecipe.copy(
            title = body.title,
            description = body.description,
            image = body.image,
            estimatedMinutes = body.estimatedMinutes,
            difficulty = body.difficulty,
            category = body.category,
            protein = body.protein,
            mealTime = body.mealTime,
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
            ResourceNotFoundException("Recipe not found with id: $id")
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
        protein = protein,
        mealTime = mealTime,
        ingredients = ingredients,
        steps = steps,
        cookingTips = cookingTips,
        variations = variations,
        servingSuggestions = servingSuggestions,
        createdAt = createdAt,
        updatedAt = updatedAt,
        published = published
    )