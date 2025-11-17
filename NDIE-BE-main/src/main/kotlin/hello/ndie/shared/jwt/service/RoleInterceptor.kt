package hello.ndie.shared.jwt.service

import hello.ndie.domain.user.data.dto.CustomOAuth2User
import hello.ndie.domain.user.data.dto.UserDto
import hello.ndie.domain.user.repository.UserRepository
import hello.ndie.shared.config.Admin
import hello.ndie.shared.config.User
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor

@Component
class RoleInterceptor(
    private val jwtUtil: JWTUtil,
    private val userRepository: UserRepository
) : HandlerInterceptor {

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        if (handler !is HandlerMethod) return true

        val hasUser = handler.hasAnnotation(User::class.java)
        val hasAdmin = handler.hasAnnotation(Admin::class.java)

        if (!hasUser && !hasAdmin) return true

        val authHeader = request.getHeader("Authorization")
        val accessToken = authHeader?.takeIf { it.startsWith("Bearer ") }?.substring(7)

        if (accessToken.isNullOrEmpty()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing access token")
            return false
        }

        try {
            if (jwtUtil.isExpired(accessToken)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access token expired")
                return false
            }
        } catch (e: RuntimeException) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid access token")
            return false
        }

        if (jwtUtil.getCategory(accessToken) != "access") {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token category")
            return false
        }

        val role = jwtUtil.getRole(accessToken)
        println("role = ${role}")
        if (hasUser && role !in listOf("ROLE_USER", "ROLE_ADMIN")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "User or Admin role required")
            return false
        }

        if (hasAdmin && role != "ROLE_ADMIN") {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Admin role required")
            return false
        }

        val username:String=jwtUtil.getUsername(accessToken)
        val user=userRepository.findByUsername(username)
        println("username = ${username}")
        println("role = ${role}")
        // 인증 객체 등록
        val userDetails = CustomOAuth2User(
            userDTO = UserDto(
                role = role,
                username = username,
                email = user!!.email,
                id = user.id,
                name = user.name
            )
        )

        val auth = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
        SecurityContextHolder.getContext().authentication = auth

        return true
    }

    // 확장 함수로 애너테이션 확인
    private fun HandlerMethod.hasAnnotation(annotationClass: Class<out Annotation>): Boolean {
        return this.method.getAnnotation(annotationClass) != null ||
                this.beanType.getAnnotation(annotationClass) != null
    }
}
