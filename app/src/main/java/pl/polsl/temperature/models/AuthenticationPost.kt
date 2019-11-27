package pl.polsl.temperature.models

import com.google.gson.annotations.SerializedName

data class AuthenticationPost(
    val username: String,
    val password: String
)