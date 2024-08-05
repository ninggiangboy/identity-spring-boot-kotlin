package dev.ngb.identity.app.identity.api

import an.awesome.pipelinr.Pipeline
import dev.ngb.identity.common.api.ApiResource
import dev.ngb.identity.common.api.DataResponse
import dev.ngb.identity.app.identity.application.command.login.LoginCommand
import dev.ngb.identity.app.identity.application.command.login.LoginRequest
import dev.ngb.identity.app.identity.application.command.logout.LogoutCommand
import dev.ngb.identity.app.identity.application.command.logout_all.LogoutAllCommand
import dev.ngb.identity.app.identity.application.command.refresh_token.RefreshTokenCommand
import dev.ngb.identity.app.identity.application.command.register.RegisterCommand
import dev.ngb.identity.app.identity.application.command.register.RegisterRequest
import dev.ngb.identity.app.identity.application.command.resend_verify_email.ResendVerifyEmailCommand
import dev.ngb.identity.app.identity.application.command.verify_email.VerifyEmailCommand
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
class IdentityResource(private val pipeline: Pipeline) : IdentityApi, ApiResource() {
    override fun whoAmI(user: Principal): DataResponse {
        val claims = getClaims(user)
        return toDataResponse(claims, "Hello, ${user.name}!")
    }

    override fun login(request: LoginRequest): DataResponse {
        val command = LoginCommand(request)
        val response = pipeline.send(command)
        return toDataResponse(response, "Logged in successfully!")
    }

    override fun refreshToken(token: String): DataResponse {
        val command = RefreshTokenCommand(token)
        val response = pipeline.send(command)
        return toDataResponse(response, "Token refreshed successfully!")
    }

    override fun logout(token: String, user: Principal): DataResponse {
        val userId = getUserId(user)
        val command = LogoutCommand(token, userId)
        pipeline.send(command)
        return toDataResponse("Logged out successfully!")
    }

    override fun logoutAll(user: Principal): DataResponse {
        val userId = getUserId(user)
        val command = LogoutAllCommand(userId)
        pipeline.send(command)
        return toDataResponse("Logged out from all devices successfully!")
    }

    override fun register(request: RegisterRequest): DataResponse {
        val command = RegisterCommand(request)
        pipeline.send(command)
        return toDataResponse("User registered successfully!")
    }

    override fun verifyEmail(token: String): DataResponse {
        val command = VerifyEmailCommand(token)
        pipeline.send(command)
        return toDataResponse("Email verified successfully!")
    }

    override fun resendVerificationEmail(email: String): DataResponse {
        val command = ResendVerifyEmailCommand(email)
        pipeline.send(command)
        return toDataResponse("Verification email sent successfully!")
    }

    override fun resetPassword(email: String): DataResponse {
        TODO("Not yet implemented")
    }

    override fun confirmResetPassword(token: String): DataResponse {
        TODO("Not yet implemented")
    }

    override fun confirmResetPassword(token: String, password: String): DataResponse {
        TODO("Not yet implemented")
    }

    override fun changePassword(
        oldPassword: String,
        newPassword: String,
        user: Principal
    ): DataResponse {
        TODO("Not yet implemented")
    }

    override fun changeEmail(newEmail: String, user: Principal): DataResponse {
        TODO("Not yet implemented")
    }
}