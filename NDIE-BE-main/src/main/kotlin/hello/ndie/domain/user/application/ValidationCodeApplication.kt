package hello.ndie.domain.user.application

import hello.ndie.domain.user.data.dto.EmailAndCodeDto

interface ValidationCodeApplication {
    fun isValid(emailAndCodeDto: EmailAndCodeDto)
}