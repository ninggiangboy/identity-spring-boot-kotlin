package dev.ngb.identity.common.api

import dev.ngb.identity.common.payload.PageResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import java.security.Principal
import java.util.*

open class ApiResource {

    protected fun getUserId(user: Principal?): UUID {
        user as JwtAuthenticationToken?
        return UUID.fromString(user?.name)
    }

    protected fun getClaims(user: Principal?): Map<String, Any> {
        user as JwtAuthenticationToken?
        return user?.tokenAttributes ?: emptyMap()
    }

    protected fun getRoles(user: Principal?): List<String> {
        user as JwtAuthenticationToken?
        return user?.authorities?.map { it.authority } ?: emptyList()
    }


    protected fun getUserDetails(user: Principal?): UserDetails {
        user as JwtAuthenticationToken? ?: throw IllegalArgumentException("Unauthorized")
        return User(user?.name, "", user?.authorities)
    }

    fun toDataResponse(data: Any?, message: String?): DataResponse {
        val response = DataResponse(
            data = if (data is PageResponse<*>) data.content else data,
            meta = DataResponse.Meta(
                message = message ?: "Success",
                extraMeta = if (data is PageResponse<*>) data.meta else null
            )
        )
        return response
    }

    fun toDataResponse(message: String) = toDataResponse(null, message)

    fun toDataResponse(data: Any) = toDataResponse(data, "Success")
}