package seamonster.kraken.androidep7.ui.notifications

import android.view.View
import seamonster.kraken.androidep7.data.models.Notification

interface NotificationItemListener {

    fun onItemClick(view: View, notification: Notification)

    fun onItemLongClick(view: View, notification: Notification): Boolean
}