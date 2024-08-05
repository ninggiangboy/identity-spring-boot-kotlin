package dev.ngb.identity.app.identity.application.command.register

import dev.ngb.identity.app.identity.domain.constant.RegexPattern
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern

data class RegisterRequest(
    @field:NotBlank(message = "Username is required")
    @field:Pattern(regexp = RegexPattern.USERNAME_PATTERN, message = "Username must contain only letters and numbers")
    val username: String?,
    @field:Email(message = "Email is invalid")
    val email: String?,
    @field:NotNull(message = "Password is required")
    @field:Pattern(
        regexp = RegexPattern.PASSWORD_PATTERN,
        message = "Password must contain at least one letter, one number, and be at least 8 characters long"
    )
    val password: String?
)