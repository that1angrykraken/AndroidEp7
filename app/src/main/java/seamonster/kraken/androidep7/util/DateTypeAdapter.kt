package seamonster.kraken.androidep7.util

import com.google.gson.*
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateTypeAdapter : JsonDeserializer<Date>, JsonSerializer<Date> {

    private const val DATE_FORMATS = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Date? {
        val dateFormatter =  SimpleDateFormat(DATE_FORMATS, Locale.getDefault())
        try {
            return json?.asString?.let { dateFormatter.parse(it) }
        } catch (ignore: ParseException) { }

        throw JsonParseException("DateParseException: " + json.toString())
    }

    override fun serialize(src: Date?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src?.format(DATE_FORMATS))
    }
}