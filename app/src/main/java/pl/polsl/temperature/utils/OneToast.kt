package pl.polsl.temperature.utils

import android.widget.Toast
import androidx.annotation.StringRes
import pl.polsl.temperature.R
import pl.polsl.temperature.application.ApplicationContext
import java.lang.ref.WeakReference

object OneToast {

    private var toast: WeakReference<Toast>? = null

    fun show(text: String?) {
        text ?: return
        val context = ApplicationContext.getAppContext() ?: return
        toast = WeakReference(Toast.makeText(context, text.toString(), Toast.LENGTH_SHORT).apply { show() })
    }

    fun show(@StringRes textId: Int) {
        toast?.get()?.cancel()
        val context = ApplicationContext.getAppContext() ?: return
        toast = WeakReference(Toast.makeText(context, context.getString(textId), Toast.LENGTH_SHORT).apply { show() })
    }

}