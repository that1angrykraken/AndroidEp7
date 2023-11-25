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
import seamonster.kraken.androidep7.util.Errors
import seamonster.kraken.androidep7.util.viewBinding

class EntryFragment : BaseFragment(R.layout.fragment_entry) {

    private val binding: FragmentEntryBinding by viewBinding()

    private val viewModel: EntryViewModel by fragmentViewModel()

    private val intentFlags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

    override fun invalidate() = withState(viewModel) { state ->
        when (state.currentUser) {
            is Success -> {
                val user = state.currentUser.invoke()
                val allRoles = user?.roles?.joinToString(";") { it.name ?: "" }
                UserPreferences(requireContext()).userRoles = allRoles
                val intent = Intent(requireActivity(), MainActivity::class.java).apply {
                    flags = intentFlags
                }
                startActivity(intent)
            }

            is Loading -> {}
            is Fail -> {
                val error = state.currentUser.error

                val message = error.message
                val con1 = message?.contains(Errors.UNAUTHORIZED) ?: false
                val con2 = message?.contains(Errors.INVALID_TOKEN) ?: false

                if (con1 || con2) {
                    val intent = Intent(requireContext(), AuthActivity::class.java).apply {
                        flags = intentFlags
                    }
                    startActivity(intent)
                } else showErrorDialog(error)
            }

            else -> viewModel.fetchCurrentUser()
        }
    }

    override fun getBinding(): ViewBinding = binding

    companion object {
        private const val TAG = "EntryFragment"
    }
}