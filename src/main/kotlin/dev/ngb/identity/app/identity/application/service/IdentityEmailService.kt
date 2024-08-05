package dev.ngb.identity.app.identity.application.service

import dev.ngb.identity.config.property.LinkProperties
import dev.ngb.identity.service.EmailService
import org.springframework.stereotype.Service

interface IdentityEmailService {
    fun sendVerificationEmail(email: String, token: String)
    fun sendPasswordResetEmail(email: String, token: String)
}

@Service
class IdentityEmailServiceImpl(
    private val emailService: EmailService,
    private val properties: LinkProperties
) : IdentityEmailService {
    override fun sendVerificationEmail(email: String, token: String) {
        emailService.sendEmail(
            email,
            "Confirmation Email",
            """
                Please click the link below to confirm your email address. 
                ${properties.verifyEmail}$token
            """.trimIndent()
        )
    }

    override fun sendPasswordResetEmail(email: String, token: String) {
        emailService.sendEmail(
            email,
            "Password Reset",
            """
                Please click the link below to reset your password. 
                ${properties.resetPassword}$token
            """.trimIndent()
        )
    }
}