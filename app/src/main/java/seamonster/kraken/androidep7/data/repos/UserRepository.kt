package seamonster.kraken.androidep7.data.repos

import retrofit2.Response
import seamonster.kraken.androidep7.data.models.Page
import seamonster.kraken.androidep7.data.models.Search
import seamonster.kraken.androidep7.data.models.Tracking
import seamonster.kraken.androidep7.data.models.User
import seamonster.kraken.androidep7.data.sources.api.NotificationApi
import seamonster.kraken.androidep7.data.sources.api.TimeSheetApi
import seamonster.kraken.androidep7.data.sources.api.TrackingApi
import seamonster.kraken.androidep7.data.sources.api.UserApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val userApi: UserApi) {

    suspend fun blockUser(id: Int) = userApi.blockUser(id)

    suspend fun fetchCurrentUser() = userApi.getCurrentUser()

    suspend fun fetchSearchResult(keyword: String, pageIndex: Int): Response<Page<User>> {
        val search = Search(keyword, 20, pageIndex, pageIndex)
        return userApi.searchByPage(search)
    }

    suspend fun fetchUser(id: Int) = userApi.getUserById(id)

    suspend fun updateMyself(user: User) = userApi.updateMyself(user)

    suspend fun updateUser(user: User) = userApi.edit(user, user.id!!)
}