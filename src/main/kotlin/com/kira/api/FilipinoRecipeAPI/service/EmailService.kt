package com.kira.api.FilipinoRecipeAPI.service

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailService(private val mailSender: JavaMailSender) {
    fun sendOtpEmail(targetEmai: String, otpCode: String) {
        val message = SimpleMailMessage().apply {
            setTo(targetEmai)
            subject = "Your Recipe App Verification Code"
            text = """
                        Hello,
        
                        You requested a password reset. Use the 6-digit verification code below to proceed:
                        
                        $otpCode
                        
                        This code is valid for 5 minutes. If you did not make this request, please ignore this email.
                        
                        Happy Cooking!
                    """.trimIndent()
        }
        mailSender.send(message)
    }
}