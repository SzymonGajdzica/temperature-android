package pl.polsl.temperature.models

data class RegisterUser(
    val username: String,
    val password: String,
    val name: String,
    val surname: String,
    val email: String
)