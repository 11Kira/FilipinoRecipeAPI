package com.kira.api.FilipinoRecipeAPI.controller

import com.kira.api.FilipinoRecipeAPI.models.enums.ResponseStatus
import com.kira.api.FilipinoRecipeAPI.models.requests.RecipeRequest
import com.kira.api.FilipinoRecipeAPI.models.requests.patch.RecipePatchRequest
import com.kira.api.FilipinoRecipeAPI.models.response.ApiResponse
import com.kira.api.FilipinoRecipeAPI.models.response.PagingResponse
import com.kira.api.FilipinoRecipeAPI.models.response.RecipeResponse
import com.kira.api.FilipinoRecipeAPI.service.RecipeService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/recipes")
class RecipeController(
    private val recipeService: RecipeService
) {
    @GetMapping
    fun getAllRecipes(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false) query: String?,
        @RequestParam(required = false) category: String?,
        @RequestParam(required = false) protein: String?,
        @RequestParam(required = false) difficulty: String?,
        @RequestParam(required = false) maxCookingTime: Int?,
        @RequestParam(defaultValue = "createdAt,desc") sort: String,
        authentication: Authentication?,
    ): ResponseEntity<ApiResponse<List<RecipeResponse>>> {
        val userId: String? = authentication?.principal?.toString()

        val pageResult = recipeService.getAllRecipes(
            userId = userId,
            query = query,
            category = category,
            protein = protein,
            difficulty = difficulty,
            maxCookingTime = maxCookingTime,
            page = page,
            size = size,
            sort = sort
        )

        return ResponseEntity.ok(
            ApiResponse(
                status = ResponseStatus.SUCCESS,
                message = "Recipes retrieved successfully",
                data = pageResult.content,
                paging = PagingResponse(
                    page = page,
                    size = size,
                    totalPages = pageResult.totalPages,
                    totalElements = pageResult.totalElements,
                    hasNext = pageResult.hasNext()
                )
            )
        )
    }

    @GetMapping("/{id}")
    fun getRecipeById(
        @PathVariable("id") id: String,
        authentication: Authentication
    ): ResponseEntity<ApiResponse<RecipeResponse>> {
        val userId = authentication.principal.toString()
        val data = recipeService.getRecipeById(id, userId)
        return ResponseEntity.ok(ApiResponse(ResponseStatus.SUCCESS, "Recipe retrieved", data))
    }

    @PostMapping
    fun saveRecipe(
        @Valid @RequestBody body: RecipeRequest,
        authentication: Authentication
    ): ResponseEntity<ApiResponse<RecipeResponse>> {
        val userId = authentication.principal.toString()
        val data = recipeService.saveRecipe(body, userId)
        return ResponseEntity.ok(ApiResponse(ResponseStatus.SUCCESS, "Recipe created", data))
    }

    @PutMapping("/{id}")
    fun updateRecipe(
        @PathVariable id: String,
        @Valid @RequestBody body: RecipeRequest,
        authentication: Authentication
    ): ResponseEntity<ApiResponse<RecipeResponse>> {
        val userId = authentication.principal.toString()
        val data = recipeService.updateRecipe(id, body, userId)

        return ResponseEntity.ok(
            ApiResponse(
                status = ResponseStatus.SUCCESS,
                message = "Recipe updated successfully",
                data = data
            )
        )
    }

    @PatchMapping("/{id}")
    fun patchRecipe(
        @PathVariable id: String,
        @RequestBody body: RecipePatchRequest,
        authentication: Authentication
    ): ResponseEntity<ApiResponse<RecipeResponse>> {
        val userId = authentication.principal.toString()
        val data = recipeService.patchRecipe(id, body, userId)

        return ResponseEntity.ok(
            ApiResponse(
                status = ResponseStatus.SUCCESS,
                message = "Recipe patched successfully",
                data = data
            )
        )
    }

    @DeleteMapping("/{id}")
    fun deleteRecipe(
        @PathVariable id: String,
        authentication: Authentication
    ): ResponseEntity<ApiResponse<Unit>> {
        val userId = authentication.principal.toString()
        recipeService.deleteRecipe(id, userId)

        return ResponseEntity.ok(
            ApiResponse(
                status = ResponseStatus.SUCCESS,
                message = "Recipe deleted successfully",
                data = null
            )
        )
    }
}