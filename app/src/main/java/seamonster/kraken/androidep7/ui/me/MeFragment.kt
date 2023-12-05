package seamonster.kraken.androidep7.ui.me

import android.content.Intent
import android.os.Build
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.google.firebase.messaging.FirebaseMessaging
import seamonster.kraken.androidep7.R
import seamonster.kraken.androidep7.core.BaseFragment
import seamonster.kraken.androidep7.data.models.User
import seamonster.kraken.androidep7.databinding.FragmentMeBinding
import seamonster.kraken.androidep7.ui.login.AuthActivity
import seamonster.kraken.androidep7.util.viewBinding

class MeFragment : BaseFragment(R.layout.fragment_me) {

    companion object {
        private const val TAG = "MeFragment"
        private const val ARG_CURRENT_USER = "currentUser"
    }

    private val binding: FragmentMeBinding by viewBinding()

    private val viewModel: MeViewModel by fragmentViewModel()

    override fun onStart() {
        super.onStart()
        arguments?.run {
            binding.user = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getSerializable(ARG_CURRENT_USER, User::class.java)
            } else {
                @Suppress("DEPRECATION")
                getSerializable(ARG_CURRENT_USER) as User
            }
        }
        initializeComponents()
    }

    override fun invalidate() = withState(viewModel) { state ->
        if (state.logout !is Loading) hideLoadingOverlay()
        when (state.logout) {
            is Success -> {
                viewModel.clearToken()
                val intent = Intent(requireContext(), AuthActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }

            is Loading -> showLoadingOverlay(R.string.logging_out)
            is Fail -> showErrorDialog(R.string.error_title, R.string.unexpected_error)
            else -> {}
        }
    }

    private fun initializeComponents() {
        val navController = findNavController()
        binding.buttonEditInformation.setOnClickListener {
            val action = MeFragmentDirections.editInfoAction()
            navController.navigate(action)
        }

        binding.buttonPreferences.setOnClickListener {
            val action = MeFragmentDirections.settingsAction()
            navController.navigate(action)
        }

        binding.buttonLogout.setOnClickListener {
            createDialog(R.string.log_out, R.string.logout_notice) { d, _ ->
                d.dismiss()
                FirebaseMessaging.getInstance().deleteToken().addOnCompleteListener {
                    if (it.isSuccessful) viewModel.logout()
                    else it.exception?.let { e -> showSnackbar(e) }
                }
            }.show()
        }

        binding.buttonExit.setOnClickListener {
            createDialog(R.string.exit_application, R.string.exitting_notice) { d, _ ->
                d.dismiss()
                requireActivity().finishAfterTransition()
            }.setNeutralButton(R.string.log_out) { d, _ ->
                d.dismiss()
                binding.buttonLogout.callOnClick()
            }.show()
        }
    }

    override fun getBinding(): ViewBinding = binding
}