package dev.ngb.identity.app.identity.application.command.logout

import an.awesome.pipelinr.Command
import dev.ngb.identity.service.TokenService
import dev.ngb.identity.common.annotation.UseCase
import dev.ngb.identity.service.TokenType
import dev.ngb.identity.app.identity.domain.exception.InvalidTokenException
import org.springframework.security.access.AccessDeniedException

@UseCase
class LogoutCommandHandler(private val tokenService: TokenService) : Command.Handler<LogoutCommand, Unit> {
    override fun handle(command: LogoutCommand) {
        val userId = tokenService.getUserIdFromToken(command.token, TokenType.REFRESH)
            ?: throw InvalidTokenException()
        if (userId != command.userId.toString()) {
            throw AccessDeniedException("Invalid token for user")
        }
        tokenService.revokeToken(command.token, TokenType.REFRESH)
    }
}