package seamonster.kraken.androidep7.util

import android.util.Log

object ApiExtension {

    private const val TAG = "ApiExtension"

    fun getResponseCode(t: Throwable): Int? {
        return try {
            val m = t.message?.split(" ")
            m?.get(1)?.toInt()
        } catch (e: Exception) {
            Log.i(
                TAG, "getResponseCode: Response code not found! Cause: ${e.cause?.message}" +
                        "\nThis seems to be a network problem, reconnect then try again."
            )
            null
        }
    }
}