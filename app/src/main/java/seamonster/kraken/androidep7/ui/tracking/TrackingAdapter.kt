package seamonster.kraken.androidep7.ui.tracking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import seamonster.kraken.androidep7.data.models.Tracking
import seamonster.kraken.androidep7.databinding.ListItemTrackingBinding
import seamonster.kraken.androidep7.util.BindingUtil

class TrackingAdapter(
    private val listener: TrackingItemListener
) : RecyclerView.Adapter<TrackingAdapter.ViewHolder>() {

    inner class ViewHolder(
        private val binding: ListItemTrackingBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(tracking: Tracking, position: Int) = binding.run {

            textContent.setText(tracking.content)
            textTrackingDate.text = BindingUtil.calendarToString(tracking.date)

            cardView.isChecked = tracking.content.isNullOrEmpty()
            (cardView.isChecked && position == 0).let {
                checkboxEditMode.isChecked = it
            }

            checkboxEditMode.isChecked.let {
                buttonSaveChanges.isVisible = it
                buttonDelete.isVisible = it
                textContent.minLines = if (it) 9 else 3
            }

            buttonSaveChanges.setOnClickListener {
                checkboxEditMode.isChecked = false
                textInputLayout.editText?.clearFocus()
                val newContent = textContent.text.toString()
                listener.onItemSaveButtonClick(it, tracking.copy(content = newContent))
            }

            buttonDelete.setOnClickListener {
                listener.onItemDeleteButtonClick(it, tracking)
            }

            checkboxEditMode.setOnClickListener {
                if (!checkboxEditMode.isChecked) textContent.setText(tracking.content)
            }
        }
    }

    private val dataSet = mutableListOf<Tracking>()

    fun updateList(newData: List<Tracking>) {
        val diffResult = DiffUtil.calculateDiff(TrackingItemCallback(dataSet, newData))
        dataSet.clear()
        dataSet.addAll(newData)
//        notifyDataSetChanged()
        diffResult.dispatchUpdatesTo(this)
    }

    // Adapter implementation

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemTrackingBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = dataSet.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tracking = dataSet[position]
        holder.bind(tracking, position)
    }
}