package seamonster.kraken.androidep7.data.models

import java.util.Date

data class TimeSheet(
    val dateAttendance: String? = null,
    val id: Int? = null,
    val ip: String? = null,
    val message: String? = null,
    val offline: Boolean? = true,
    val user: User? = null
)
