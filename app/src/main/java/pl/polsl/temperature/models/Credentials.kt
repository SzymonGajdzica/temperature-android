package pl.polsl.temperature.models

import com.google.gson.annotations.SerializedName

data class Credentials(
    val username: String,
    val password: String
)