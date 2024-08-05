package dev.ngb.identity.common.api

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.Instant


@JsonInclude(JsonInclude.Include.NON_NULL)
data class ErrorResponse(
    val status: Int,
    val path: String,
    val errors: Map<String, List<String>>? = null,
    val message: String
)
