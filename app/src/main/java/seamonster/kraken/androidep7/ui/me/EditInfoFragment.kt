package seamonster.kraken.androidep7.ui.me

import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.airbnb.mvrx.*
import com.google.android.material.datepicker.MaterialDatePicker
import seamonster.kraken.androidep7.R
import seamonster.kraken.androidep7.core.BaseFragment
import seamonster.kraken.androidep7.data.models.clone
import seamonster.kraken.androidep7.data.models.toFilteredUser
import seamonster.kraken.androidep7.databinding.FragmentEditMeBinding
import seamonster.kraken.androidep7.ui.main.MainViewModel
import seamonster.kraken.androidep7.util.viewBinding
import java.util.Date

class EditInfoFragment : BaseFragment(R.layout.fragment_edit_me) {

    private val binding: FragmentEditMeBinding by viewBinding()

    private val viewModel: MainViewModel by activityViewModel()

    override fun onStart() {
        super.onStart()
        initializeComponents()
    }

    override fun invalidate() = withState(viewModel) { state ->
        binding.indicatorLoading.isVisible = state.currentUser is Loading
        when (state.currentUser) {
            is Success -> {
                val user = state.currentUser.invoke()
                binding.oldUser = user.toFilteredUser()
                binding.user = binding.oldUser.clone()
            }
            is Fail -> {
                val message = state.currentUser.error.message ?: getString(R.string.unexpected_error)
                showSnackbar(message)
                binding.user = binding.oldUser.clone()
            }
            else -> {}
        }
    }

    private fun initializeComponents() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.buttonSaveChanges.setOnClickListener {
            viewModel.updateMyself(binding.user!!)
        }

        binding.fieldDob.setEndIconOnClickListener {
            showDatePicker()
        }
    }

    private fun showDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(R.string.date_of_birth)
            .setSelection(binding.user?.dob?.time)
            .setInputMode(MaterialDatePicker.INPUT_MODE_TEXT)
            .build()
        datePicker.addOnPositiveButtonClickListener { selection ->
            binding.user?.dob = Date(selection)
        }
        datePicker.show(parentFragmentManager, TAG)
    }

    override fun getBinding(): ViewBinding = binding

    companion object {
        private const val TAG = "EditInfoFragment"
    }
}