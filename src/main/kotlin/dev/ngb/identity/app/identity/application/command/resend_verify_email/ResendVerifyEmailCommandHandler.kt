package dev.ngb.identity.app.identity.application.command.resend_verify_email

import an.awesome.pipelinr.Command
import dev.ngb.identity.service.TokenService
import dev.ngb.identity.service.TokenType
import dev.ngb.identity.app.identity.application.service.IdentityEmailService
import dev.ngb.identity.app.identity.domain.exception.NotFoundUserException
import dev.ngb.identity.app.identity.domain.exception.UserAlreadyVerifiedException
import dev.ngb.identity.app.identity.domain.repository.AppUserRepository
import dev.ngb.identity.common.annotation.UseCase

@UseCase
class ResendVerifyEmailCommandHandler(
    private val userRepository: AppUserRepository,
    private val tokenService: TokenService,
    private val identityEmailService: IdentityEmailService,
) : Command.Handler<ResendVerifyEmailCommand, Unit> {
    override fun handle(command: ResendVerifyEmailCommand) {
        val user = userRepository.findByEmail(command.email) ?: throw NotFoundUserException(command.email)
        if (user.isEnabled!!) {
            throw UserAlreadyVerifiedException()
        }
        val token = tokenService.generateToken(user.id.toString(), TokenType.VERIFICATION)
        identityEmailService.sendVerificationEmail(user.email!!, token)
    }
}