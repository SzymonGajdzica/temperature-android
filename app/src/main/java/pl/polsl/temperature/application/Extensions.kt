package pl.polsl.temperature.application

import android.app.Activity
import android.app.Dialog
import android.view.View
import android.view.inputmethod.InputMethodManager

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