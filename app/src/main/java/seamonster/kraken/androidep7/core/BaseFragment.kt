package seamonster.kraken.androidep7.core

import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.CallSuper
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.airbnb.mvrx.MavericksView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import seamonster.kraken.androidep7.R
import seamonster.kraken.androidep7.ui.LoadingOverlayDialog

abstract class BaseFragment(@LayoutRes id: Int) : Fragment(id), MavericksView {

    companion object {
        private const val TAG = "BaseFragment"
    }

    private var overlay: LoadingOverlayDialog? = null
    private var snackbar: Snackbar? = null

    @CallSuper
    override fun onStart() {
        Log.i(TAG, "onStart: ${javaClass.simpleName}")
        super.onStart()
    }

    @CallSuper
    override fun onResume() {
        Log.i(TAG, "onResume: ${javaClass.simpleName}")
        super.onResume()
    }

    @CallSuper
    override fun onDestroyView() {
        Log.i(TAG, "onDestroyView: ${javaClass.simpleName}")
        super.onDestroyView()
    }

    @CallSuper
    override fun onDestroy() {
        Log.i(TAG, "onDestroy: ${javaClass.simpleName}")
        super.onDestroy()
    }

    protected fun hideSoftKeyboard(view: View) {
        val imm = requireContext().applicationContext
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    protected fun showSnackbar(
        @StringRes messageId: Int,
        duration: Int = Snackbar.LENGTH_SHORT,
        @StringRes actionId: Int? = null,
        handler: ((View) -> Unit)? = null
    ) {
        val message = getString(messageId)
        val actionText = if (actionId != null) getString(actionId) else null
        showSnackbar(message, duration, actionText, handler)
    }

    protected fun showSnackbar(
        t: Throwable,
        duration: Int = Snackbar.LENGTH_SHORT,
        action: String? = null,
        handler: ((View) -> Unit)? = null
    ) {
        t.printStackTrace()
        val message = t.message ?: getString(R.string.unexpected_error)
        showSnackbar(message, duration, action, handler)
    }

    protected fun showSnackbar(
        message: String,
        duration: Int = Snackbar.LENGTH_SHORT,
        action: String? = null,
        handler: ((View) -> Unit)? = null
    ) {
        snackbar?.dismiss()
        snackbar = Snackbar.make(getBinding().root, message, duration).apply {
            setAction(action, handler)
        }
        snackbar?.show()
    }

    protected fun createDialog(
        @StringRes titleId: Int,
        @StringRes messageId: Int,
        @DrawableRes iconId: Int? = null,
        handler: ((DialogInterface, Int) -> Unit)? = null
    ): MaterialAlertDialogBuilder {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(titleId)
            .setMessage(messageId)
            .setNegativeButton(R.string.dialog_negative_button) { _, _ -> }
            .setPositiveButton(R.string.dialog_positive_button, handler)
        iconId?.let { dialog.setIcon(it) }
        return dialog
    }

    protected fun showErrorDialog(
        @StringRes titleResId: Int,
        @StringRes messageResId: Int,
        handler: (() -> Unit)? = null
    ) {
        val title = getString(titleResId)
        val message = getString(messageResId)
        showErrorDialog(title, message, handler)
    }

    protected fun showErrorDialog(
        t: Throwable,
        handler: (() -> Unit)? = null
    ) {
        t.printStackTrace()
        val title = getString(R.string.error_title)
        val message = t.message ?: getString(R.string.unexpected_error)
        showErrorDialog(title, message, handler)
    }

    protected fun showErrorDialog(
        title: String,
        message: String,
        handler: (() -> Unit)? = null
    ) {
        MaterialAlertDialogBuilder(requireContext())
            .setIcon(R.drawable.round_error_outline_24)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(R.string.dialog_positive_button) { dialog, _ ->
                dialog.dismiss()
                if (handler != null) {
                    handler()
                }
            }
            .show()
    }

    /**
     * Show an [LoadingOverlayDialog] to prevent user from interacting with screen components.
     *
     * Usually uses to prevent user from sending multiple requests at once.
     */
    protected fun showLoadingOverlay(@StringRes messageResId: Int? = null) {
        overlay = LoadingOverlayDialog(requireContext()).apply {
            if (messageResId != null) text = getString(messageResId)
        }
        overlay!!.show()
    }

    /**
     * Remove overlay that has been shown by [showLoadingOverlay]
     */
    protected fun hideLoadingOverlay() {
        try {
            overlay!!.dismiss()
        } catch (e: Exception) {
            val message =
                "showLoadingOverlay() should've been called first.\nsender: ${javaClass.simpleName}"
            Log.i(TAG, "hideLoadingOverlay: $message", e)
        }
        overlay = null
    }

    abstract fun getBinding(): ViewBinding
}