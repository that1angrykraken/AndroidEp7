package seamonster.kraken.androidep7.data.models

import java.util.Calendar

data class Notification(
    val body: String? = null,
    val date: Calendar? = null,
    val id: Int? = null,
    val title: String? = null,
    val type: String? = null,
    val user: User? = null
)