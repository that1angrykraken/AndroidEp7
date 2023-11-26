package seamonster.kraken.androidep7.util

import android.location.Location
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import okhttp3.ResponseBody
import seamonster.kraken.androidep7.data.models.ErrorResponse
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun Calendar?.isToday(timeZone: TimeZone = TimeZone.getDefault()): Boolean {
    if (this == null) return false
    val removeTime: Calendar.() -> Unit = {
        this.timeZone = timeZone
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    removeTime()
    val today = Calendar.getInstance().apply(removeTime)
    return timeInMillis == today.timeInMillis
}

fun Calendar.format(format: String? = null): String {
    val dateFormat = SimpleDateFormat(format ?: "HH:mm dd-MM-yyyy", Locale.getDefault())
    return dateFormat.format(time)
}

fun ResponseBody?.toMessage(): String? {
    if (this == null) return null
    val response: ErrorResponse? = Gson().fromJson(this.string(), ErrorResponse::class.java)
    return if (response != null) {
        "${response.error}: ${response.errorDescription}"
    } else ""
}

/**
 * Check if the text inside the editText which attached to this view is valid.
 * Works properly if a pattern has been defined for this view.
 */
fun TextInputLayout.isValid(): Boolean {
    editText?.run { text = text }
    return error.isNullOrEmpty()
}

fun ImageView.setImageUrl(url: String?) {
    Glide.with(context).load(url).into(this)
}

fun Location?.toText(default: Boolean = false): String? {
    return if (this != null) {
        "($latitude, $longitude)"
    } else {
        if (default) "Unknown location" else null
    }
}

fun Date.format(format: String? = null): String {
    val ld = toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    return ld.format(DateTimeFormatter.ofPattern(format ?: "dd/MM/yyyy"))
}

fun AppCompatActivity.addFragment(
    frameId: Int,
    fragment: Fragment,
    allowStateLoss: Boolean = false
) {
    supportFragmentManager.commitTransaction(allowStateLoss) { add(frameId, fragment) }
}

inline fun androidx.fragment.app.FragmentManager.commitTransaction(
    allowStateLoss: Boolean = false,
    func: FragmentTransaction.() -> FragmentTransaction
) {
    val transaction = beginTransaction().func()
    if (allowStateLoss) {
        transaction.commitAllowingStateLoss()
    } else {
        transaction.commit()
    }
}

fun <T : Fragment> AppCompatActivity.addFragmentToBackstack(
    frameId: Int,
    fragmentClass: Class<T>,
    tag: String? = null,
    allowStateLoss: Boolean = false,
    option: ((FragmentTransaction) -> Unit)? = null
) {
    supportFragmentManager.commitTransaction(allowStateLoss) {
        option?.invoke(this)
        replace(frameId, fragmentClass, null, tag).addToBackStack(tag)
    }
}