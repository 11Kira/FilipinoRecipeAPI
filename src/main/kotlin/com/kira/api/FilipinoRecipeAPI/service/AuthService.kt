package com.kira.api.FilipinoRecipeAPI.service

import com.kira.api.FilipinoRecipeAPI.dto.requests.LoginRequest
import com.kira.api.FilipinoRecipeAPI.dto.requests.RefreshTokenRequest
import com.kira.api.FilipinoRecipeAPI.dto.requests.RegistrationRequest
import com.kira.api.FilipinoRecipeAPI.dto.response.AuthResponse
import com.kira.api.FilipinoRecipeAPI.dto.response.RefreshTokenResponse
import com.kira.api.FilipinoRecipeAPI.exception.UserAlreadyExistsException
import com.kira.api.FilipinoRecipeAPI.model.RefreshToken
import com.kira.api.FilipinoRecipeAPI.model.User
import com.kira.api.FilipinoRecipeAPI.model.enums.Role
import com.kira.api.FilipinoRecipeAPI.repository.password.PasswordResetOtpRepository
import com.kira.api.FilipinoRecipeAPI.repository.token.RefreshTokenRepository
import com.kira.api.FilipinoRecipeAPI.repository.user.UserRepository
import com.kira.api.FilipinoRecipeAPI.security.HashEncoder
import org.apache.coyote.BadRequestException
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.MessageDigest
import java.security.SecureRandom
import java.time.Instant
import java.util.*

@Service
class AuthService(
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val hashEncoder: HashEncoder,
) {
    @Transactional
    fun registerUser(
        request: RegistrationRequest
    ): AuthResponse {
        if (userRepository.existsByEmail(request.email)) {
            throw UserAlreadyExistsException("An account with this email already exists.")
        }
        if (userRepository.existsByUsername(request.username)) {
            throw UserAlreadyExistsException("This username is already taken.")
        }

        val user = User(
            email = request.email,
            hashedPassword = hashEncoder.encode(request.password),
            username = request.username,
            role = Role.USER
        )

        val saved = userRepository.save(user)
        val accessToken = jwtService.generateAccessToken(saved.id!!, user.role.name)
        val refreshToken = jwtService.generateRefreshToken(saved.id)

        storeRefreshToken(saved.id, refreshToken)
        return AuthResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
    }

    @Transactional
    fun loginUser(
        request: LoginRequest
    ): AuthResponse {
        val user = userRepository.findByEmail(request.email)
            ?: throw BadCredentialsException("Invalid credentials.")
        if (!hashEncoder.matches(request.password, user.hashedPassword)) {
            throw BadCredentialsException("Invalid credentials.")
        }

        val accessToken = jwtService.generateAccessToken(user.id!!, user.role.name)
        val refreshToken = jwtService.generateRefreshToken(user.id)

        storeRefreshToken(user.id, refreshToken)

        return AuthResponse(
            accessToken,
            refreshToken
        )
    }

    @Transactional
    fun refreshToken(request: RefreshTokenRequest): RefreshTokenResponse {
        if (!jwtService.validateRefreshToken(request.refreshToken)) {
            throw BadRequestException("Invalid or expired refresh token.")
        }

        val userId = jwtService.getUserIdFromToken(request.refreshToken)
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User not found.") }
        val hashed = hashToken(request.refreshToken)

        val tokenDoc = refreshTokenRepository.findByHashedToken(hashed)
            ?: throw IllegalArgumentException("Refresh token not recognized.")

        if (tokenDoc.expiresAt.isBefore(Instant.now())) {
            refreshTokenRepository.delete(tokenDoc)
            throw BadRequestException("Refresh token has expired.")
        }

        val newAccessToken = jwtService.generateAccessToken(tokenDoc.userId, user.role.name)

        return RefreshTokenResponse(accessToken = newAccessToken)
    }

    private fun storeRefreshToken(userId: String, rawRefreshToken: String) {
        val hashed = hashToken(rawRefreshToken)
        val expiryMs = jwtService.refreshTokenValidityMs
        val expiresAt = Instant.now().plusMillis(expiryMs)

        refreshTokenRepository.save(
            RefreshToken(
                userId = userId,
                expiresAt = expiresAt,
                createdAt = Instant.now(),
                hashedToken = hashed
            )
        )
    }

    private fun hashToken(token: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(token.encodeToByteArray())
        return Base64.getEncoder().encodeToString(hashBytes)
    }

    @Transactional
    fun revokeToken(rawRefreshToken: String) {
        val hashedToken = hashToken(rawRefreshToken)
        refreshTokenRepository.deleteByHashedToken(hashedToken)
    }
}