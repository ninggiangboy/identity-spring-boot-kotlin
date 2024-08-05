package dev.ngb.identity.app.identity.application.command.logout

import an.awesome.pipelinr.Command
import java.util.*

data class LogoutCommand(
    val token: String,
    val userId: UUID
) : Command<Unit>