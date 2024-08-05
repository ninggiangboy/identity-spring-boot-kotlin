package dev.ngb.identity.app.identity.application.command.register

import an.awesome.pipelinr.Command

data class RegisterCommand(val request: RegisterRequest) : Command<Unit>