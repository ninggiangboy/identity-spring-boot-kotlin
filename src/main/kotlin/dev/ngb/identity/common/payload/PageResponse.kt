package dev.ngb.identity.common.payload


data class PageResponse<T>(
    val content: List<T>,
    val meta: Meta
) {

    class Meta(
        val page: Int,
        val size: Int,
        val total: Long,
        val totalPages: Int,
        val hasNext: Boolean,
        val hasPrev: Boolean,
        val sort: List<String> = emptyList(),
        val validSort: Set<String> = emptySet(),
        val filter: List<String> = emptyList(),
        val validFilter: Set<String> = emptySet()
    )
}