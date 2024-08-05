package dev.ngb.identity.app.identity.application.command.refresh_token

import an.awesome.pipelinr.Command
import dev.ngb.identity.common.annotation.UseCase
import dev.ngb.identity.service.JwtService
import dev.ngb.identity.service.TokenService
import dev.ngb.identity.service.TokenType
import dev.ngb.identity.app.identity.domain.exception.InvalidTokenException
import dev.ngb.identity.app.identity.domain.repository.AppUserRepository
import org.springframework.security.authentication.DisabledException
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import java.util.*

@UseCase
class RefreshTokenCommandHandler(
    private val jwtService: JwtService,
    private val tokenService: TokenService,
    private val appUserRepository: AppUserRepository
) : Command.Handler<RefreshTokenCommand, RefreshTokenResponse> {
    override fun handle(command: RefreshTokenCommand): RefreshTokenResponse {
        val userId = tokenService.getUserIdFromToken(command.refreshToken, TokenType.REFRESH)
            ?: throw InvalidTokenException()
        val user = appUserRepository.findById(UUID.fromString(userId))!!.let {
            User.builder()
                .username(it.id.toString())
                .password(it.hashPassword)
                .disabled(!it.isEnabled!!)
                .authorities(it.roles.map { role -> SimpleGrantedAuthority("ROLE_${role.name}") })
                .build()
        }

        if (user.isEnabled) throw DisabledException("User is disabled")
        val accessToken = jwtService.generateAccessToken(user)
        val refreshToken = tokenService.generateToken(user.username, TokenType.REFRESH)
        tokenService.revokeToken(command.refreshToken, TokenType.REFRESH)
        return RefreshTokenResponse(accessToken, refreshToken)
    }
}