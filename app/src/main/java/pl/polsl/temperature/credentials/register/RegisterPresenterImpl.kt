package pl.polsl.temperature.credentials.register

import pl.polsl.temperature.application.ApplicationContext
import pl.polsl.temperature.models.UserPost
import pl.polsl.temperature.services.AuthenticationService
import retrofit2.create

class RegisterPresenterImpl(private val registerActivity: RegisterActivity): RegisterPresenter {

    private val authenticationService: AuthenticationService = ApplicationContext.getRetrofit().create()

    override fun register(userPost: UserPost) {
        authenticationService.register(userPost).enqueue(registerActivity.getContext().createCallback {
            registerActivity.registerSucceed()
        })
    }

}