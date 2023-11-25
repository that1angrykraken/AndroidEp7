package seamonster.kraken.androidep7.ui.user

import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy.*
import androidx.viewbinding.ViewBinding
import com.airbnb.mvrx.*
import seamonster.kraken.androidep7.R
import seamonster.kraken.androidep7.core.BaseFragment
import seamonster.kraken.androidep7.data.models.Page
import seamonster.kraken.androidep7.data.models.User
import seamonster.kraken.androidep7.databinding.FragmentUsersBinding
import seamonster.kraken.androidep7.ui.main.MainFragmentDirections
import seamonster.kraken.androidep7.util.viewBinding

class UserFragment : BaseFragment(R.layout.fragment_users), UserItemListener {

    private val binding: FragmentUsersBinding by viewBinding()

    private val viewModel: UserViewModel by activityViewModel()

    private val adapter = UserAdapter(this).apply {
        stateRestorationPolicy = PREVENT_WHEN_EMPTY
    }

    override fun onStart() {
        super.onStart()
        initializeComponents()
    }

    override fun invalidate() = withState(viewModel) { state ->
        binding.swipeRefreshLayout.isRefreshing = state.searchResult is Loading
        when (state.searchResult) {
            Uninitialized -> viewModel.fetchAllUsers()
            is Success -> onSuccess(state.searchResult.invoke())
            is Loading -> {}
            is Fail -> onFailed(state.searchResult.error)
        }
    }

    private fun onSuccess(page: Page<User>) {
        Log.i(TAG, "onSuccess: ok")
        adapter.updateList(page.content, !page.first)
    }

    private fun onFailed(t: Throwable) {
        val message = t.message ?: getString(R.string.unexpected_error)
        showSnackbar(message)
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

        buttonTop.setOnClickListener { list.smoothScrollToPosition(0) }

        swipeRefreshLayout.setOnRefreshListener { viewModel.fetchAllUsers() }
    }

    override fun onItemClick(view: View, user: User) {
        val action = MainFragmentDirections.seeUserDetailAction(user.id!!)
        findNavController().navigate(action)
    }

    override fun onItemLongClick(view: View, user: User): Boolean {
        return true
    }

    override fun onReachedLastItem() {
        // load more result
        viewModel.fetchAllUsers(nextPage = true)
    }

    override fun getBinding(): ViewBinding = binding

    companion object {
        private const val TAG = "UserFragment"
    }

}