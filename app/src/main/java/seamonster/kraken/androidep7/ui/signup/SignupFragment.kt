package seamonster.kraken.androidep7.ui.signup

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.forEachIndexed
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.airbnb.mvrx.*
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import seamonster.kraken.androidep7.R
import seamonster.kraken.androidep7.core.BaseFragment
import seamonster.kraken.androidep7.data.models.User
import seamonster.kraken.androidep7.databinding.FragmentSignupBinding
import seamonster.kraken.androidep7.util.isValid
import seamonster.kraken.androidep7.util.viewBinding

class SignupFragment : BaseFragment(R.layout.fragment_signup) {

    private val binding: FragmentSignupBinding by viewBinding()

    private val viewModel: SignupViewModel by fragmentViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeComponents()
    }

    override fun invalidate() = withState(viewModel) { state ->
        if (state.signUp !is Loading) hideLoadingOverlay()
        when (state.signUp) {
            is Success -> {
                showSnackbar(getString(R.string.sign_up_success), Snackbar.LENGTH_LONG)
            }
            is Loading -> showLoadingOverlay()
            is Fail -> showSnackbar(state.signUp.error)
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
                viewModel.signUp(binding.user!!)
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