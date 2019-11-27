package pl.polsl.temperature.credentials.register

import pl.polsl.temperature.application.BaseActivity

interface RegisterActivity {

    fun registerSucceed()

    fun getContext(): BaseActivity

}