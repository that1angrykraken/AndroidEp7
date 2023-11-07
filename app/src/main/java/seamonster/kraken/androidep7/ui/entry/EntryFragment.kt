package seamonster.kraken.androidep7.ui.entry

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import seamonster.kraken.androidep7.R
import seamonster.kraken.androidep7.databinding.FragmentEntryBinding
import seamonster.kraken.androidep7.ui.auth.AuthActivity
import seamonster.kraken.androidep7.ui.main.MainActivity
import seamonster.kraken.androidep7.utils.viewBinding

class EntryFragment : Fragment(R.layout.fragment_entry), MavericksView {

    private val binding: FragmentEntryBinding by viewBinding()

    private val viewModel: EntryViewModel by fragmentViewModel()

    override fun invalidate() = withState(viewModel) { state ->
        handleStateChange(state)
    }

    private fun handleStateChange(state: EntryState) {
        binding.indicatorLoading.isVisible = state.currentUser is Loading
        when (state.currentUser) {
            is Success -> {
                startActivity(Intent(requireActivity(), MainActivity::class.java))
                binding.textStatus.text = getString(R.string.welcome)
            }

            is Loading -> {
                binding.textStatus.text = getString(R.string.fetching_current_user)
            }

            is Uninitialized -> {
                viewModel.handle(EntryAction.GetCurrentUser)
            }

            is Fail -> {
                val message = state.currentUser.error.message
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                startActivity(Intent(requireActivity(), AuthActivity::class.java))
            }
        }
    }
}