package seamonster.kraken.androidep7.ui.tracking

import android.view.View
import seamonster.kraken.androidep7.data.models.Notification
import seamonster.kraken.androidep7.data.models.Tracking

interface TrackingItemListener {

    fun onItemSaveButtonClick(view: View, tracking: Tracking)

    fun onItemDeleteButtonClick(view: View, tracking: Tracking)
}