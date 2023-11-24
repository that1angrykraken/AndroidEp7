package seamonster.kraken.androidep7.ui.user

import android.view.View
import seamonster.kraken.androidep7.data.models.User

interface UserItemListener {

    fun onItemClick(view: View, user: User)

    fun onItemLongClick(view: View, user: User): Boolean

    fun onReachedLastItem()
}