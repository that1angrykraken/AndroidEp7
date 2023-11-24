package seamonster.kraken.androidep7.ui.entry

import android.content.Intent
import android.util.Log
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
import seamonster.kraken.androidep7.util.viewBinding

class EntryFragment : BaseFragment(R.layout.fragment_entry) {

    private val binding: FragmentEntryBinding by viewBinding()

    private val viewModel: EntryViewModel by fragmentViewModel()

    override fun invalidate() = withState(viewModel) { state ->
        when (state.currentUser) {
            is Success -> {
                val user = state.currentUser.invoke()
                val allRoles = user?.roles?.joinToString(";") { it.name ?: "" }
                UserPreferences(requireContext()).userRoles = allRoles
                val intent = Intent(requireActivity(), MainActivity::class.java)
                startActivity(intent)
            }

            is Loading -> {}
            is Fail -> {
                val message = state.currentUser.error.message ?: getString(R.string.unexpected_error)

                val con1 = message.contains("unauthorized")
                val con2 = message.contains("invalid_token")

                if (con1 || con2) {
                    startActivity(Intent(requireActivity(), AuthActivity::class.java))
                } else showError(message)
            }

            else -> viewModel.fetchCurrentUser()
        }
    }

    private fun showError(message: String) {
        val title = getString(R.string.error_title)
        showErrorDialog(title, message) {
            requireActivity().finishAfterTransition()
        }
    }

    override fun getBinding(): ViewBinding = binding

    companion object {
        private const val TAG = "EntryFragment"
    }
}