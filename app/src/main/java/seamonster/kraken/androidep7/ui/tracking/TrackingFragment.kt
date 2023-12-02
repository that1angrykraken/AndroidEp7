package seamonster.kraken.androidep7.ui.tracking

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
import androidx.viewbinding.ViewBinding
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import seamonster.kraken.androidep7.R
import seamonster.kraken.androidep7.core.BaseFragment
import seamonster.kraken.androidep7.data.models.Tracking
import seamonster.kraken.androidep7.data.models.clone
import seamonster.kraken.androidep7.databinding.FragmentTrackingBinding
import seamonster.kraken.androidep7.ui.main.MainViewModel
import seamonster.kraken.androidep7.util.viewBinding

class TrackingFragment : BaseFragment(R.layout.fragment_tracking), TrackingItemListener {

    private val binding: FragmentTrackingBinding by viewBinding()

    private val viewModel: TrackingViewModel by fragmentViewModel()

    private val mainViewModel: MainViewModel by activityViewModel()

    private val adapter = TrackingAdapter(this).apply {
        stateRestorationPolicy = PREVENT_WHEN_EMPTY
    }

    override fun onStart() {
        super.onStart()
        initializeComponents()
    }

    override fun invalidate() = withState(viewModel) { state ->
        when (state.trackingAction) {
            is Success -> viewModel.fetchAll()
            is Fail -> showSnackbar(state.trackingAction.error)
            else -> {}
        }

        binding.swipeRefreshLayout.isRefreshing = state.timeline is Loading
        when (state.timeline) {
            is Success -> {
                val list = state.timeline.invoke()
                binding.textTotal.text = getString(R.string.total_tracking, list.size)
                adapter.updateList(list.reversed())
                binding.list.scrollToPosition(0)
            }

            is Fail -> showSnackbar(state.timeline.error)
            is Loading -> {}
            Uninitialized -> viewModel.fetchAll()
        }

        val con1 = state.timeline !is Loading
        val con2 = state.trackingAction !is Loading
        binding.buttonNew.isEnabled = con1 && con2
    }

    private fun initializeComponents() = binding.run {
        list.adapter = adapter
        list.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(view, dx, dy)

                val layoutManager = view.layoutManager as LinearLayoutManager
                val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
                buttonTop.isVisible = firstVisiblePosition != 0
            }
        })

        buttonTop.setOnClickListener {
            list.smoothScrollToPosition(0)
        }

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchAll()
        }

        buttonNew.setOnClickListener {
            withState(mainViewModel) { mainState ->
                if (mainState.currentUser !is Success) return@withState
                viewModel.createNewTracking(mainState.currentUser.invoke())
            }
        }
    }

    override fun onItemSaveButtonClick(view: View, tracking: Tracking) {
        viewModel.updateTracking(tracking)
        hideSoftKeyboard(view)
    }

    override fun onItemDeleteButtonClick(view: View, tracking: Tracking) {
        createDialog(
            R.string.dialog_title_delete,
            R.string.dialog_message_delete
        ) { _, _ ->
            tracking.id?.let { viewModel.deleteTracking(it) }
            hideSoftKeyboard(view)
        }.show()
    }

    override fun getBinding(): ViewBinding = binding
}