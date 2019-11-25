package pl.polsl.temperature.application

import android.app.Activity
import android.app.Dialog
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.gson.Gson
import pl.polsl.temperature.models.Message
import retrofit2.Response

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