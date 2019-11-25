package pl.polsl.temperature.credentials.login

import pl.polsl.temperature.models.Credentials

interface LoginPresenter {

    fun login(credentials: Credentials)

}