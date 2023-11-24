package seamonster.kraken.androidep7.ui.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import seamonster.kraken.androidep7.data.models.User
import seamonster.kraken.androidep7.databinding.ListItemUserBinding

class UserAdapter(private val listener: UserItemListener) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(
        private val binding: ListItemUserBinding,
        private val listener: UserItemListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            binding.user = user
            binding.root.apply {
                setOnClickListener { view -> listener.onItemClick(view, user) }
                setOnLongClickListener { view -> listener.onItemLongClick(view, user) }
            }
        }
    }

    private var dataSet = mutableListOf<User>()

    fun updateList(newData: List<User>?, append: Boolean = false) {
        val newDataSet = mutableListOf<User>().apply {
            if (append) addAll(dataSet)
            addAll(newData ?: emptyList())
        }
        val diffResult = DiffUtil.calculateDiff(UserItemCallback(dataSet, newDataSet))
        dataSet = newDataSet
        diffResult.dispatchUpdatesTo(this)
    }

    // Adapter implementation

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemUserBinding.inflate(inflater, parent, false)
        return UserViewHolder(binding, listener)
    }

    override fun getItemCount(): Int = dataSet.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = dataSet[position]
        if (position == dataSet.lastIndex) listener.onReachedLastItem()
        holder.bind(user)
    }

}