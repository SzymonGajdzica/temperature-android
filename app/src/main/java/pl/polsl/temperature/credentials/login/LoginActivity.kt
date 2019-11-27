package pl.polsl.temperature.credentials.login

import pl.polsl.temperature.application.BaseActivity

interface LoginActivity {

    fun loginSucceed()

    fun getContext(): BaseActivity

}