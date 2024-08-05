package dev.ngb.identity.app.identity.application.command.refresh_token

data class RefreshTokenResponse(
    val accessToken: String,
    val refreshToken: String
)