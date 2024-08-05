package dev.ngb.identity.app.identity.application.command.login

import an.awesome.pipelinr.Command
import dev.ngb.identity.app.identity.application.command.login.LoginRequest
import dev.ngb.identity.app.identity.application.command.login.LoginResponse

data class LoginCommand(
    val request: LoginRequest
) : Command<LoginResponse>
