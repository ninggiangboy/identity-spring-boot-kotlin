package dev.ngb.identity.common.exception

import org.springframework.http.HttpStatus


open class DuplicationErrorException : ErrorException {
    constructor() : super("Resource already exists", HttpStatus.CONFLICT)
    constructor(message: String) : super(message, HttpStatus.CONFLICT)
}