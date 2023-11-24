package seamonster.kraken.androidep7.ui.notifications

import androidx.recyclerview.widget.DiffUtil
import seamonster.kraken.androidep7.data.models.Notification

class NotificationItemCallback<T: List<Notification>>(
    private val oldData: T,
    private val newData: T
): DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldData.size

    override fun getNewListSize(): Int = newData.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldData[oldItemPosition].id == newData[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldData[oldItemPosition] == newData[newItemPosition]
    }
}