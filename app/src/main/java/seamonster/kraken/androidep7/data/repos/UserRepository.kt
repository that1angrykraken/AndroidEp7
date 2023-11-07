package seamonster.kraken.androidep7.data.repos

import seamonster.kraken.androidep7.data.sources.remote.UserRestController
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val userApi: UserRestController) {
    suspend fun getCurrentUser() = userApi.getCurrentUser()
}