package hello.ndie.domain.user.service

import hello.ndie.domain.user.data.dto.CustomOAuth2User
import hello.ndie.shared.jwt.service.JWTUtil
import jakarta.servlet.ServletException
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class CustomSuccessHandler(
    private val jwtUtil: JWTUtil,
    private val codeRequestService: CodeRequestService
) : SimpleUrlAuthenticationSuccessHandler() {

    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {

        // OAuth2User
        val customUserDetails = authentication.principal as CustomOAuth2User
        val userId:Int=customUserDetails.getId().toString().toInt()
        val code:String=codeRequestService.request(userId)
        response.sendRedirect("https://ndie-fe-985895714915.asia-northeast1.run.app/login/success?code=$code")
    }

    private fun createCookie(key: String, value: String): Cookie {
        val cookie = Cookie(key, value)
        cookie.maxAge = 60 * 60 * 60
        cookie.path = "/"
        cookie.isHttpOnly = true
        return cookie
    }

    private fun UncreateCookie(key: String, value: String): Cookie {
        val cookie = Cookie(key, value)
        cookie.maxAge = 60 * 60 * 60
        cookie.path = "/"
        return cookie
    }
}
