package hello.ndie.domain.user.service

import hello.ndie.domain.user.data.dto.EmailDto

interface LoginService {
    fun login(email: String, password: String): Boolean
}