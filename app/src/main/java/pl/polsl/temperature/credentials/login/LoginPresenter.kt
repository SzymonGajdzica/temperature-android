package pl.polsl.temperature.credentials.login

import pl.polsl.temperature.models.AuthenticationPost

interface LoginPresenter {

    fun login(authenticationPost: AuthenticationPost)

}