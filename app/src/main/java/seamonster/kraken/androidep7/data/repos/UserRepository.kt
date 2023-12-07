package seamonster.kraken.androidep7.data.repos

import androidx.lifecycle.MutableLiveData
import seamonster.kraken.androidep7.data.models.Page
import seamonster.kraken.androidep7.data.models.Search
import seamonster.kraken.androidep7.data.models.User
import seamonster.kraken.androidep7.data.sources.api.UserApi
import seamonster.kraken.androidep7.util.toMessage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userApi: UserApi,
) {

    val currentUserLiveData = MutableLiveData<User>()

    val pageLiveData = MutableLiveData<Page<User>>()

    suspend fun blockUser(id: Int) = userApi.blockUser(id)

    suspend fun fetchCurrentUser() {
        val response = userApi.getCurrentUser()
        if (response.isSuccessful) {
            currentUserLiveData.postValue(response.body())
        } else {
            val message = response.errorBody().toMessage()
            throw Throwable(message)
        }
    }

    suspend fun fetchSearchResult(keyword: String, pageIndex: Int) {
        val search = Search(keyword, 20, pageIndex, pageIndex)
        val response = userApi.searchByPage(search)
        if (response.isSuccessful) {
            pageLiveData.postValue(response.body())
        } else {
            val message = response.errorBody().toMessage()
            throw Throwable(message)
        }
    }

    suspend fun fetchUser(id: Int) = userApi.getUserById(id)

    suspend fun registerTokenDevice(tokenDevice: String) {
        val response = userApi.edit(tokenDevice)
        if (response.isSuccessful) {
            currentUserLiveData.postValue(response.body())
        } else {
            val message = response.errorBody().toMessage()
            throw Throwable(message)
        }
    }

    suspend fun updateMyself(user: User) {
        val response = userApi.updateMyself(user)
        if (response.isSuccessful) {
            currentUserLiveData.postValue(response.body())
        } else {
            val message = response.errorBody().toMessage()
            throw Throwable(message)
        }
    }

    suspend fun updateUser(user: User) = userApi.edit(user, user.id!!)
}