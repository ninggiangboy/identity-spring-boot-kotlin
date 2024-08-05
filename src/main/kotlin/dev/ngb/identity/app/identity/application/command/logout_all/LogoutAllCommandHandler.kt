package dev.ngb.identity.app.identity.application.command.logout_all

import an.awesome.pipelinr.Command
import dev.ngb.identity.service.TokenService
import dev.ngb.identity.common.annotation.UseCase

@UseCase
class LogoutAllCommandHandler(private val tokenService: TokenService) : Command.Handler<LogoutAllCommand, Unit> {
    override fun handle(command: LogoutAllCommand) {
        tokenService.revokeUserTokens(command.userId.toString())
    }
}