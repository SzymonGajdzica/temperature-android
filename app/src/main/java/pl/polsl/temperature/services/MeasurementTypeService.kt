package pl.polsl.temperature.services

import pl.polsl.temperature.models.AddMeasurementType
import pl.polsl.temperature.models.MeasurementType
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface MeasurementTypeService {

    @Headers("Content-Type: application/json")
    @POST("measurement_types/")
    fun addMeasurementType(@Body addMeasurementType: AddMeasurementType): Call<MeasurementType>

    @Headers("Content-Type: application/json")
    @GET("measurement_types/")
    fun getMeasurementTypes(): Call<List<MeasurementType>>

}