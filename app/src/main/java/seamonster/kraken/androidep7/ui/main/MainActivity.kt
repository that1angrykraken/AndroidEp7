package seamonster.kraken.androidep7.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import seamonster.kraken.androidep7.R
import seamonster.kraken.androidep7.core.BaseActivity
import seamonster.kraken.androidep7.data.sources.UserPreferences
import seamonster.kraken.androidep7.ui.FullscreenDialog

class MainActivity : BaseActivity(R.layout.activity_main) {

    private lateinit var userPreferences: UserPreferences

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            checkPermission()
        }

    override fun onStart() {
        super.onStart()
        userPreferences = UserPreferences(this)
        checkPermission()
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return
        if (!userPreferences.shouldRequestPermission) return
        val permission = Manifest.permission.POST_NOTIFICATIONS
        when {
            checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED ->
                with(NotificationManagerCompat.from(this)) {
                    if (getNotificationChannel(getString(R.string.notification_channel_id)) != null) return@with
                    val channel = NotificationChannelCompat
                        .Builder(getString(R.string.notification_channel_id), NotificationManagerCompat.IMPORTANCE_HIGH)
                        .setName(getString(R.string.main_channel_name))
                        .setVibrationEnabled(true)
                        .build()
                    createNotificationChannel(channel)
                }

            shouldShowRequestPermissionRationale(permission) -> {
                FullscreenDialog().Builder()
                    .setIcon(R.drawable.round_notifications_24)
                    .setTitle(R.string.turn_on_notifications)
                    .setMessage(R.string.turn_on_notification_message)
                    .setNegativeButton(R.string.dialog_negative_button) {}
                    .setPositiveButton(R.string.dialog_positive_button) {
                        requestPermissionLauncher.launch(permission)
                    }
                    .setNeutralButton(R.string.never_ask_again) {
                        userPreferences.shouldRequestPermission = false
                        MaterialAlertDialogBuilder(this)
                            .setTitle(R.string.permission_denied)
                            .setMessage(R.string.permission_denied_message)
                            .setPositiveButton(R.string.dialog_positive_button) { _, _ -> }
                            .show()
                    }
                    .build().show(supportFragmentManager, FullscreenDialog.TAG)
            }

            else -> {
                requestPermissionLauncher.launch(permission)
            }
        }
    }


}