package dev.ngb.identity.app.identity.application.command.logout_all

import an.awesome.pipelinr.Command
import java.util.UUID

data class LogoutAllCommand(val userId: UUID) : Command<Unit>