package dev.ngb.identity.app.identity.application.command.verify_email

import an.awesome.pipelinr.Command
import dev.ngb.identity.service.TokenService
import dev.ngb.identity.service.TokenType
import dev.ngb.identity.app.identity.domain.exception.InvalidTokenException
import dev.ngb.identity.app.identity.domain.exception.UserAlreadyVerifiedException
import dev.ngb.identity.app.identity.domain.repository.AppUserRepository
import dev.ngb.identity.common.annotation.UseCase
import java.util.*

@UseCase
class VerifyEmailCommandHandler(
    private val appUserRepository: AppUserRepository,
    private val tokenService: TokenService
) : Command.Handler<VerifyEmailCommand, Unit> {
    override fun handle(command: VerifyEmailCommand) {
        val userId = tokenService.getUserIdFromToken(command.token, TokenType.VERIFICATION)
            ?: throw InvalidTokenException()
        val user = appUserRepository.findById(UUID.fromString(userId))
            ?: throw InvalidTokenException()
        if (user.isEnabled!!) throw UserAlreadyVerifiedException()
        user.isEnabled = true
        appUserRepository.save(user)
        tokenService.revokeUserTokens(user.id.toString())
    }
}