package seamonster.kraken.androidep7.core

import android.util.Log
import android.view.View
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.airbnb.mvrx.MavericksView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import seamonster.kraken.androidep7.R
import seamonster.kraken.androidep7.utils.LoadingOverlayDialog

abstract class BaseFragment(@LayoutRes id: Int) : Fragment(id), MavericksView {

    companion object {
        private const val TAG = "BaseFragment"
    }

    private var dialog: LoadingOverlayDialog? = null

    protected fun showSnackbar(
        @StringRes textId: Int,
        duration: Int = Snackbar.LENGTH_SHORT,
        @StringRes actionTextId: Int? = null,
        actionHandler: View.OnClickListener? = null
    ) {
        val message = getString(textId)
        val actionText = if (actionTextId != null) getString(actionTextId) else null
        showSnackbar(message, duration, actionText, actionHandler)
    }

    protected fun showSnackbar(
        message: String,
        duration: Int = Snackbar.LENGTH_SHORT,
        actionText: String? = null,
        actionHandler: View.OnClickListener? = null
    ) {
        Snackbar.make(getBinding().root, message, duration).apply {
            if (!actionText.isNullOrBlank() && actionHandler != null) {
                setAction(actionText, actionHandler)
            }
        }.show()
    }

    protected fun showErrorDialog(
        @StringRes titleResId: Int,
        @StringRes messageResId: Int,
    ) {
        MaterialAlertDialogBuilder(requireContext())
            .setIcon(R.drawable.round_error_outline_24)
            .setTitle(titleResId)
            .setMessage(messageResId)
            .setNegativeButton(getString(R.string.dialog_negative_button)) { _, _ -> }
            .show()
    }

    /**
     * Show an [LoadingOverlayDialog] to prevent user from interacting with screen components.
     */
    protected fun showLoadingOverlay(@StringRes messageResId: Int) {
        dialog = LoadingOverlayDialog(requireContext()).apply { text = getString(messageResId) }
        dialog!!.show()
    }

    /**
     * Remove overlay that has been shown by [showLoadingOverlay]
     */
    protected fun hideLoadingOverlay() {
        try {
            dialog!!.dismiss()
        } catch (e: Exception) {
            Log.e(TAG, "hideLoadingOverlay: showLoadingDialog() should've been called first.", e)
        }
        dialog = null
    }

    abstract fun getBinding(): ViewBinding
}