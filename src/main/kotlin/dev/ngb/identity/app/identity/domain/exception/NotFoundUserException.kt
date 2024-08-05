package dev.ngb.identity.app.identity.domain.exception

import dev.ngb.identity.common.exception.NotFoundErrorException

class NotFoundUserException(email: String) : NotFoundErrorException("User with email $email not found")