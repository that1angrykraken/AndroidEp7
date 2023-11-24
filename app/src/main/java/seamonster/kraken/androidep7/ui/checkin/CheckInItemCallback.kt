package seamonster.kraken.androidep7.ui.checkin

import androidx.recyclerview.widget.DiffUtil
import seamonster.kraken.androidep7.data.models.TimeSheet

class CheckInItemCallback(
    private val oldData: List<TimeSheet>,
    private val newData: List<TimeSheet>
): DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldData.size

    override fun getNewListSize(): Int =newData.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldData[oldItemPosition].id == newData[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldData[oldItemPosition] == newData[newItemPosition]
    }
}