package hello.ndie.domain.user.application

import hello.ndie.domain.user.data.dto.EmailAndPassword
import hello.ndie.domain.user.repository.UserRepository
import hello.ndie.domain.user.service.LoginService
import hello.ndie.shared.exception.LoginException
import hello.ndie.shared.jwt.service.AddRefreshEntity
import hello.ndie.shared.jwt.service.JWTUtil
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class LoginApplicationImpl(
    private val loginService: LoginService,
    private val jwtUtil: JWTUtil,
    private val userRepository: UserRepository,
    private val addRefreshEntity: AddRefreshEntity
) : LoginApplication {

    private val log = org.slf4j.LoggerFactory.getLogger(LoginApplicationImpl::class.java)

    override fun login(emailAndPassword: EmailAndPassword, request: HttpServletRequest, response: HttpServletResponse) {
        try {
            if (!loginService.login(emailAndPassword.email, emailAndPassword.password)) {
                log.warn("로그인 실패 - 잘못된 이메일 또는 비밀번호: 이메일={}", emailAndPassword.email)
                throw LoginException("login or password is wrong", HttpStatus.UNAUTHORIZED)
            }

            val findByEmail = userRepository.findByEmail(emailAndPassword.email)
                ?: throw LoginException("사용자를 찾을 수 없습니다: 이메일=${emailAndPassword.email}", HttpStatus.UNAUTHORIZED)

            val username: String = findByEmail.username
            val role: String = findByEmail.role

            val access: String = jwtUtil.createJwt(
                category = "access",
                username = username,
                role = role,
                expiredMs = 86400000L
            )
            val refresh: String = jwtUtil.createJwt(
                category = "refresh",
                username = username,
                role = role,
                expiredMs = 1209600000L
            )
            addRefreshEntity.addRefreshEntity(
                username = username,
                refresh = refresh,
                expiredMs = 1209600000L
            )

            response.setHeader("Authorization", access)
            response.addCookie(createCookie("refresh", refresh))
            response.status = HttpStatus.OK.value()

            log.info("로그인 성공: 이메일={}", emailAndPassword.email)

        } catch (e: LoginException) {
            throw e
        } catch (e: Exception) {
            log.error("로그인 처리 중 오류 발생: 이메일={}, 오류 메시지={}", emailAndPassword.email, e.message, e)
            throw LoginException("로그인 처리 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    private fun createCookie(key: String, value: String): Cookie {
        val cookie = Cookie(key, value)
        cookie.maxAge = 24 * 60 * 60 * 14
        cookie.isHttpOnly = true
        return cookie
    }
}
