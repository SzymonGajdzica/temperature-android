package pl.polsl.temperature.application

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.loading_layout.*
import pl.polsl.temperature.services.CallbackAdapter
import pl.polsl.temperature.utils.OneToast

abstract class BaseActivity: AppCompatActivity() {

    fun showLoader(){
        loader?.visibility = View.VISIBLE
    }

    fun hideLoader(){
        loader?.visibility = View.GONE
    }

    fun<T> createCallback(callback: (result: T) -> Unit): CallbackAdapter<T> {
        return CallbackAdapter(this, callback)
    }

}