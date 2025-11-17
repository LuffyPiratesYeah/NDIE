package hello.ndie.domain.user.application

import hello.ndie.domain.user.data.dto.CustomOAuth2User
import hello.ndie.domain.user.data.dto.GenderAndBirth
import hello.ndie.domain.user.data.dto.RequestCode
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

interface CodeRequestApplication {
    fun response(request: HttpServletRequest, response: HttpServletResponse, requestCode: RequestCode)
    fun changeBirthAndDate(user:CustomOAuth2User,genderAndBirth: GenderAndBirth)
}