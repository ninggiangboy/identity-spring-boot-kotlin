package dev.ngb.identity.app.identity.application.command.register

import an.awesome.pipelinr.Command
import dev.ngb.chatapp.app.identity.domain.entity.AppUser
import dev.ngb.identity.service.TokenService
import dev.ngb.identity.service.TokenType
import dev.ngb.identity.app.identity.application.service.IdentityEmailService
import dev.ngb.identity.app.identity.domain.exception.DuplicationEmailException
import dev.ngb.identity.app.identity.domain.exception.DuplicationUsernameException
import dev.ngb.identity.app.identity.domain.repository.AppUserRepository
import dev.ngb.identity.common.annotation.UseCase
import org.springframework.security.crypto.password.PasswordEncoder

@UseCase
class RegisterCommandHandler(
    private val appUserRepository: AppUserRepository,
    private val tokenService: TokenService,
    private val encoder: PasswordEncoder,
    private val identityEmailService: IdentityEmailService
) : Command.Handler<RegisterCommand, Unit> {
    override fun handle(command: RegisterCommand) {
        val request = command.request
        if (appUserRepository.existsByUsername(request.username!!)) {
            throw DuplicationUsernameException(request.username)
        }
        val existUser = appUserRepository.findByEmail(request.email!!)
        existUser?.let { existingUser ->
            when {
                isVerifiedUser(existingUser) || isWaitingForVerification(existingUser) -> {
                    throw DuplicationEmailException(request.email)
                }

                else -> {
                    appUserRepository.deleteById(existingUser.id!!)
                }
            }
        }

        val user: AppUser = request.toEntity()
        appUserRepository.save(user)
        val token = tokenService.generateToken(user.id.toString(), TokenType.VERIFICATION)
        identityEmailService.sendVerificationEmail(user.email!!, token)
    }

    private fun isVerifiedUser(existUser: AppUser): Boolean {
        return existUser.isEnabled!!
    }

    private fun isWaitingForVerification(existUser: AppUser): Boolean {
        return !existUser.isEnabled!! && tokenService.isUserHasToken(
            existUser.id.toString(),
            TokenType.VERIFICATION
        )
    }

    private fun RegisterRequest.toEntity() = AppUser().apply {
        username = this@toEntity.username
        email = this@toEntity.email
        isEnabled = false
        hashPassword = encoder.encode(this@toEntity.password)
    }
}