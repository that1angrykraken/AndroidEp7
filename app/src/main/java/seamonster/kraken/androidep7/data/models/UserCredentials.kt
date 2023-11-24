package seamonster.kraken.androidep7.data.models

import com.google.gson.annotations.SerializedName
import seamonster.kraken.androidep7.data.sources.api.TokenApi

data class UserCredentials @JvmOverloads constructor(

    @SerializedName("client_id")
    val clientId: String = TokenApi.CLIENT_ID,

    @SerializedName("client_secret")
    val clientSecret: String = TokenApi.CLIENT_SECRET,

    val username: String? = null,

    val password: String? = null,

    @SerializedName("refresh_token")
    val refreshToken: String? = null,

    @SerializedName("grant_type")
    val grantType: String,
)
