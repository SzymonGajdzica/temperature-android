package pl.polsl.temperature.models

data class UserPost(
    val username: String,
    val password: String,
    val name: String,
    val surname: String,
    val email: String
)