package seamonster.kraken.androidep7.ui.notifications

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import seamonster.kraken.androidep7.data.models.Notification
import seamonster.kraken.androidep7.databinding.ListItemNotificationBinding

class NotificationAdapter(
    private val listener: NotificationItemListener
): RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    class ViewHolder(
        private val binding: ListItemNotificationBinding,
        private val listener: NotificationItemListener
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(notification: Notification) {
            binding.root.run {
                binding.notification = notification
                setOnClickListener { listener.onItemClick(it, notification) }
                setOnLongClickListener { listener.onItemLongClick(it, notification) }
            }
        }
    }

    var dataSet = mutableListOf<Notification>()

    fun updateList(newData: List<Notification>?) {
        val newDataSet = newData ?: emptyList()
        val diffResult = DiffUtil.calculateDiff(NotificationItemCallback(dataSet, newDataSet))
        dataSet.clear()
        dataSet.addAll(newDataSet)
        diffResult.dispatchUpdatesTo(this)
    }

    // Adapter implementation

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemNotificationBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, listener)
    }

    override fun getItemCount(): Int = dataSet.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notification = dataSet[position]
        holder.bind(notification)
    }
}