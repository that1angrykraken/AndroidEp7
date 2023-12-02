package seamonster.kraken.androidep7.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

interface ConnectivityObserver {

    fun observe(): Flow<Status>

    enum class Status {
        Unavailable, Lost, Losing, Available
    }
}

class NetworkConnectivityObserver(private val context: Context): ConnectivityObserver {

    private val cm = context.getSystemService(ConnectivityManager::class.java)

    override fun observe(): Flow<ConnectivityObserver.Status> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                launch { send(ConnectivityObserver.Status.Available) }
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                super.onLosing(network, maxMsToLive)
                launch { send(ConnectivityObserver.Status.Losing) }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                launch { send(ConnectivityObserver.Status.Lost) }
            }

            override fun onUnavailable() {
                super.onUnavailable()
                launch { send(ConnectivityObserver.Status.Unavailable) }
            }
        }

        cm.registerDefaultNetworkCallback(callback)
        awaitClose {
            cm.unregisterNetworkCallback(callback)
        }
    }.distinctUntilChanged()

}