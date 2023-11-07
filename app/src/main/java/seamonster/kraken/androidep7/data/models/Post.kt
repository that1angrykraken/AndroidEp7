package seamonster.kraken.androidep7.data.models

data class Post(
    val content: String,
    val comments: List<Comment>,
    val date: String,
    val id: Int,
    val likes: List<Like>,

)
