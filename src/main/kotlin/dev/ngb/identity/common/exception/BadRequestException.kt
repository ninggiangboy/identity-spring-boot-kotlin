package dev.ngb.identity.common.exception

import org.springframework.http.HttpStatus

open class BadRequestException : ErrorException {
    constructor() : super("Bad request", HttpStatus.BAD_REQUEST)
    constructor(message: String) : super(message, HttpStatus.BAD_REQUEST)
}