package hello.ndie.shared.exception

import org.springframework.http.HttpStatus

class SignupValidationException(
    override val message: String,
    val status: HttpStatus
) : RuntimeException(message)

