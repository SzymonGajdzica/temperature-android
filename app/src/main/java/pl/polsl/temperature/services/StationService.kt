package pl.polsl.temperature.services

import pl.polsl.temperature.models.Station
import pl.polsl.temperature.models.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.*

interface StationService {

    @Headers("Content-Type: application/json")
    @GET("stations/{id}/")
    fun getStation(
        @Path("id") id: Long,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String): Call<Station>

}