package seamonster.kraken.androidep7.util

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.BigTextStyle
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import seamonster.kraken.androidep7.R
import seamonster.kraken.androidep7.data.repos.UserRepository
import seamonster.kraken.androidep7.data.sources.UserPreferences
import seamonster.kraken.androidep7.ui.entry.EntryActivity
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class MessagingService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "MessagingService"
    }

    @Inject
    lateinit var repository: UserRepository

    private var mJob: Job? = null

    override fun onNewToken(token: String) {
        Log.d(TAG, "New token: $token")
        UserPreferences(applicationContext).tokenDevice = token
        mJob = CoroutineScope(Dispatchers.IO).launch {
            try {
                repository.registerTokenDevice(token)
            } catch (e: Exception) {
                Log.e(TAG, "onNewToken: ", e)
            }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d(TAG, "Source: ${message.from}")

        if (message.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${message.data}")

            // enqueue a worker (WorkManager)
        }

        // check if message comes with a notification
        message.notification?.let {
            Log.d(TAG, "Notification body: ${it.title}-${it.body}")

            val title = it.title ?: getString(R.string.untitled)
            val body = it.body ?: getString(R.string.no_message)
            val channelId = it.channelId ?: getString(R.string.notification_channel_id)

            showNotification(title, body, channelId)
        }
    }

    private fun showNotification(title: String, message: String, channelId: String) {
        val intent = Intent(this, EntryActivity::class.java)
        val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, flags)

        val notification = NotificationCompat.Builder(this, channelId)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setContentText(message)
            .setContentTitle(title)
            .setSmallIcon(R.drawable.ic_stat_ic_notification)
            .setStyle(BigTextStyle().bigText(message))
            .build()

        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val id = Date().time.toInt() // use current date time as notification id
                notify(id, notification)
            }
        }
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
        Log.d(TAG, "onDeletedMessages: ${Calendar.getInstance().format()}")
    }

    override fun onDestroy() {
        mJob?.cancel()

        super.onDestroy()
    }
}