package com.kira.api.FilipinoRecipeAPI.models.exception

import com.kira.api.FilipinoRecipeAPI.models.enums.ResponseStatus
import com.kira.api.FilipinoRecipeAPI.models.response.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleNotFound(ex: ResourceNotFoundException): ResponseEntity<ApiResponse<Nothing>> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ApiResponse(ResponseStatus.FAILED, ex.message ?: "Not found", null, null))
    }
}
