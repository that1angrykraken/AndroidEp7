package seamonster.kraken.androidep7.ui.checkin

import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import androidx.viewbinding.ViewBinding
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.creative.ipfyandroid.Ipfy
import seamonster.kraken.androidep7.R
import seamonster.kraken.androidep7.core.BaseFragment
import seamonster.kraken.androidep7.databinding.FragmentCheckInBinding
import seamonster.kraken.androidep7.util.viewBinding

class CheckInFragment : BaseFragment(R.layout.fragment_check_in) {

    private val binding: FragmentCheckInBinding by viewBinding()

    private val viewModel: CheckInViewModel by fragmentViewModel()

    private val adapter = CheckInAdapter()

    override fun onStart() {
        super.onStart()
        initializeComponents()
    }

    override fun invalidate() {
        onHistoryStateChanged()
        onCheckInStateChanged()
    }

    private fun onCheckInStateChanged() = withState(viewModel) { state ->
        binding.buttonCheckIn.isEnabled = state.checkIn !is Loading
        when (state.checkIn) {
            is Success -> {
                viewModel.fetchHistory()
                showSnackbar(getString(R.string.check_in_successfully))
            }

            is Fail -> showSnackbar(state.checkIn.error)
            else -> {}
        }
    }

    private fun onHistoryStateChanged() = withState(viewModel) { state ->
        binding.swipeRefreshLayout.isRefreshing = state.history is Loading
        binding.textTotal.isVisible = state.history !is Loading
        when (state.history) {
            is Success -> {
                val list = state.history.invoke()
                binding.textTotal.text = getString(R.string.total_check_in, list.size)
                adapter.updateList(list.reversed())
            }

            is Fail -> showSnackbar(state.history.error)
            is Loading -> {}
            Uninitialized -> viewModel.fetchHistory()
        }
    }

    private fun initializeComponents() = binding.run {
        list.adapter = adapter

        list.addOnScrollListener(object : OnScrollListener() {

            override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(view, dx, dy)

                val layoutManager = view.layoutManager as LinearLayoutManager
                val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
                buttonTop.isVisible = firstVisiblePosition != 0
            }
        })

        buttonTop.setOnClickListener { list.smoothScrollToPosition(0) }

        swipeRefreshLayout.setOnRefreshListener { viewModel.fetchHistory() }

        Ipfy.getInstance().getPublicIpObserver().observe(requireActivity()) { ipData ->
            // every time there's a network change, set a new listener
            buttonCheckIn.setOnClickListener { viewModel.checkIn(ipData.currentIpAddress) }
        }
    }

    override fun getBinding(): ViewBinding = binding
}