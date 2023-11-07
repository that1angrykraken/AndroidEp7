package seamonster.kraken.androidep7.data.models

data class Comment(
    val content: String,
    val date: String,
    val id: Int,
    val post: Any?,
    val user: User
)
