package seamonster.kraken.androidep7.util

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CalendarTypeAdapter : JsonSerializer<Calendar>, JsonDeserializer<Calendar> {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())

    override fun serialize(
        src: Calendar?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(dateFormat.format(src?.time))
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Calendar {
        val calendar = Calendar.getInstance()
        try {
            val date = dateFormat.parse(json?.asString)
            calendar.time = date
        } catch (e: ParseException) {
            throw JsonParseException(e)
        }
        return calendar
    }
}