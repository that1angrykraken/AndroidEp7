package seamonster.kraken.androidep7.util

import android.view.View
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout
import seamonster.kraken.androidep7.R
import seamonster.kraken.androidep7.data.models.Page
import seamonster.kraken.androidep7.data.models.User
import seamonster.kraken.androidep7.ui.user.UserAdapter

object BindingAdapters {

    @BindingAdapter("app:inputPattern")
    @JvmStatic
    fun inputPattern(view: TextInputLayout, pattern: String? = null) {
        if (pattern.isNullOrBlank()) return
        view.editText?.addTextChangedListener { text ->
            if ((text ?: "").matches(pattern.toRegex())) {
                view.error = null
                view.isErrorEnabled = false
            }
            else {
                view.error = view.context.getString(R.string.is_not_valid, view.hint)
            }
        }
        view.editText?.onFocusChangeListener = View.OnFocusChangeListener { _, isFocused ->
            if (isFocused) view.isErrorEnabled = false
        }
    }

    @BindingAdapter("app:adapter")
    @JvmStatic
    fun adapter(view: RecyclerView, adapter: Adapter<ViewHolder>?) {
        if (adapter != null) view.adapter = adapter
    }

    @BindingAdapter("app:imageUrl")
    @JvmStatic
    fun imageUrl(view: ImageView, url: String? = null) {
        if (url.isNullOrEmpty()) return
        Glide.with(view).load(url).into(view)
    }

}

