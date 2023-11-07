package seamonster.kraken.androidep7.data.models

import com.google.gson.annotations.SerializedName
import seamonster.kraken.androidep7.data.sources.remote.TokenController

data class UserCredentials @JvmOverloads constructor(

    @SerializedName("client_id")
    val clientId: String = TokenController.CLIENT_ID,

    @SerializedName("client_secret")
    val clientSecret: String = TokenController.CLIENT_SECRET,

    @SerializedName("username")
    val username: String? = null,

    @SerializedName("password")
    val password: String? = null,

    @SerializedName("refresh_token")
    val refreshToken: String? = null,

    @SerializedName("grant_type")
    val grantType: String,
)
