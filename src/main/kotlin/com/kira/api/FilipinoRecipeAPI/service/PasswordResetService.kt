package com.kira.api.FilipinoRecipeAPI.service

import com.kira.api.FilipinoRecipeAPI.dto.requests.ForgotPasswordRequest
import com.kira.api.FilipinoRecipeAPI.dto.requests.ResetPasswordRequest
import com.kira.api.FilipinoRecipeAPI.dto.requests.VerifyOtpRequest
import com.kira.api.FilipinoRecipeAPI.dto.response.ApiResponse
import com.kira.api.FilipinoRecipeAPI.dto.response.OtpVerificationResponse
import com.kira.api.FilipinoRecipeAPI.model.PasswordResetOtp
import com.kira.api.FilipinoRecipeAPI.model.enums.ResponseStatus
import com.kira.api.FilipinoRecipeAPI.repository.password.PasswordResetOtpRepository
import com.kira.api.FilipinoRecipeAPI.repository.user.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.security.SecureRandom
import java.util.*

@Service
class PasswordResetService(
    private val otpRepository: PasswordResetOtpRepository,
    private val userRepository: UserRepository,
    private val emailService: EmailService,
    private val passwordEncoder: PasswordEncoder
) {
    private val secureRandom = SecureRandom()

    fun initiatePasswordReset(request: ForgotPasswordRequest): ApiResponse<Unit> {
        val email = request.email.trim().lowercase()
        val userExists = userRepository.existsByEmail(email)

        if (!userExists) {
            return ApiResponse(
                status = ResponseStatus.SUCCESS,
                message = "If an account matches that email, a verification code has been dispatched.",
                data = null
            )
        }

        otpRepository.deleteByEmail(email)

        val generatedOtp = (100000 + secureRandom.nextInt(900000)).toString()

        otpRepository.save(PasswordResetOtp(email = email, otp = generatedOtp))

        try {
            emailService.sendPasswordResetOtp(email, generatedOtp)
        } catch (e: Exception) {
            return ApiResponse(
                ResponseStatus.FAILED,
                "Failed to send verification email. Please try again later.",
                null
            )
        }

        return ApiResponse(
            status = ResponseStatus.SUCCESS,
            message = "If an account matches that email, a verification code has been dispatched.",
            data = null
        )
    }

    fun validateOtpCode(request: VerifyOtpRequest): ApiResponse<OtpVerificationResponse> {
        val email = request.email.trim().lowercase()
        val record = otpRepository.findByEmail(email)
            ?: return ApiResponse(ResponseStatus.FAILED, "Code has expired or does not exist.", null)

        if (record.otp != request.otp) {
            return ApiResponse(ResponseStatus.FAILED, "Invalid code. Please verify and try again.", null)
        }

        val transientResetToken = UUID.randomUUID().toString()

        otpRepository.save(record.copy(resetToken = transientResetToken))

        return ApiResponse(
            status = ResponseStatus.SUCCESS,
            message = "Code verified successfully.",
            data = OtpVerificationResponse(resetToken = transientResetToken)
        )
    }

    fun completePasswordReset(request: ResetPasswordRequest): ApiResponse<Unit> {
        val email = request.email.trim().lowercase()
        val otpRecord = otpRepository.findByEmail(email)
            ?: return ApiResponse(ResponseStatus.FAILED, "Session expired. Please request a new code.", null)

        if (otpRecord.resetToken == null || otpRecord.resetToken != request.resetToken) {
            return ApiResponse(ResponseStatus.FAILED, "Unauthorized reset transaction.", null)
        }

        val user = userRepository.findByEmail(email)
            ?: return ApiResponse(ResponseStatus.FAILED, "User record lookup failed.", null)

        val updatedUser = user.copy(hashedPassword = passwordEncoder.encode(request.newPassword))
        userRepository.save(updatedUser)

        otpRepository.delete(otpRecord)

        return ApiResponse(
            status = ResponseStatus.SUCCESS,
            message = "Your password has been successfully reset.",
            data = null
        )
    }
}