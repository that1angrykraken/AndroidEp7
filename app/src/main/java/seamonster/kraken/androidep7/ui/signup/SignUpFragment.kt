package seamonster.kraken.androidep7.ui.signup

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.forEachIndexed
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import seamonster.kraken.androidep7.R
import seamonster.kraken.androidep7.core.BaseFragment
import seamonster.kraken.androidep7.data.models.User
import seamonster.kraken.androidep7.databinding.FragmentSignupBinding
import seamonster.kraken.androidep7.util.ApiExtension
import seamonster.kraken.androidep7.util.isValid
import seamonster.kraken.androidep7.util.viewBinding

class SignUpFragment : BaseFragment(R.layout.fragment_signup) {

    private val binding: FragmentSignupBinding by viewBinding()

    private val viewModel: SignUpViewModel by fragmentViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeComponents()
    }

    override fun onDestroy() {
        viewModel.onCleared()
        super.onDestroy()
    }

    @SuppressLint("MissingSuperCall")
    override fun invalidate(): Unit = withState(viewModel) { state ->
        if (state.signUp !is Loading) hideLoadingOverlay()
        when (state.signUp) {
            is Success -> {
                showSnackbar(getString(R.string.sign_up_success), Snackbar.LENGTH_LONG)
            }

            is Loading -> {
                showLoadingOverlay()
            }

            is Fail -> {
                when (ApiExtension.getResponseCode(state.signUp.error)) {
                    401 -> {
                        binding.textFieldUsername.error = getString(R.string.account_already_exist)
                        showSnackbar(R.string.one_or_more_fields_are_not_valid)
                    }
                    else -> showSnackbar(R.string.unexpected_error)
                }
            }

            Uninitialized -> {}
        }
    }

    private fun initializeComponents() {
        binding.user = User()

        val navController = findNavController()
        binding.buttonGoToLogin.setOnClickListener {
            navController.navigateUp()
        }
        binding.buttonSignUp.setOnClickListener {
            if (shouldSignUp(binding.linearLayoutCompat)) {
                viewModel.setUser(binding.user)
                viewModel.handleSignUp()
            } else {
                showSnackbar(getString(R.string.one_or_more_fields_are_not_valid))
            }
        }
        binding.textPassword.addTextChangedListener {
            // trigger listener
            binding.textRePassword.run { text = text }
        }
        binding.textFieldRePassword.apply {
            editText?.addTextChangedListener {
                val compare = binding.user?.confirmPassword != binding.user?.password
                error = if (compare) { getString(R.string.must_match_with_password) } else null
            }
        }
    }

    private fun shouldSignUp(layout: LinearLayoutCompat): Boolean {
        var check = true
        layout.forEachIndexed { _, view ->
            val valid = when (view) {
                is TextInputLayout -> view.isValid()
                is LinearLayoutCompat -> shouldSignUp(view)
                else -> true
            }
            if (check) check = valid
        }
        return check
    }

    override fun getBinding(): ViewBinding = binding

    companion object {
        private const val TAG = "SignUpFragment"
    }
}