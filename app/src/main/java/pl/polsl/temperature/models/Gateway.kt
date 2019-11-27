package pl.polsl.temperature.models

data class Gateway(
    val id: Long,
    var name: String,
    val stations: ArrayList<StationReduced>
)