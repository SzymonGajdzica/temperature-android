package pl.polsl.temperature.application

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.multidex.MultiDexApplication
import androidx.preference.PreferenceManager
import net.danlew.android.joda.JodaTimeAndroid
import java.lang.ref.WeakReference
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.Executor
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class ApplicationContext: MultiDexApplication() {

    companion object {

        private lateinit var threadPoolExecutor: ThreadPoolExecutor
        private lateinit var weakReference: WeakReference<Context>

        fun getAppContext(): Context? {
            return weakReference.get()
        }

        fun getString(id: Int): String?{
            return getAppContext()
                ?.getString(id)
        }

        fun getSharedPreferences(): SharedPreferences?{
            return getAppContext()
                ?.let { PreferenceManager.getDefaultSharedPreferences(it) }
        }

        fun log(vararg values: Any?){
            Log.d(Keys.TAG, values.joinToString(separator = " "))
        }

        @Suppress("DEPRECATION")
        fun isNetworkAvailable(): Boolean {
            val connectivityManager = getAppContext()?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            if (Build.VERSION.SDK_INT < 23)
                return connectivityManager?.activeNetworkInfo?.isConnected == true
            else {
                val nc = connectivityManager?.getNetworkCapabilities(connectivityManager.activeNetwork)
                    ?: return false
                return nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            }
        }

    }

    override fun onCreate() {
        super.onCreate()
        weakReference = WeakReference(applicationContext)
        JodaTimeAndroid.init(this)
    }

}