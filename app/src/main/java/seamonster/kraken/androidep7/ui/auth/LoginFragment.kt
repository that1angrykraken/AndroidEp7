package seamonster.kraken.androidep7.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import seamonster.kraken.androidep7.R
import seamonster.kraken.androidep7.core.BaseFragment
import seamonster.kraken.androidep7.databinding.FragmentLoginBinding
import seamonster.kraken.androidep7.ui.main.MainActivity
import seamonster.kraken.androidep7.utils.viewBinding

sealed class LoginAction {
    data class Submit(val username: String, val password: String) : LoginAction()
    data object ResetPassword : LoginAction()
    data object SignUp : LoginAction()
}

class LoginFragment : BaseFragment(R.layout.fragment_login) {

    private val binding: FragmentLoginBinding by viewBinding()

    private val viewModel: AuthViewModel by activityViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonLogin.setOnClickListener {
            val username = binding.textUsername.text.toString()
            val password = binding.textPassword.text.toString()
            viewModel.handle(LoginAction.Submit(username, password))
        }
        val navController = findNavController()
        binding.buttonSignUp.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_signUpFragment)
        }
        binding.buttonResetPassword.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_resetPasswordFragment)
        }
    }

    override fun invalidate() = withState(viewModel) {
        binding.textLoginIssue.isVisible = it.userLogin is Fail
        when (it.userLogin) {
            is Success -> {
                startActivity(Intent(requireActivity(), MainActivity::class.java))
            }

            is Loading -> {
                showLoadingOverlay(R.string.logging_in)
            }

            is Uninitialized -> {} // not used

            is Fail -> {
                hideLoadingOverlay()
                showSnackbar(it.userLogin.error.stackTraceToString())
            }
        }
    }

    override fun getBinding(): ViewBinding = binding
}