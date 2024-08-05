package dev.ngb.identity.common.payload

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

data class PageParam(
    @field:Min(1)
    val page: Int = 1,
    @field:Min(1)
    @field:Max(100)
    val size: Int = 10,
    val sort: List<String>? = null,
    val filter: List<String>? = null
)