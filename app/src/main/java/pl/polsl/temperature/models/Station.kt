package pl.polsl.temperature.models

data class Station(
    val id: Long,
    var name: String,
    val measurements: ArrayList<Measurement>
)
