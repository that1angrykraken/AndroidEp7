package seamonster.kraken.androidep7.data.models

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    val error: String? = null,

    @SerializedName("error_description")
    val errorDescription: String? = null,
)