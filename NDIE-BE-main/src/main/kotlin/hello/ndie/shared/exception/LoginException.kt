package hello.ndie.shared.exception

import org.springframework.http.HttpStatus

class LoginException(
    override val message: String,
    val status: HttpStatus
) : RuntimeException(message)
