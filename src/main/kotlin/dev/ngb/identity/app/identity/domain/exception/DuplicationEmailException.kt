package dev.ngb.identity.app.identity.domain.exception

import dev.ngb.identity.common.exception.DuplicationErrorException

class DuplicationEmailException(email: String) : DuplicationErrorException("Email $email already exists")