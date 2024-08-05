package dev.ngb.identity.app.identity.application.command.resend_verify_email

import an.awesome.pipelinr.Command

data class ResendVerifyEmailCommand(
    val email: String
) : Command<Unit>