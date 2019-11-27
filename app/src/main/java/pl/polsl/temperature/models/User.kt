package pl.polsl.temperature.models

data class User(
    val id: Long,
    var username: String,
    var name: String,
    var surname: String,
    var email: String,
    val rolesId: Set<Long>,
    val gateways: ArrayList<Gateway>
)