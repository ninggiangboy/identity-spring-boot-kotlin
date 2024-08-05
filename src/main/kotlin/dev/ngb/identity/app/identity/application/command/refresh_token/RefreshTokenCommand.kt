package dev.ngb.identity.app.identity.application.command.refresh_token

import an.awesome.pipelinr.Command

data class RefreshTokenCommand(
    val refreshToken: String
) : Command<RefreshTokenResponse>