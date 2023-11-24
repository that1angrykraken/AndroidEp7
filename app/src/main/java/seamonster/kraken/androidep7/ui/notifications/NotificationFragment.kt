package seamonster.kraken.androidep7.ui.notifications

import android.view.View
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import seamonster.kraken.androidep7.R
import seamonster.kraken.androidep7.core.BaseFragment
import seamonster.kraken.androidep7.data.models.Notification
import seamonster.kraken.androidep7.databinding.FragmentNotificationBinding
import seamonster.kraken.androidep7.util.viewBinding

class NotificationFragment : BaseFragment(R.layout.fragment_notification), NotificationItemListener {

    private val binding: FragmentNotificationBinding by viewBinding()

    private val viewModel: NotificationViewModel by fragmentViewModel()

    private val adapter = NotificationAdapter(this)

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

    private fun initializeComponents() {
        binding.list.adapter = adapter

        binding.swipeRefreshLayout.setOnRefreshListener {
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