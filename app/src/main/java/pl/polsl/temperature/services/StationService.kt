package pl.polsl.temperature.services

import pl.polsl.temperature.models.StationPost
import pl.polsl.temperature.models.Station
import retrofit2.Call
import retrofit2.http.*

interface StationService {

    @Headers("Content-Type: application/json")
    @GET("stations/{id}/")
    fun getStation(
        @Path("id") id: Long,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String): Call<Station>

    @Headers("Content-Type: application/json")
    @POST("stations/")
    fun addStation(@Body stationPost: StationPost): Call<Station>

}