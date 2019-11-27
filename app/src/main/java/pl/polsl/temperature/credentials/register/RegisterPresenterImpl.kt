package pl.polsl.temperature.credentials.register

import pl.polsl.temperature.application.ApplicationContext
import pl.polsl.temperature.models.RegisterUser
import pl.polsl.temperature.services.CredentialsService
import retrofit2.create

class RegisterPresenterImpl(private val registerActivity: RegisterActivity): RegisterPresenter {

    private val credentialsService: CredentialsService = ApplicationContext.getRetrofit().create()

    override fun register(registerUser: RegisterUser) {
        credentialsService.register(registerUser).enqueue(registerActivity.getContext().createCallback {
            registerActivity.registerSucceed()
        })
    }

}