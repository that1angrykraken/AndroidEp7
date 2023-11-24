package seamonster.kraken.androidep7.ui.user

import androidx.recyclerview.widget.DiffUtil
import seamonster.kraken.androidep7.data.models.User


class UserItemCallback( // DiffUtil callback
    private val oldData: List<User>,
    private val newData: List<User>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldData.size

    override fun getNewListSize(): Int = newData.size

    override fun areItemsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        return oldData[oldPosition].id == newData[newPosition].id
    }

    override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        return oldData[oldPosition] == newData[newPosition]
    }

}