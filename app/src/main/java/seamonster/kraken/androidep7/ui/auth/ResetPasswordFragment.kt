package seamonster.kraken.androidep7.ui.auth

import seamonster.kraken.androidep7.core.BaseFragment
import androidx.viewbinding.ViewBinding
import seamonster.kraken.androidep7.R
import seamonster.kraken.androidep7.databinding.FragmentResetPasswordBinding
import seamonster.kraken.androidep7.utils.viewBinding

class ResetPasswordFragment: BaseFragment(R.layout.fragment_reset_password) {

    private val binding: FragmentResetPasswordBinding by viewBinding()

    override fun invalidate() {
        TODO("Not yet implemented")
    }

    override fun getBinding(): ViewBinding = binding
}