package pl.polsl.temperature.models

import java.util.*

data class CredentialsResponse(
    val token: String,
    val expirationDate: Date
)