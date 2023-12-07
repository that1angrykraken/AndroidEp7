package seamonster.kraken.androidep7.ui.main

import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.airbnb.mvrx.*
import com.google.android.material.tabs.TabLayoutMediator
import seamonster.kraken.androidep7.R
import seamonster.kraken.androidep7.core.BaseFragment
import seamonster.kraken.androidep7.data.models.User
import seamonster.kraken.androidep7.data.sources.UserPreferences
import seamonster.kraken.androidep7.databinding.FragmentMainBinding
import seamonster.kraken.androidep7.util.viewBinding

class MainFragment : BaseFragment(R.layout.fragment_main) {

    private val binding: FragmentMainBinding by viewBinding()

    private val viewModel: MainViewModel by activityViewModel()

    override fun onStart() {
        super.onStart()
        initializeComponents()
    }

    override fun invalidate(): Unit = withState(viewModel) { state ->
        if (state.currentUser is Fail) showSnackbar(state.currentUser.error)
    }

    private fun initializeComponents() {

        viewModel.currentUser.observe(this) { user ->
            binding.user = user
            registerDevice(user)
        }

        binding.viewPager.run {
            offscreenPageLimit = 3
            adapter = MainPagerAdapter(this@MainFragment)
        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.setIcon(R.drawable.round_view_timeline_24).contentDescription =
                    getString(R.string.tracking_timeline)

                1 -> tab.setIcon(R.drawable.round_today_24).contentDescription =
                    getString(R.string.check_in_history)

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

    private fun registerDevice(user: User) {
        val tokenDevice = UserPreferences(requireContext()).tokenDevice
        val con1 = user.tokenDevice.isNullOrEmpty()
        val con2 = user.tokenDevice != tokenDevice
        val shouldRegister = con1 || con2
        if (shouldRegister) viewModel.registerTokenDevice(tokenDevice!!)
    }

    override fun getBinding(): ViewBinding = binding
}
