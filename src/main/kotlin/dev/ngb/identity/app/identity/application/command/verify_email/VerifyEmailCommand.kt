package dev.ngb.identity.app.identity.application.command.verify_email

import an.awesome.pipelinr.Command

data class VerifyEmailCommand(
    val token: String
) : Command<Unit>