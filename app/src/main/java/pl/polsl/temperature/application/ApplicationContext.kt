package pl.polsl.temperature.application

import android.content.Context
import android.content.SharedPreferences
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

        fun getSharedPreferences(): SharedPreferences?{
            return getAppContext()
                ?.let { PreferenceManager.getDefaultSharedPreferences(it) }
        }

        fun log(vararg values: Any?){
            Log.d("halo", values.joinToString(separator = " "))
        }

        fun getRetrofit(): Retrofit {
            val interceptor = Interceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header("Authorization", "Bearer ${SettingsTools.getToken() ?: ""}")
                    .method(original.method(), original.body())
                    .build()
                log("url = ", request.url().url())
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
                .baseUrl("https://temperature-controller.herokuapp.com/temperature-controller/")
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