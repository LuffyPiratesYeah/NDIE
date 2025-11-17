package hello.ndie.domain.user.service

import hello.ndie.domain.user.data.model.VerificationCode
import hello.ndie.domain.user.repository.VerificationCodeRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime

/**
 * 사용자 등록 관련 유효성 검사를 처리하는 서비스 클래스입니다.
 * 이메일, 비밀번호, 사용자 ID, 사용자 이름, 사용자 클래스, 학년, 번호에 대한 유효성 검사를 제공합니다.
 */
@Service
class ValidationServiceImpl(
    private val verificationCodeRepository: VerificationCodeRepository
) : ValidationService {

    // 비밀번호 유효성 검사를 위한 패턴. 최소 1개의 알파벳, 특수문자, 숫자가 포함되어야 함.
    companion object {
        private val PASSWORD_PATTERN = Regex("^(?=.*[a-zA-Z])(?=.*[~!@#$%^&*+=()_-])(?=.*[0-9]).+$")
    }

    /**
     * 비밀번호와 확인용 비밀번호의 유효성을 검사합니다.
     * 비밀번호는 최소 길이, 최대 길이, 특수 문자, 숫자 및 알파벳 포함 조건을 충족해야 하며, 비밀번호와 ID가 일치하지 않아야 합니다.
     *
     * @param password 비밀번호
     * @param rePassword 확인용 비밀번호
     * @param email 사용자 email
     * @return 비밀번호가 유효하면 true, 그렇지 않으면 false
     */
    override fun isValidPassword(password: String, rePassword: String, email: String): Boolean {
        if (password != rePassword) throw IllegalArgumentException("비밀번호와 확인 비밀번호가 일치하지 않습니다.")
        if (!PASSWORD_PATTERN.matches(password)) throw IllegalArgumentException("비밀번호는 알파벳, 숫자, 특수문자를 포함해야 합니다.")
        if (password.contains(email)) throw IllegalArgumentException("비밀번호에 이메일이 포함될 수 없습니다.")
        return true
    }


    /**
     * 이메일과 인증 코드가 일치하는지 확인하고, 코드가 만료되지 않았는지 검사합니다.
     *
     * @param email 인증을 확인할 이메일 주소
     * @param code 인증 코드
     * @return 코드가 유효하면 true, 그렇지 않으면 false
     */
    override fun verifyCode(email: String, code: String): Boolean {
        val storedCode = verificationCodeRepository.findById(email)
            .orElseThrow { IllegalArgumentException("해당 이메일의 인증 코드가 존재하지 않습니다.") }

        val now = LocalDateTime.now()
        if (storedCode.createdAt.isBefore(now.minusMinutes(10))) {
            throw IllegalStateException("인증 코드가 만료되었습니다.")
        }

        return storedCode.code == code
    }



}