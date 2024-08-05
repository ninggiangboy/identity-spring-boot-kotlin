package dev.ngb.identity.common.api

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class DataResponse(
    val meta: Meta,
    val data: Any?
) {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    data class Meta(
        val message: String,
        val extraMeta: Any? = null
    )
}
