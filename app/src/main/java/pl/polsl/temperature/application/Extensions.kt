package pl.polsl.temperature.application

import android.app.Activity
import android.app.Dialog
import android.content.res.Resources
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.gson.Gson
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

fun Activity.hideKeyboard(){
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
    val view = currentFocus ?: View(this)
    imm?.hideSoftInputFromWindow(view.windowToken, 0)
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

fun Date.dateToShortString(): String{
    val sdf = SimpleDateFormat("HH:mm:ss dd-MM-yyyy ", Locale.ENGLISH)
    return sdf.format(this)
}

fun Date.dateToString(): String{
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ENGLISH)
    return sdf.format(this)
}

fun String?.stringToDate(): Date?{
    return try{
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ENGLISH)
        sdf.parse(this.toString())
    }catch (e: Throwable){
        null
    }
}

fun Station.reduce(): StationReduced{
    return StationReduced(id, name)
}

fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()