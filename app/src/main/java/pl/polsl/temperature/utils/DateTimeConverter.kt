package pl.polsl.temperature.utils

import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat

object DateTimeConverter {

    fun parseStringToDate(dateString: String?): DateTime?{
        return try{
            DateTime.parse(dateString, ISODateTimeFormat.dateTimeParser())
        }catch (e: Throwable){
            null
        }
    }

    fun parseDateToString(dateTime: DateTime?): String?{
        return try{
            ISODateTimeFormat.dateTime().print(dateTime)
        }catch (e: Throwable){
            null
        }
    }

}