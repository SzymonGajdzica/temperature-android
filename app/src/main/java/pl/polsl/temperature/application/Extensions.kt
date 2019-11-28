package pl.polsl.temperature.application

import android.app.Activity
import android.app.Dialog
import android.content.res.Resources
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.gson.Gson
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatterBuilder
import pl.polsl.temperature.models.Message
import pl.polsl.temperature.models.Station
import pl.polsl.temperature.models.StationReduced
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

fun Any?.toMString(): String{
    return this?.toString() ?: ""
}

fun Dialog.mShow(activity: Activity?){
    if(activity?.isFinishing == false)
        show()
}

fun <T> Response<T>.getMessage(): Message?{
    return try {
        Gson().fromJson(errorBody()?.string(), Message::class.java)
    }catch (e: Throwable){
        null
    }
}

fun <T> Response<T>.getMessageString(): String?{
    return getMessage()?.run{ title + "\n" + description }
}

fun DateTime.dateToShortString(): String{
    val patternFormat = DateTimeFormatterBuilder()
        .appendPattern("HH:mm:ss dd-MM-yyyy")
        .toFormatter()
    return toString(patternFormat)
}

fun DateTime.dateToString(): String{
    val patternFormat = DateTimeFormatterBuilder()
        .appendPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
        .toFormatter()
    ApplicationContext.log(toString(patternFormat))
    return toString(patternFormat)
}

fun String?.stringToDate(): DateTime?{
    return try{
        val patternFormat = DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
            .toFormatter()
        patternFormat.parseDateTime(this)
    }catch (e: Throwable){
        null
    }
}

fun Station.reduce(): StationReduced{
    return StationReduced(id, name)
}

fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()