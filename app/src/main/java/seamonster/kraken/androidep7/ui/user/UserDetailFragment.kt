package seamonster.kraken.androidep7.ui.user

import androidx.core.view.allViews
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.withState
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import seamonster.kraken.androidep7.R
import seamonster.kraken.androidep7.core.BaseFragment
import seamonster.kraken.androidep7.data.models.User
import seamonster.kraken.androidep7.data.models.clone
import seamonster.kraken.androidep7.data.models.toFilteredUser
import seamonster.kraken.androidep7.data.sources.UserPreferences
import seamonster.kraken.androidep7.databinding.FragmentUserDetailBinding
import seamonster.kraken.androidep7.util.isValid
import seamonster.kraken.androidep7.util.viewBinding
import java.util.Calendar
import java.util.Date

class UserDetailFragment : BaseFragment(R.layout.fragment_user_detail) {

    private val binding: FragmentUserDetailBinding by viewBinding()

    private val viewModel: UserViewModel by activityViewModel()

    override fun onStart() {
        super.onStart()
        initializeComponents()
    }

    override fun onDestroy() {
        viewModel.resetUserAction()
        super.onDestroy()
    }

    override fun invalidate(): Unit = withState(viewModel) { state ->
        binding.indicatorLoading.isVisible = state.userAction is Loading
        when (state.userAction) {
            is Success -> onSuccess(state.userAction.invoke())
            is Loading -> {}
            is Fail -> onFailed(state.userAction.error)
            Uninitialized -> {
                val id = arguments?.getInt("userId") ?: -1
                viewModel.fetchUser(id)
            }
        }
    }

    private fun onSuccess(user: User?) {
        binding.oldUser = user.toFilteredUser()
        binding.user = binding.oldUser.clone()
    }

    private fun onFailed(t: Throwable) {
        val message = t.message ?: getString(R.string.unexpected_error)
        showSnackbar(message)
        binding.checkboxEdit.callOnClick()
    }

    private fun initializeComponents() {
        val roles = UserPreferences(requireContext()).userRoles
        binding.isAdmin = roles?.contains("ADMIN") ?: false

        binding.buttonBlock.setOnClickListener {
            createDialog(R.string.block_this_user, R.string.block_user_notice) { _, _ ->
                viewModel.blockUser(binding.user!!.id!!)
            }.show()
        }

        binding.buttonUnblock.setOnClickListener {
            createDialog(R.string.unblock_user, R.string.give_access_to_this_user) { _, _ ->
                binding.oldUser?.active = true
                viewModel.updateUser(binding.oldUser!!)
            }.show()
        }

        binding.buttonSaveChanges.setOnClickListener {
            if (shouldSaveChanges()) {
                clearFocus()
                binding.checkboxEdit.isChecked = false
                viewModel.updateUser(binding.user!!)
            } else {
                showSnackbar(R.string.one_or_more_fields_are_not_valid)
            }
        }

        binding.checkboxEdit.run {
            addOnCheckedStateChangedListener { checkbox, state ->
                if (state == MaterialCheckBox.STATE_UNCHECKED) {
                    clearFocus()
                    hideSoftKeyboard(checkbox)
                }
            }
            setOnClickListener {
                if (!this.isChecked) binding.user = binding.oldUser.clone()
            }
        }

        binding.toolbarUserDetail.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.fieldDob.setEndIconOnClickListener {
            showDatePicker()
        }
    }

    private fun clearFocus() {
        binding.layoutUserDetail.allViews.forEach {
            it.clearFocus()
        }
    }

    private fun shouldSaveChanges(): Boolean {
        var check = true
        binding.layoutUserDetail.allViews.forEach { view ->
            if (view is TextInputLayout) {
                val valid = view.isValid()
                if (check) check = valid
            }
        }
        return check
    }

    private fun showDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(R.string.date_of_birth)
            .setSelection(binding.user?.dob?.timeInMillis)
            .setInputMode(MaterialDatePicker.INPUT_MODE_TEXT)
            .build()
        datePicker.addOnPositiveButtonClickListener { selection ->
            binding.user?.dob = Calendar.getInstance().apply { timeInMillis = selection }
        }
        datePicker.show(parentFragmentManager, TAG)
    }

    override fun getBinding(): ViewBinding = binding

    companion object {
        private const val TAG = "UserDetailFragment"
    }
}