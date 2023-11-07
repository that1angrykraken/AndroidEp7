package seamonster.kraken.androidep7.ui.auth

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import seamonster.kraken.androidep7.R
import seamonster.kraken.androidep7.core.BaseFragment
import seamonster.kraken.androidep7.databinding.FragmentSignupBinding
import seamonster.kraken.androidep7.utils.viewBinding

class SignUpFragment: BaseFragment(R.layout.fragment_signup){

    private val binding: FragmentSignupBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonGoToLogin.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun invalidate() {
        TODO("Not yet implemented")
    }

    override fun getBinding(): ViewBinding = binding
}