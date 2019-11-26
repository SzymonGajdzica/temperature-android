package pl.polsl.temperature.application

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.multidex.MultiDexApplication
import androidx.preference.PreferenceManager
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import pl.polsl.temperature.utils.SettingsTools
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit


class ApplicationContext: MultiDexApplication() {

    companion object {

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

        fun getRetrofit(): Retrofit {
            val interceptor = Interceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header("Authorization", "Bearer ${SettingsTools.getToken() ?: ""}")
                    .method(original.method(), original.body())
                    .build()
                log("url = ", request.url())
                chain.proceed(request)
            }
            val gson = GsonBuilder()
                .setLenient()
                .create()
            val okHttpClient = OkHttpClient().newBuilder()
                .addInterceptor(interceptor)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build()
            return Retrofit.Builder()
                .baseUrl("https://temperature-controller.herokuapp.com/temperature/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }

    }

    override fun onCreate() {
        super.onCreate()
        weakReference = WeakReference(applicationContext)
    }

}