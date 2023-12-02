package seamonster.kraken.androidep7.ui.main

import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.airbnb.mvrx.*
import com.google.android.material.tabs.TabLayoutMediator
import seamonster.kraken.androidep7.R
import seamonster.kraken.androidep7.core.BaseFragment
import seamonster.kraken.androidep7.databinding.FragmentMainBinding
import seamonster.kraken.androidep7.util.viewBinding

class MainFragment : BaseFragment(R.layout.fragment_main) {

    private val binding: FragmentMainBinding by viewBinding()

    private val viewModel: MainViewModel by activityViewModel()

    override fun onStart() {
        super.onStart()
        initializeComponents()
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchCurrentUser()
    }

    override fun invalidate(): Unit = withState(viewModel) { state ->
        when (state.currentUser) {
            is Success -> binding.user = state.currentUser.invoke()
            is Loading -> {}
            is Fail -> showSnackbar(state.currentUser.error)
            Uninitialized -> {}
        }
    }

    private fun initializeComponents() {
        binding.viewPager.run {
            offscreenPageLimit = 3
            adapter = MainPagerAdapter(this@MainFragment)
        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.setIcon(R.drawable.round_view_timeline_24).contentDescription =
                    getString(R.string.tracking)

                1 -> tab.setIcon(R.drawable.round_today_24).contentDescription =
                    getString(R.string.check_in)

                2 -> tab.setIcon(R.drawable.round_notifications_24).contentDescription =
                    getString(R.string.notifications)

                3 -> tab.setIcon(R.drawable.round_people_outline_24).contentDescription =
                    getString(R.string.user_manager)

                else -> throw IllegalArgumentException("Invalid position: $position")
            }
        }.attach()

        binding.imgCurrentUser.setOnClickListener {
            val action = MainFragmentDirections.showOptionsAction(binding.user)
            findNavController().navigate(action)
        }
    }

    override fun getBinding(): ViewBinding = binding

    companion object {
        private const val TAG = "MainFragment"
    }
}
