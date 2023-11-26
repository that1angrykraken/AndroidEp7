package seamonster.kraken.androidep7.data.models

import java.util.Calendar

data class Tracking(
    val content: String? = null,
    val date: Calendar? = null,
    val id: Int? = null,
    val user: User? = null
)