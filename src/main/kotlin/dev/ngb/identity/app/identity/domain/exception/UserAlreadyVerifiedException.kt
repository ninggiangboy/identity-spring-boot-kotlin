package dev.ngb.identity.app.identity.domain.exception

import dev.ngb.identity.common.exception.BadRequestException

class UserAlreadyVerifiedException : BadRequestException("User is already verified")