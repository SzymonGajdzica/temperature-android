package pl.polsl.temperature.models

import java.util.*

data class Measurement(
    val id: Long,
    val date: Date,
    val value: Double,
    val measurementTypeId: Long
)
