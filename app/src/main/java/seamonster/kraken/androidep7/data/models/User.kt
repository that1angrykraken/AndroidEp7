package seamonster.kraken.androidep7.data.models

import java.util.Date

data class User(
    val active: Boolean?,
    val birthPlace: String?,
    val changePass: Boolean?,
    val confirmPassword: String?,
    val countDayCheckin: Int?,
    val countDayTracking: Int?,
    val displayName: String?,
    val dob: Date?,
    val email: String?,
    val firstName: String?,
    val gender: String?,
    val hasPhoto: Boolean?,
    val id: Int?,
    val image: String?,
    val lastName: String?,
    val password: String?,
    val roles: List<Role>?,
    val setPassword: Boolean?,
    val tokenDevice: String?,
    val university: String?,
    val username: String?,
    val year: Int?
)