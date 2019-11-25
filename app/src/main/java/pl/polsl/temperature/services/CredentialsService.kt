package pl.polsl.temperature.services

import pl.polsl.temperature.models.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface CredentialsService {

    @Headers("Content-Type: application/json")
    @POST("authenticate/login/")
    fun login(@Body credentials: Credentials): Call<CredentialsResponse>

    @Headers("Content-Type: application/json")
    @POST("authenticate/register/")
    fun register(@Body registerUser: RegisterUser): Call<Message>

}