package pl.polsl.temperature.models

import java.util.*

data class Station(
    val id: Long,
    var name: String,
    val measurements: ArrayList<Measurement>,
    val secretId: UUID
)
