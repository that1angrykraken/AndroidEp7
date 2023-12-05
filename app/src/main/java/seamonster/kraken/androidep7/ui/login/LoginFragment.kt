package seamonster.kraken.androidep7.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.airbnb.mvrx.*
import seamonster.kraken.androidep7.R
import seamonster.kraken.androidep7.core.BaseFragment
import seamonster.kraken.androidep7.databinding.FragmentLoginBinding
import seamonster.kraken.androidep7.ui.entry.EntryActivity
import seamonster.kraken.androidep7.util.viewBinding

class LoginFragment : BaseFragment(R.layout.fragment_login) {

    private val binding: FragmentLoginBinding by viewBinding()

    private val viewModel: LoginViewModel by fragmentViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeComponents()
    }

    override fun invalidate(): Unit = withState(viewModel) { state ->
        state.login.let {
            binding.textLoginIssue.isVisible = it is Fail
            if (it !is Loading && it !is Uninitialized) hideLoadingOverlay()
        }
        when (state.login) {
            is Success -> {
                val token = state.login.invoke()
                Log.i(TAG, "invalidate: $token")
                viewModel.saveToken(token)
                val intent = Intent(requireContext(), EntryActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }

            is Loading -> showLoadingOverlay(R.string.logging_in)
            is Fail -> {

                showSnackbar(state.login.error)
            }
            Uninitialized -> {}
        }
    }

    private fun initializeComponents() {
        binding.buttonLogin.setOnClickListener {
            val username = binding.textUsername.text.toString().trim()
            val password = binding.textPassword.text.toString().trim()
            viewModel.login(username, password)
        }
        val navController = findNavController()
        binding.buttonSignUp.setOnClickListener {
            val action = LoginFragmentDirections.signupAction()
            navController.navigate(action)
        }
        binding.buttonResetPassword.setOnClickListener {
            val action = LoginFragmentDirections.resetPasswordAction()
            navController.navigate(action)
        }
    }

    override fun getBinding(): ViewBinding = binding

    companion object {
        private const val TAG = "LoginFragment"
    }
}