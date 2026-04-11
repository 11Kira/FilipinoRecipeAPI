package com.kira.api.FilipinoRecipeAPI.security

import com.kira.api.FilipinoRecipeAPI.database.model.RefreshToken
import com.kira.api.FilipinoRecipeAPI.database.model.User
import com.kira.api.FilipinoRecipeAPI.database.repository.token.RefreshTokenRepository
import com.kira.api.FilipinoRecipeAPI.database.repository.user.UserRepository
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.MessageDigest
import java.time.Instant
import java.util.*

@Service
class AuthService(
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
    private val hashEncoder: HashEncoder,
    private val refreshTokenRepository: RefreshTokenRepository
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
        val newRefreshToken = jwtService.generateRefreshToken(user.id.toString())

        storeRefreshToken(user.id.toString(), newAccessToken)

        return TokenPair(
            newAccessToken,
            newRefreshToken
        )
    }

    @Transactional
    fun refresh(refreshToken: String): TokenPair {
        if (!jwtService.validateRefreshToken(refreshToken)) {
            throw IllegalArgumentException("Invalid refresh token.")
        }

        val userId = jwtService.getUserIdFromToken(refreshToken)
        val user = userRepository.findById(userId).orElseThrow { IllegalArgumentException("Invalid refresh token.") }

        val hashed = hashToken(refreshToken)
        refreshTokenRepository.findByUserIdAndHashedToken(user.id.toString(), hashed)
            ?: throw IllegalArgumentException("Refresh token not recognized (maybe used or expired).")
        refreshTokenRepository.deleteByUserIdAndHashedToken(user.id.toString(), hashed)

        val newAccessToken = jwtService.generateAccessToken(userId)
        val newRefreshToken = jwtService.generateRefreshToken(userId)

        storeRefreshToken(user.id.toString(), newRefreshToken)

        return TokenPair(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken
        )
    }

    private fun storeRefreshToken(userId: String, rawRefreshToken: String) {
        val hashed = hashToken(rawRefreshToken)
        val expiryMs = jwtService.refreshTokenValidityMs
        val expiresAt = Instant.now().plusMillis(expiryMs)

        refreshTokenRepository.save(
            RefreshToken(
                userId = userId,
                expiresAt = expiresAt,
                hashedToken = hashed
            )
        )
    }

    private fun hashToken(token: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(token.encodeToByteArray())
        return Base64.getEncoder().encodeToString(hashBytes)
    }
}