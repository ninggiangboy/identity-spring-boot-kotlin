package dev.ngb.identity.app.identity.application.command.login

import an.awesome.pipelinr.Command
import dev.ngb.identity.common.annotation.UseCase
import dev.ngb.identity.service.JwtService
import dev.ngb.identity.service.TokenService
import dev.ngb.identity.service.TokenType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails

@UseCase
class LoginCommandHandler(
    private val jwtService: JwtService,
    private val tokenService: TokenService,
    private val authenticationManager: AuthenticationManager
) : Command.Handler<LoginCommand, LoginResponse> {
    override fun handle(command: LoginCommand): LoginResponse {
        val request = command.request
        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                request.username,
                request.password
            )
        )
        val user = authentication.principal as UserDetails
        val accessToken = jwtService.generateAccessToken(user)
        val refreshToken = tokenService.generateToken(user.username, TokenType.REFRESH)
        return LoginResponse(accessToken, refreshToken)
    }
}