package pl.polsl.temperature.credentials.register

import pl.polsl.temperature.models.RegisterUser

interface RegisterPresenter {

    fun register(registerUser: RegisterUser)

}