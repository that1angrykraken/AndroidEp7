package seamonster.kraken.androidep7.ui.tracking

import android.animation.LayoutTransition
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import seamonster.kraken.androidep7.data.models.Tracking
import seamonster.kraken.androidep7.databinding.ListItemTrackingBinding

class TrackingAdapter(
    private val listener: TrackingItemListener
) : RecyclerView.Adapter<TrackingAdapter.ViewHolder>() {

    class ViewHolder(
        private val binding: ListItemTrackingBinding,
        private val listener: TrackingItemListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(tracking: Tracking) = binding.run {
            binding.clItemContent.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
            binding.tracking = tracking.copy()
            buttonSaveChanges.setOnClickListener {
                checkboxEditMode.isChecked = false
                textInputLayout.editText?.clearFocus()
                listener.onItemSaveButtonClick(it, binding.tracking!!)
            }
            buttonDelete.setOnClickListener {
                listener.onItemDeleteButtonClick(it, binding.tracking!!)
            }
        }

    }

    var dataSet = mutableListOf<Tracking>()

    fun updateList(newData: List<Tracking>?) {
        val newDataSet = newData ?: emptyList()
        val diffResult = DiffUtil.calculateDiff(TrackingItemCallback(dataSet, newDataSet))
        dataSet.clear()
        dataSet.addAll(newDataSet)
        diffResult.dispatchUpdatesTo(this)
    }

    // Adapter implementation

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemTrackingBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, listener)
    }

    override fun getItemCount(): Int = dataSet.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tracking = dataSet[position]
        holder.bind(tracking)
    }
}