package dev.ngb.identity.app.identity.domain.exception

import dev.ngb.identity.common.exception.DuplicationErrorException

class DuplicationUsernameException(username: String) : DuplicationErrorException("Username $username already exists")