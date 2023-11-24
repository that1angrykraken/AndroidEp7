package seamonster.kraken.androidep7.util

object Patterns {
    const val emailAddress: String = "^[\\w._-]+@[\\w._-]+\\.[\\w^0-9]+\$"
    const val number: String = "^[\\s0-9.+-]+$"
    const val password: String = "^.{8,}$"
    const val nonEmpty: String = "^.+$"
    const val username: String = "^[\\w._-]{6,}$"
    const val displayName: String = "^.{4,}$"
    const val year: String = "^([1-8])$"

    @Suppress("unused")
    private fun checkRegex() {
        emailAddress.toRegex()
        username.toRegex()
        year.toRegex()
        nonEmpty.toRegex()
        displayName.toRegex()
    }
}