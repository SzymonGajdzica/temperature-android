package pl.polsl.temperature.services

import pl.polsl.temperature.models.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface UserService {

    @Headers("Content-Type: application/json")
    @GET("users/current/")
    fun getUserData(): Call<User>

}