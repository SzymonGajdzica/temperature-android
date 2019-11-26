package pl.polsl.temperature.application

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.loading_layout.*

abstract class BaseActivity: AppCompatActivity() {

    protected fun showLoader(){
        loader?.visibility = View.VISIBLE
    }

    protected fun hideLoader(){
        loader?.visibility = View.GONE
    }

    protected fun isLoading(): Boolean{
        return loader?.visibility == View.VISIBLE
    }

}