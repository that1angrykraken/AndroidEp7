package seamonster.kraken.androidep7.ui.tracking

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import seamonster.kraken.androidep7.R
import seamonster.kraken.androidep7.core.BaseFragment
import seamonster.kraken.androidep7.data.models.Tracking
import seamonster.kraken.androidep7.databinding.FragmentTrackingBinding
import seamonster.kraken.androidep7.util.viewBinding

class TrackingFragment : BaseFragment(R.layout.fragment_tracking), TrackingItemListener {

    private val binding: FragmentTrackingBinding by viewBinding()

    private val viewModel: TrackingViewModel by fragmentViewModel()

    private val adapter = TrackingAdapter(this)

    override fun onStart() {
        super.onStart()
        initializeComponents()
    }

    override fun invalidate() {
        onTimelineStateChanged()
        onTrackingActionStateChanged()
    }

    private fun onTrackingActionStateChanged() = withState(viewModel) { state ->
        if (!state.shouldCheckAction) return@withState
        when (state.trackingAction) {
            is Success -> {
                viewModel.fetchAll()
                showSnackbar(R.string.completed)
            }
            is Fail -> showSnackbar(state.trackingAction.error)
            else -> {}
        }
    }

    private fun onTimelineStateChanged() = withState(viewModel) { state ->
        binding.swipeRefreshLayout.isRefreshing = state.timeline is Loading
        when (state.timeline) {
            is Success -> {
                val list = state.timeline.invoke()
                binding.textTotal.text = getString(R.string.total_tracking, list.size)
                adapter.updateList(list.reversed())
            }
            is Fail -> showSnackbar(state.timeline.error)
            is Loading -> {}
            Uninitialized -> viewModel.fetchAll()
        }
    }

    private fun initializeComponents() = binding.run {
        list.adapter = adapter

        list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(view: RecyclerView, state: Int) {
                super.onScrollStateChanged(view, state)
                buttonTop.isVisible = view.canScrollVertically(0)
            }
        })

        buttonTop.setOnClickListener {
            list.smoothScrollToPosition(0)
        }

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchAll()
        }
    }

    override fun onItemSaveButtonClick(view: View, tracking: Tracking) {
        viewModel.updateTracking(tracking)
    }

    override fun onItemDeleteButtonClick(view: View, tracking: Tracking) {
        createDialog(
            R.string.dialog_title_delete,
            R.string.dialog_message_delete
        ) { _, _ ->
            viewModel.deleteTracking(tracking.id!!)
        }.show()
    }

    override fun getBinding(): ViewBinding = binding
}