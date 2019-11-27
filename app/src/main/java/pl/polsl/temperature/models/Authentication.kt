package pl.polsl.temperature.models

import java.util.*

data class Authentication(
    val token: String,
    val expirationDate: Date
)