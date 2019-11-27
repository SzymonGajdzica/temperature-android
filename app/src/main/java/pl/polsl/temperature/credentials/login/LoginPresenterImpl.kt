package pl.polsl.temperature.credentials.login

import pl.polsl.temperature.application.ApplicationContext
import pl.polsl.temperature.models.AuthenticationPost
import pl.polsl.temperature.services.AuthenticationService
import pl.polsl.temperature.utils.SettingsTools
import retrofit2.create

class LoginPresenterImpl(private val loginActivity: LoginActivity): LoginPresenter {

    private val authenticationService: AuthenticationService = ApplicationContext.getRetrofit().create()

    override fun login(authenticationPost: AuthenticationPost) {
        authenticationService.login(authenticationPost).enqueue(loginActivity.getContext().createCallback {
            SettingsTools.setToken(it)
            loginActivity.loginSucceed()
        })
    }

}