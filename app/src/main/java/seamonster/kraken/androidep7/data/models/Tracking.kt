package seamonster.kraken.androidep7.data.models

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import java.util.Calendar
import java.util.Date

data class Tracking(
    @get:Bindable
    var content: String? = null,
    @get:Bindable
    val date: Calendar? = null,
    val id: Int? = null,
    @get:Bindable
    val user: User? = null
): BaseObservable()