package seamonster.kraken.androidep7.ui

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import seamonster.kraken.androidep7.R
import seamonster.kraken.androidep7.databinding.DialogFullscreenBinding
import seamonster.kraken.androidep7.util.viewBinding

class FullscreenDialog: DialogFragment(R.layout.dialog_fullscreen) {

    companion object {
        const val TAG = "FullscreenDialog"
    }

    private val binding: DialogFullscreenBinding by viewBinding()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), R.style.Theme_AndroidEp7_FullscreenDialog)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mIcon?.let {
            with(binding.imgDialogIcon) {
                isVisible = true
                setImageResource(it)
            }
        }
        mTitle?.let { binding.textDialogTitle.setText(it) }
        mMessage?.let { binding.textDialogMessage.setText(it) }
        mPositiveButton?.let {
            with(binding.buttonPositive) {
                isVisible = true
                setText(it)
                setOnClickListener {
                    mPositiveButtonHandler!!(it)
                    dialog?.dismiss()
                }
            }
        }
        mNegativeButton?.let {
            with(binding.buttonNegative) {
                isVisible = true
                setText(it)
                setOnClickListener {
                    mNegativeButtonHandler!!(it)
                    dismiss()
                }
            }
        }
        mNeutralButton?.let {
            with(binding.buttonNeutral) {
                isVisible = true
                setText(it)
                setOnClickListener {
                    mNeutralButtonHandler!!(it)
                    dismiss()
                }
            }
        }
        dialog?.setContentView(binding.root)
        dialog?.setCancelable(false)
    }

    @StringRes
    private var mPositiveButton: Int? = null
    private var mPositiveButtonHandler: ((View) -> Unit)? = null

    @StringRes
    private var mNegativeButton: Int? = null
    private var mNegativeButtonHandler: ((View) -> Unit)? = null

    @StringRes
    private var mNeutralButton: Int? = null
    private var mNeutralButtonHandler: ((View) -> Unit)? = null

    @StringRes
    private var mTitle: Int? = null
    @StringRes
    private var mMessage: Int? = null
    @DrawableRes
    private var mIcon: Int? = null

    inner class Builder {
        fun setIcon(@DrawableRes id: Int): Builder {
            this@FullscreenDialog.mIcon = id
            return this
        }

        fun setTitle(@StringRes id: Int): Builder {
            this@FullscreenDialog.mTitle = id
            return this
        }

        fun setMessage(@StringRes id: Int): Builder {
            this@FullscreenDialog.mMessage = id
            return this
        }

        fun setPositiveButton(@StringRes id: Int, handler: (View) -> Unit): Builder {
            this@FullscreenDialog.mPositiveButton = id
            this@FullscreenDialog.mPositiveButtonHandler = handler
            return this
        }

        fun setNegativeButton(@StringRes id: Int, handler: (View) -> Unit): Builder {
            this@FullscreenDialog.mNegativeButton = id
            this@FullscreenDialog.mNegativeButtonHandler = handler
            return this
        }

        fun setNeutralButton(@StringRes id: Int, handler: (View) -> Unit): Builder {
            this@FullscreenDialog.mNeutralButton = id
            this@FullscreenDialog.mNeutralButtonHandler = handler
            return this
        }

        fun build(): FullscreenDialog = this@FullscreenDialog
    }

}