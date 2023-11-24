package seamonster.kraken.androidep7.data.models

data class Pageable(
    val offset: Int? = null,
    val pageNumber: Int? = null,
    val pageSize: Int? = null,
    val paged: Boolean? = null,
    val sort: Sort? = null,
    val unpaged: Boolean? = null
)
