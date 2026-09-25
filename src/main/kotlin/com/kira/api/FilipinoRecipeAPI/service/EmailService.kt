package com.kira.api.FilipinoRecipeAPI.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Service
class EmailService(
    @Value("\${resend.api-key}") private val resendApiKey: String
) {
    private val httpClient = HttpClient.newHttpClient()

    @Async
    fun sendPasswordResetOtp(toEmail: String, otp: String) {
        val jsonPayload = """
            {
                "from": "Filipino Recipe <noreply@recipe.juliusv.dev>",
                "to": ["$toEmail"],
                "subject": "Filipino Recipe - Password Reset Code",
                "text": "Your password reset code is: $otp\n\nThis code is valid for 15 minutes."
            }
        """.trimIndent()

        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.resend.com/emails"))
            .header("Authorization", "Bearer $resendApiKey")
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
            .build()

        try {
            val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
            if (response.statusCode() !in 200..299) {
                println("Failed to send email via Resend API: ${response.body()}")
            }
        } catch (e: Exception) {
            println("Exception sending email via Resend API: ${e.message}")
        }
    }
}