package dev.ngb.identity.common.exception

import org.springframework.http.HttpStatus

open class NotFoundErrorException : ErrorException {
    constructor() : super("Resource not found", HttpStatus.NOT_FOUND)
    constructor(message: String) : super(message, HttpStatus.NOT_FOUND)
}