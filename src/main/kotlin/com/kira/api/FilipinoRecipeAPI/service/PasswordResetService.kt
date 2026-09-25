package com.kira.api.FilipinoRecipeAPI.service

import com.kira.api.FilipinoRecipeAPI.dto.requests.ForgotPasswordRequest
import com.kira.api.FilipinoRecipeAPI.dto.requests.ResetPasswordRequest
import com.kira.api.FilipinoRecipeAPI.dto.requests.VerifyOtpRequest
import com.kira.api.FilipinoRecipeAPI.dto.response.OtpVerificationResponse
import com.kira.api.FilipinoRecipeAPI.exception.ResourceNotFoundException
import com.kira.api.FilipinoRecipeAPI.model.PasswordResetToken
import com.kira.api.FilipinoRecipeAPI.repository.password.PasswordResetTokenRepository
import com.kira.api.FilipinoRecipeAPI.repository.user.UserRepository
import org.apache.coyote.BadRequestException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*
import kotlin.random.Random

@Service
class PasswordResetService(
    private val passwordResetTokenRepository: PasswordResetTokenRepository,
    private val userRepository: UserRepository,
    private val emailService: EmailService,
    private val passwordEncoder: PasswordEncoder
) {

    fun initiatePasswordReset(request: ForgotPasswordRequest) {
        println("DEBUG: Starting password reset for email: ${request.email}")

        val user = userRepository.findByEmail(request.email)
        println("DEBUG: User lookup result: ${user != null}")

        if (user == null) {
            println("DEBUG: User not found, returning early.")
            return
        }

        val otp = String.format("%06d", Random.nextInt(1000000))
        println("DEBUG: Generated OTP: $otp")

        passwordResetTokenRepository.findByEmail(request.email).ifPresent {
            println("DEBUG: Deleting existing token for email")
            passwordResetTokenRepository.delete(it)
        }

        val resetTokenEntity = PasswordResetToken(
            email = request.email,
            otp = otp,
            createdAt = Instant.now()
        )

        println("DEBUG: Attempting to save token to MongoDB...")
        passwordResetTokenRepository.save(resetTokenEntity)
        println("DEBUG: Token successfully saved to MongoDB.")

        println("DEBUG: Calling email service...")
        emailService.sendPasswordResetOtp(request.email, otp)
        println("DEBUG: Email service call completed.")
    }

    fun validateOtpCode(request: VerifyOtpRequest): OtpVerificationResponse {
        val tokenEntity = passwordResetTokenRepository.findByEmail(request.email)
            .orElseThrow { BadRequestException("Invalid or expired OTP request.") }

        if (tokenEntity.createdAt.plusSeconds(15 * 60).isBefore(Instant.now())) {
            passwordResetTokenRepository.delete(tokenEntity)
            throw BadRequestException("OTP has expired.")
        }

        if (tokenEntity.otp != request.otp) {
            throw BadRequestException("Invalid OTP code.")
        }

        val secureResetToken = UUID.randomUUID().toString()
        val updatedEntity = tokenEntity.copy(
            resetToken = secureResetToken,
            isVerified = true
        )
        passwordResetTokenRepository.save(updatedEntity)

        return OtpVerificationResponse(resetToken = secureResetToken)
    }

    fun completePasswordReset(request: ResetPasswordRequest) {
        val tokenEntity = passwordResetTokenRepository.findByResetToken(request.resetToken)
            .orElseThrow { BadRequestException("Invalid or expired reset token.") }

        if (!tokenEntity.isVerified || tokenEntity.email != request.email) {
            throw BadRequestException("Unauthorized password reset attempt.")
        }

        if (tokenEntity.createdAt.plusSeconds(15 * 60).isBefore(Instant.now())) {
            passwordResetTokenRepository.delete(tokenEntity)
            throw BadRequestException("Reset token has expired.")
        }


        val user = userRepository.findByEmail(request.email)
            ?: throw ResourceNotFoundException("User not found.")

        val encodedPassword = passwordEncoder.encode(request.newPassword)
        val updatedUser = user.copy(hashedPassword = encodedPassword)
        userRepository.save(updatedUser)

        passwordResetTokenRepository.delete(tokenEntity)
    }
}