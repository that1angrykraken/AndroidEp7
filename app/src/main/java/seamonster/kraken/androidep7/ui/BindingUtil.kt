package seamonster.kraken.androidep7.ui

import androidx.databinding.InverseMethod
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import seamonster.kraken.androidep7.R
import seamonster.kraken.androidep7.util.format
import java.util.Calendar
import java.util.Date

object BindingUtil {

    @InverseMethod("stringToInteger")
    @JvmStatic
    fun integerToString(value: Int?): String {
        return (value ?: "").toString()
    }

    @JvmStatic
    fun stringToInteger(value: String?): Int? {
        if (value.isNullOrEmpty()) return null
        return value.toInt()
    }

    @JvmStatic
    fun intToString(value: Int?): String {
        return (value ?: 0).toString()
    }

    @InverseMethod("shortenStringGender")
    @JvmStatic
    fun stringGender(
        view: MaterialAutoCompleteTextView,
        value: String?
    ): String? {
        if (value.isNullOrEmpty()) return null
        return if (value == "M") view.context.getString(R.string.gender_male)
        else view.context.getString(R.string.gender_female)
    }

    @JvmStatic
    fun shortenStringGender(
        view: MaterialAutoCompleteTextView,
        value: String?
    ): String? {
        if(value.isNullOrEmpty()) return null
        return if (value == view.context.getString(R.string.gender_male)) "M" else "F"
    }

    @JvmStatic
    fun calendarToString(calendar: Calendar?): String? {
        if (calendar == null) return null
        return calendar.format()
    }
}