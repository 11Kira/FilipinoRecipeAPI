package com.kira.api.FilipinoRecipeAPI.controller

import com.kira.api.FilipinoRecipeAPI.models.enums.ResponseStatus
import com.kira.api.FilipinoRecipeAPI.models.requests.RecipeRequest
import com.kira.api.FilipinoRecipeAPI.models.requests.patch.RecipePatchRequest
import com.kira.api.FilipinoRecipeAPI.models.response.ApiResponse
import com.kira.api.FilipinoRecipeAPI.models.response.PagingResponse
import com.kira.api.FilipinoRecipeAPI.models.response.RecipeResponse
import com.kira.api.FilipinoRecipeAPI.service.RecipeService
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
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
        authentication: Authentication,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<List<RecipeResponse>>> {
        val userId = authentication.principal.toString()
        return runCatching {
            val categoryList = category?.split(",")?.map { it.trim().uppercase() }?.filter { it.isNotBlank() }
            val proteinList = protein?.split(",")?.map { it.trim().uppercase() }?.filter { it.isNotBlank() }
            val difficultyList = difficulty?.split(",")?.map { it.trim().uppercase() }?.filter { it.isNotBlank() }

            val sortParts = sort.split(",")
            val pageable = PageRequest.of(
                page - 1, size, Sort.by(
                    if (sortParts[1].equals("desc", true)) Sort.Direction.DESC else Sort.Direction.ASC,
                    sortParts[0]
                )
            )

            val pageResult = recipeService.getAllRecipes(
                userId, query, categoryList, proteinList, difficultyList, maxCookingTime, pageable,
            )

            val baseUrl = request.requestURL.toString()
            fun pageUrl(p: Int) = "$baseUrl?page=$p&size=$size" + (if (!query.isNullOrBlank()) "&query=$query" else "")

            ResponseEntity.ok(
                ApiResponse(
                    status = ResponseStatus.SUCCESS,
                    message = "Recipes retrieved successfully",
                    data = pageResult.content,
                    paging = PagingResponse(
                        page = page,
                        size = size,
                        total = pageResult.totalElements,
                        next = if (pageResult.hasNext()) pageUrl(page + 1) else null,
                        previous = if (pageResult.hasPrevious()) pageUrl(page - 1) else null
                    )
                )
            )
        }.getOrElse {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse(status = ResponseStatus.FAILED, data = null, message = it.message ?: "Error"))
        }
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