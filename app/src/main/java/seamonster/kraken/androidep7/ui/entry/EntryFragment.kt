package seamonster.kraken.androidep7.ui.entry

import android.content.Intent
import androidx.viewbinding.ViewBinding
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import seamonster.kraken.androidep7.R
import seamonster.kraken.androidep7.core.BaseFragment
import seamonster.kraken.androidep7.data.sources.UserPreferences
import seamonster.kraken.androidep7.databinding.FragmentEntryBinding
import seamonster.kraken.androidep7.ui.login.AuthActivity
import seamonster.kraken.androidep7.ui.main.MainActivity
import seamonster.kraken.androidep7.util.Errors
import seamonster.kraken.androidep7.util.viewBinding

class EntryFragment : BaseFragment(R.layout.fragment_entry) {

    private val binding: FragmentEntryBinding by viewBinding()

    private val viewModel: EntryViewModel by fragmentViewModel()

    override fun onStart() {
        super.onStart()

        viewModel.currentUser.observe(this) { user ->
            val joined = user.roles?.joinToString(";") { it.name ?: "" }
            UserPreferences(requireContext()).userRoles = joined
        }
    }

    override fun invalidate() = withState(viewModel) { state ->
        when (state.currentUser) {
            is Success -> navigateTo(Intent(requireActivity(), MainActivity::class.java))

            is Fail -> {
                val error = state.currentUser.error

                val message = error.message
                val con1 = message?.contains(Errors.UNAUTHORIZED)
                val con2 = message?.contains(Errors.INVALID_TOKEN)

                if (con1 == true || con2 == true) {
                    navigateTo(Intent(requireContext(), AuthActivity::class.java))
                } else {
                    showErrorDialog(error)
                }
            }

            is Loading -> {}
            else -> viewModel.fetchCurrentUser()
        }
    }

    private fun navigateTo(intent: Intent) {
        val intentFlags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.flags = intentFlags
        startActivity(intent)
    }

    override fun getBinding(): ViewBinding = binding
}