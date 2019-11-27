package pl.polsl.temperature.services

import pl.polsl.temperature.models.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthenticationService {

    @Headers("Content-Type: application/json")
    @POST("authenticate/login/")
    fun login(@Body authenticationPost: AuthenticationPost): Call<Authentication>

    @Headers("Content-Type: application/json")
    @POST("authenticate/register/")
    fun register(@Body userPost: UserPost): Call<Message>

}