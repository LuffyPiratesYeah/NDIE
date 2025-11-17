package hello.ndie.domain.user.data.dto

data class UserDto(
    val role: String,
    val name: String,
    val username: String,
    val email:String,
    val id: Long?
)
