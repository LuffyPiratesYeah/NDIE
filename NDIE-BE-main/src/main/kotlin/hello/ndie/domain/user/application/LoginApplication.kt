package hello.ndie.domain.user.application

import hello.ndie.domain.user.data.dto.EmailAndPassword
import hello.ndie.domain.user.data.dto.EmailDto
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

interface LoginApplication {
    fun login(emailAndPassword: EmailAndPassword, request: HttpServletRequest, response: HttpServletResponse)
}