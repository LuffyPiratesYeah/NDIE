package hello.ndie.domain.user.application

import hello.ndie.domain.user.data.dto.CustomOAuth2User
import hello.ndie.domain.user.data.dto.GenderAndBirth
import hello.ndie.domain.user.data.dto.RequestCode
import hello.ndie.domain.user.repository.UserRepository
import hello.ndie.domain.user.service.CodeRequestService
import hello.ndie.shared.exception.CodeRequestException
import hello.ndie.shared.jwt.service.AddRefreshEntity
import hello.ndie.shared.jwt.service.JWTUtil
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class CodeRequestApplicationImpl(
    private val codeRequestService: CodeRequestService,
    private val jwtUtil: JWTUtil,
    private val userRepository: UserRepository,
    private val addRefreshEntity: AddRefreshEntity
) : CodeRequestApplication {

    private val log = LoggerFactory.getLogger(CodeRequestApplicationImpl::class.java)

    override fun response(request: HttpServletRequest, response: HttpServletResponse, requestCode: RequestCode) {
        try {
            val userId: Int = codeRequestService.response(requestCode.code)

            val user = userRepository.findById(userId.toLong())
            if (user.isEmpty) {
                log.warn("User not found for id: $userId")
                throw CodeRequestException("사용자를 찾을 수 없습니다.")
            }

            val username = user.get().username
            val role = user.get().role

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

            log.info("Code response 성공: userId={}, username={}", userId, username)

        } catch (e: CodeRequestException) {
            throw e
        } catch (e: Exception) {
            log.error("Code response 처리 중 오류 발생: code=${requestCode.code}, 메시지=${e.message}", e)
            throw CodeRequestException("코드 응답 처리 중 오류가 발생했습니다.")
        }
    }

    override fun changeBirthAndDate(user: CustomOAuth2User, genderAndBirth: GenderAndBirth) {
        try {
            val userId = user.getId()
                ?: throw CodeRequestException("유저 ID가 존재하지 않습니다.")

            codeRequestService.changeBirth(
                birth = genderAndBirth.birth,
                id = userId
            )
            codeRequestService.changeGender(
                gender = genderAndBirth.gender,
                id = userId
            )
            codeRequestService.changeActivityArea(
                activityArea = genderAndBirth.activityArea,
                id = userId
            )
            log.info("사용자 생년월일 및 성별 변경 성공: userId={}", userId)

        } catch (e: CodeRequestException) {
            throw e
        } catch (e: Exception) {
            log.error("생년월일 및 성별 변경 처리 중 오류 발생: userId=${user.getId()}, 메시지=${e.message}", e)
            throw CodeRequestException("사용자 정보 변경 처리 중 오류가 발생했습니다.")
        }
    }

    private fun createCookie(key: String, value: String): Cookie {
        val cookie = Cookie(key, value)
        cookie.maxAge = 24 * 60 * 60 * 14 // 14일
        cookie.isHttpOnly = true
        return cookie
    }
}
