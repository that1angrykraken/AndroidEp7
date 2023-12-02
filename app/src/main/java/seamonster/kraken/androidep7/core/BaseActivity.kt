package seamonster.kraken.androidep7.core

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.progressindicator.CircularProgressIndicator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import seamonster.kraken.androidep7.R
import seamonster.kraken.androidep7.util.ConnectivityObserver
import seamonster.kraken.androidep7.util.NetworkConnectivityObserver

abstract class BaseActivity(@LayoutRes id: Int): AppCompatActivity(id) {

    private lateinit var connectivityObserver: NetworkConnectivityObserver
    private lateinit var job: Job

    private lateinit var dialog: AlertDialog

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectivityObserver = NetworkConnectivityObserver(applicationContext)
        observeConnectivity()
    }

    @CallSuper
    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    private fun showConnectionLostDialog() {
        dialog = MaterialAlertDialogBuilder(this)
            .setTitle(R.string.connection_lost)
            .setPositiveButton(R.string.exit_application) { dialog, _ ->
                dialog.dismiss()
                finishAfterTransition()
            }
            .setView(R.layout.dialog_lost_connection)
            .setCancelable(false)
            .show()
    }

    private fun observeConnectivity() {
        val status = connectivityObserver.observe().stateIn(
            lifecycleScope,
            SharingStarted.WhileSubscribed(),
            ConnectivityObserver.Status.Unavailable
        )
        job = lifecycleScope.launch {
            status.collect {
                when (it) {
                    ConnectivityObserver.Status.Available -> {
                        try { dialog.dismiss() } catch (_: Exception) {}
                    }
                    ConnectivityObserver.Status.Losing -> {}
                    else -> showConnectionLostDialog()
                }
            }
        }
    }
}