package seamonster.kraken.androidep7.ui.settings

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import seamonster.kraken.androidep7.R
import seamonster.kraken.androidep7.core.BaseFragment
import seamonster.kraken.androidep7.databinding.FragmentSettingsBinding
import seamonster.kraken.androidep7.util.LocaleHelper
import seamonster.kraken.androidep7.util.ThemeHelper
import seamonster.kraken.androidep7.util.viewBinding

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    private val binding: FragmentSettingsBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeComponents()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.clear()
        super.onSaveInstanceState(outState)
    }

    // if there aren't any viewModel attached to this fragment, this method will not be called
    override fun invalidate() {}

    private fun initializeComponents() = binding.run {
        toolbarSettings.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        when (LocaleHelper.getLanguage()) {
            "vi" -> buttonLangVi.isChecked = true
            else -> buttonLangEn.isChecked = true
        }

        buttonLangVi.setOnClickListener {
            LocaleHelper.setLanguage("vi")
        }
        buttonLangEn.setOnClickListener {
            LocaleHelper.setLanguage("en")
        }

        when (ThemeHelper.getThemeMode(requireContext())) {
            MODE_NIGHT_NO -> buttonThemeDay.isChecked = true
            MODE_NIGHT_YES -> buttonThemeNight.isChecked = true
            MODE_NIGHT_FOLLOW_SYSTEM -> {
                buttonThemeSystem.isChecked = true
            }
        }

        buttonThemeSystem.setOnClickListener {
            setTheme(MODE_NIGHT_FOLLOW_SYSTEM)
        }
        buttonThemeDay.setOnClickListener {
            setTheme(MODE_NIGHT_NO)
        }
        buttonThemeNight.setOnClickListener {
            setTheme(MODE_NIGHT_YES)
        }
    }

    private fun setTheme(theme: Int) {
        ThemeHelper.setThemeMode(requireActivity(), theme)
        ThemeHelper.changeTheme(requireActivity())
    }

    override fun getBinding(): ViewBinding = binding

    companion object {
        private const val TAG = "SettingsFragment"
    }
}