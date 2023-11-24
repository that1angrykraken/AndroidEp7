package seamonster.kraken.androidep7.data.models

data class Page<T>(
    val content: List<T>? = null,
    val empty: Boolean? = null,
    val first: Boolean = false,
    val last: Boolean = false,
    val number: Int? = null,
    val numberOfElements: Int? = null,
    val pageable: Pageable? = null,
    val size: Int? = null,
    val sort: Sort? = null,
    val totalElements: Int? = null,
    val totalPages: Int
)
