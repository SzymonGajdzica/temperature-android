package pl.polsl.temperature.credentials.register

import pl.polsl.temperature.models.UserPost

interface RegisterPresenter {

    fun register(userPost: UserPost)

}