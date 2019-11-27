package pl.polsl.temperature.services

import pl.polsl.temperature.models.*
import retrofit2.Call
import retrofit2.http.*

interface GatewayService {

    @Headers("Content-Type: application/json")
    @POST("gateways/")
    fun addGateway(@Body gatewayPost: GatewayPost): Call<Gateway>

}