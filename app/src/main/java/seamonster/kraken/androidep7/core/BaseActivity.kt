package seamonster.kraken.androidep7.core

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
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
        initConnectionLostDialog()
        connectivityObserver = NetworkConnectivityObserver(applicationContext)
        observeConnectivity()
    }

    @CallSuper
    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    private fun initConnectionLostDialog() {
        dialog = MaterialAlertDialogBuilder(this)
            .setTitle(R.string.connection_lost)
            .setPositiveButton(R.string.exit_application) { dialog, _ ->
                dialog.dismiss()
                finishAfterTransition()
            }
            .setView(R.layout.dialog_lost_connection)
            .setCancelable(false)
            .create()
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
                    ConnectivityObserver.Status.Available -> dialog.dismiss()
                    ConnectivityObserver.Status.Losing -> {}
                    else -> dialog.show()
                }
            }
        }
    }
}