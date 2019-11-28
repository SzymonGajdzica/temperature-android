package pl.polsl.temperature.models

import java.util.*

data class StationReduced(
    val id: Long,
    var name: String,
    val secretId: UUID

)
