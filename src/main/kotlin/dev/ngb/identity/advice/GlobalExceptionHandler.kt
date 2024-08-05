package dev.ngb.identity.advice

import com.fasterxml.jackson.annotation.JsonProperty
import dev.ngb.identity.common.api.ErrorResponse
import dev.ngb.identity.common.exception.ErrorException
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authorization.AuthorizationDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.resource.NoResourceFoundException

@RestControllerAdvice
class GlobalExceptionHandler {

    val logger: Logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception, request: HttpServletRequest): ResponseEntity<ErrorResponse> {
        logger.error("Internal server error", e)
        val errorResponse = ErrorResponse(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            path = request.requestURI,
            message = e.message ?: "Internal server error",
        )
        return ResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(ErrorException::class)
    fun handleClientErrorException(e: ErrorException, request: HttpServletRequest): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = e.status.value(),
            path = request.requestURI,
            message = e.message ?: "Client error",
        )
        return ResponseEntity(errorResponse, e.status)
    }

    @ExceptionHandler(NoResourceFoundException::class)
    fun handleNoResourceFoundException(
        e: NoResourceFoundException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.NOT_FOUND.value(),
            path = request.requestURI,
            message = e.message ?: "Resource not found",
        )
        return ResponseEntity(errorResponse, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(
        e: IllegalArgumentException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            path = request.requestURI,
            message = e.message ?: "Invalid argument",
        )
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(
        e: MethodArgumentNotValidException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        val targetObject = e.bindingResult.target
        val errors = e.bindingResult.fieldErrors
            .groupBy({ resolveJsonName(it.field, targetObject) }, { it.defaultMessage ?: "Invalid value" })
        val errorResponse = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            path = request.requestURI,
            message = "Validation error",
            errors = errors
        )
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    private fun resolveJsonName(fieldName: String, targetObject: Any?): String {
        if (targetObject == null) return fieldName
        return try {
            val field = targetObject.javaClass.getDeclaredField(fieldName)
            field.isAccessible = true
            return field.getAnnotation(JsonProperty::class.java)?.value ?: fieldName
        } catch (e: NoSuchFieldException) {
            fieldName
        }
    }

    @ExceptionHandler(AuthorizationDeniedException::class)
    fun handleAuthorizationDeniedException(
        e: AuthorizationDeniedException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.FORBIDDEN.value(),
            path = request.requestURI,
            message = e.message ?: "Access denied",
        )
        return ResponseEntity(errorResponse, HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(AuthenticationException::class)
    fun handleAuthenticationException(
        e: AuthenticationException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.UNAUTHORIZED.value(),
            path = request.requestURI,
            message = e.message ?: "Unauthorized",
        )
        return ResponseEntity(errorResponse, HttpStatus.UNAUTHORIZED)
    }
}