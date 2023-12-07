package seamonster.kraken.androidep7.ui.user

import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
import androidx.viewbinding.ViewBinding
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import seamonster.kraken.androidep7.R
import seamonster.kraken.androidep7.core.BaseFragment
import seamonster.kraken.androidep7.data.models.User
import seamonster.kraken.androidep7.databinding.FragmentUsersBinding
import seamonster.kraken.androidep7.ui.main.MainFragmentDirections
import seamonster.kraken.androidep7.util.viewBinding

class UserFragment : BaseFragment(R.layout.fragment_users), UserItemListener {

    private val binding: FragmentUsersBinding by viewBinding()

    private val viewModel: UserViewModel by activityViewModel()

    override fun onStart() {
        super.onStart()
        initComponents()
    }

    override fun invalidate() = withState(viewModel) { state ->
        binding.swipeRefreshLayout.isRefreshing = state.searchResult is Loading
        state.searchResult.let {
            if (it is Uninitialized) viewModel.fetchAllUsers()
            if (it is Fail) showSnackbar(it.error)
        }
    }

    private fun initComponents() = binding.run {
        viewModel.userPage.observe(this@UserFragment) { page ->
            val adapter = binding.list.adapter as UserAdapter
            adapter.updateList(page.content, !page.first)
        }

        list.adapter = UserAdapter(this@UserFragment).apply {
            stateRestorationPolicy = PREVENT_WHEN_EMPTY
        }

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
        viewModel.fetchUser(user.id!!)
        val action = MainFragmentDirections.seeUserDetailAction()
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
}