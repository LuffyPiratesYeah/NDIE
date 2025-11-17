package hello.ndie.domain.user.service

import hello.ndie.domain.user.data.model.User
import hello.ndie.domain.user.repository.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class LoginServiceImpl(
    private val userRepository: UserRepository,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder
) : LoginService {

    override fun login(email: String, password: String): Boolean {
        val user = userRepository.findByEmail(email)
            ?: throw IllegalArgumentException("해당 이메일을 가진 사용자가 존재하지 않습니다.")

        val encodedPassword = user.password
            ?: throw IllegalStateException("사용자 비밀번호가 존재하지 않습니다.")

        if (!bCryptPasswordEncoder.matches(password, encodedPassword)) {
            throw IllegalArgumentException("비밀번호가 올바르지 않습니다.")
        }

        return true
    }
}
