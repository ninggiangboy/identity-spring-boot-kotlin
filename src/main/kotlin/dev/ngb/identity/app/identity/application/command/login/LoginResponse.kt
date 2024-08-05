package dev.ngb.identity.app.identity.application.command.login

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String
)
