package com.kira.api.FilipinoRecipeAPI.models.exception

import com.kira.api.FilipinoRecipeAPI.models.enums.ResponseStatus
import com.kira.api.FilipinoRecipeAPI.models.response.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleNotFound(ex: ResourceNotFoundException): ResponseEntity<ApiResponse<Nothing>> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ApiResponse(ResponseStatus.FAILED, ex.message ?: "Not found", null, null))
    }

    // Handles our new duplicate user exception
    @ExceptionHandler(UserAlreadyExistsException::class)
    fun handleUserAlreadyExists(ex: UserAlreadyExistsException): ResponseEntity<ApiResponse<Unit>> {
        return ResponseEntity
            .status(HttpStatus.CONFLICT) // 409 Conflict
            .body(ApiResponse(ResponseStatus.FAILED, ex.message ?: "User already exists.", null))
    }

    // Handles login failures from your AuthService
    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentials(ex: BadCredentialsException): ResponseEntity<ApiResponse<Unit>> {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED) // 401 Unauthorized
            .body(ApiResponse(ResponseStatus.FAILED, "Invalid email or password.", null))
    }

    // The Catch-All for database timeouts, 500s, null pointers, etc.
    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ApiResponse<Unit>> {
        // TODO: Log the actual exception (ex.message) here for your own debugging!

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR) // 500 Internal Server Error
            .body(
                ApiResponse(
                    ResponseStatus.FAILED,
                    "Something went wrong on our end. Please try again later.",
                    null
                )
            )
    }
}
