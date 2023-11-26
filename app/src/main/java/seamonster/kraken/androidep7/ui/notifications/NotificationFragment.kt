package seamonster.kraken.androidep7.ui.notifications

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
import androidx.viewbinding.ViewBinding
import com.airbnb.mvrx.*
import seamonster.kraken.androidep7.R
import seamonster.kraken.androidep7.core.BaseFragment
import seamonster.kraken.androidep7.data.models.Notification
import seamonster.kraken.androidep7.databinding.FragmentNotificationBinding
import seamonster.kraken.androidep7.util.viewBinding

class NotificationFragment : BaseFragment(R.layout.fragment_notification),
    NotificationItemListener {

    private val binding: FragmentNotificationBinding by viewBinding()

    private val viewModel: NotificationViewModel by fragmentViewModel()

    private val adapter = NotificationAdapter(this).apply {
        stateRestorationPolicy = PREVENT_WHEN_EMPTY
    }

    override fun onStart() {
        super.onStart()
        initializeComponents()
    }

    override fun invalidate() = withState(viewModel) { state ->
        binding.swipeRefreshLayout.isRefreshing = state.notifications is Loading
        when (state.notifications) {
            is Success -> onSuccess(state.notifications.invoke())
            is Loading -> {}
            is Fail -> onFailed(state.notifications.error)
            Uninitialized -> viewModel.fetchAll()
        }
    }

    private fun onSuccess(dataSet: List<Notification>) {
        binding.textEmptyList.isVisible = dataSet.isEmpty()
        adapter.updateList(dataSet.reversed())
    }

    private fun onFailed(t: Throwable) {
        binding.textEmptyList.isVisible = adapter.dataSet.isEmpty()
        val message = t.message ?: getString(R.string.unexpected_error)
        showSnackbar(message)
        binding.textEmptyList.isVisible = adapter.dataSet.isEmpty()
    }

    private fun initializeComponents() = binding.run {
        list.adapter = adapter

        val layoutManager = list.layoutManager as LinearLayoutManager

        list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(view, dx, dy)

                val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
                buttonTop.isVisible = firstVisiblePosition != 0
            }
        })

        buttonTop.setOnClickListener {
            if (layoutManager.findFirstVisibleItemPosition() >= 20) {
                // There will be some cases where your list has hundreds of items, this condition
                // block will immediately move to a position close to the top of the page and then
                // perform a smooth scroll to the top of the page, this helps you go to the top of
                // the page instantly with a smooth scrolling effect without having to wait for long.
                list.scrollToPosition(10)
            }
            list.smoothScrollToPosition(0)
        }
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchAll()
        }
    }

    override fun onItemClick(view: View, notification: Notification) {

    }

    override fun onItemLongClick(view: View, notification: Notification): Boolean {

        return true
    }

    override fun getBinding(): ViewBinding = binding
}