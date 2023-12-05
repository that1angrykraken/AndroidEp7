package seamonster.kraken.androidep7.ui.checkin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import seamonster.kraken.androidep7.data.models.TimeSheet
import seamonster.kraken.androidep7.databinding.ListItemTimeSheetBinding

class CheckInAdapter: RecyclerView.Adapter<CheckInAdapter.ViewHolder>() {

    class ViewHolder(
        private val binding: ListItemTimeSheetBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(timeSheet: TimeSheet) {
            binding.sheet = timeSheet
        }
    }

    private var dataSet = mutableListOf<TimeSheet>()

    fun updateList(newData: List<TimeSheet>) {
        val diffResult = DiffUtil.calculateDiff(CheckInItemCallback(dataSet, newData))
        dataSet.clear()
        dataSet.addAll(newData)
//        notifyDataSetChanged()
        diffResult.dispatchUpdatesTo(this)
    }

    // Adapter implementation

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemTimeSheetBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = dataSet.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val timeSheet = dataSet[position]
        holder.bind(timeSheet)
    }

}