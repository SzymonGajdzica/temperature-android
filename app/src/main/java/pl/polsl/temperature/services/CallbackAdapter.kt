package pl.polsl.temperature.services

import pl.polsl.temperature.application.ApplicationContext
import pl.polsl.temperature.application.BaseActivity
import pl.polsl.temperature.application.getMessageString
import pl.polsl.temperature.utils.OneToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CallbackAdapter<T>(
    private val baseActivity: BaseActivity,
    private val callback: (result: T) -> Unit): Callback<T> {

    init {
        baseActivity.showLoader()
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        baseActivity.hideLoader()
        loadingFailed(t.message)
    }

    override fun onResponse(call: Call<T>, response: Response<T>) {
        baseActivity.hideLoader()
        val resultObject = response.body()
        if (response.isSuccessful && resultObject != null)
            callback(resultObject)
         else
            loadingFailed(response.getMessageString())
    }

    private fun loadingFailed(message: String?){
        ApplicationContext.log(message)
        OneToast.show(message)
    }

}