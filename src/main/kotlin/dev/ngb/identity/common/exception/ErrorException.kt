package dev.ngb.identity.common.exception

import org.springframework.http.HttpStatus


open class ErrorException(message: String, val status: HttpStatus = HttpStatus.BAD_REQUEST) : RuntimeException(message)