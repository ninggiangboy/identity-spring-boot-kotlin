package dev.ngb.identity.app.identity.domain.exception

import dev.ngb.identity.common.exception.BadRequestException

class InvalidTokenException : BadRequestException("Invalid token")