package com.kira.api.FilipinoRecipeAPI.security

import com.kira.api.FilipinoRecipeAPI.database.model.User
import com.kira.api.FilipinoRecipeAPI.database.repository.user.UserRepository
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
    private val hashEncoder: HashEncoder
) {

    data class TokenPair(val accessToken: String, val refreshToken: String)

    fun registerUser(
        email: String,
        password: String,
        username: String,
    ): User {
        return userRepository.save(
            User(
                email = email,
                hashedPassword = hashEncoder.encode(password),
                username = username
            )
        )
    }

    fun loginUser(
        email: String,
        password: String,
    ): TokenPair {
        val user = userRepository.findByEmail(email)
            ?: throw BadCredentialsException("Invalid credentials.")
        if (!hashEncoder.matches(password, user.hashedPassword)) {
            throw BadCredentialsException("Invalid credentials.")
        }

        val newAccessToken = jwtService.generateAccessToken(user.id.toString())
        val newRefreshToken = jwtService.generaRefreshToken(user.id.toString())
        return TokenPair(
            newAccessToken,
            newRefreshToken
        )
    }
}