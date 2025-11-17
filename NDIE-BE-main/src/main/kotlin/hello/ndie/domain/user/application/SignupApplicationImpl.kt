package hello.ndie.domain.user.application

import hello.ndie.domain.user.data.dto.EmailAndCodeDto
import hello.ndie.domain.user.data.dto.EmailAndCodeResponseValidationDTO
import hello.ndie.domain.user.data.dto.EmailDto
import hello.ndie.domain.user.data.dto.SignupUserDto
import hello.ndie.domain.user.data.model.User
import hello.ndie.domain.user.repository.UserRepository
import hello.ndie.domain.user.service.ValidationService
import hello.ndie.shared.exception.SignupValidationException
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

/**
 * SignupApplication 인터페이스의 구현체로, 회원가입 및 이메일 인증 코드 검증 기능을 제공합니다.
 * 
 * 이 클래스는 다음과 같은 주요 기능을 담당합니다:
 * - 사용자 회원가입 처리 (비밀번호 검증, 이메일 인증 코드 검증, 이메일 중복 검사)
 * - 이메일 인증 코드 유효성 검증
 * 
 * 모든 검증 과정에서 발생하는 예외는 SignupValidationException으로 변환하여 처리합니다.
 */
@Service
class SignupApplicationImpl(
    private val validationService: ValidationService,
    private val userRepository: UserRepository,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder
) : SignupApplication {

    private val log = LoggerFactory.getLogger(SignupApplicationImpl::class.java)

    /**
     * 사용자 회원가입을 처리합니다.
     * 
     * 다음과 같은 검증 단계를 거칩니다:
     * 1. 비밀번호 유효성 검사 (비밀번호와 확인용 비밀번호 일치 여부, 복잡성 요구사항 등)
     * 2. 이메일 인증 코드 검증 (사용자가 제출한 코드와 저장된 코드 비교)
     * 3. 이메일 중복 검사 (이미 등록된 이메일인지 확인)
     * 
     * 모든 검증을 통과하면 사용자 정보를 저장하고, 비밀번호는 암호화하여 저장합니다.
     * 검증 과정에서 오류가 발생하면 SignupValidationException을 발생시킵니다.
     *
     * @param signupUserDto 회원가입에 필요한 사용자 정보
     * @throws SignupValidationException 회원가입 검증 과정에서 오류가 발생한 경우
     */
    override fun signup(signupUserDto: SignupUserDto) {
        try {
            // 비밀번호 유효성 검사
            if (!validationService.isValidPassword(signupUserDto.password, signupUserDto.rePassword, signupUserDto.email)) {
                throw SignupValidationException("비밀번호 유효성 검사 실패: ${signupUserDto.email}", HttpStatus.BAD_REQUEST)
            }

            // 이메일 인증 코드 유효성 검사
            if (!validationService.verifyCode(signupUserDto.email, signupUserDto.code)) {
                throw SignupValidationException("이메일 인증 코드 검증 실패: ${signupUserDto.email} ${signupUserDto.code}", HttpStatus.BAD_REQUEST)
            }

            // 이메일 중복 검사
            if (userRepository.existsByEmail(signupUserDto.email)) {
                throw SignupValidationException("이메일 중복됨: ${signupUserDto.email}", HttpStatus.BAD_REQUEST)
            }

            // 사용자 정보 저장
            val savedUser = userRepository.save(
                User(
                    email = signupUserDto.email,
                    name = signupUserDto.name,
                    role = "ROLE_USER", // 기본 역할은 사용자
                    password = bCryptPasswordEncoder.encode(signupUserDto.password),
                    username = "email " + signupUserDto.name,
                    gender = signupUserDto.gender,
                    birthDate = signupUserDto.birthDate,
                    activeArea = signupUserDto.activityArea
                )
            )
            log.info("회원가입 성공: 이메일={}", signupUserDto.email)

        } catch (e: SignupValidationException) {
            log.error("회원가입 검증 실패: 이메일={}, 메시지={}", signupUserDto.email, e.message)
            throw e
        } catch (e: Exception) {
            log.error("회원가입 처리 중 오류 발생: 이메일={}, 예외={}", signupUserDto.email, e.message, e)
            throw SignupValidationException("회원가입 처리 중 알 수 없는 오류가 발생했습니다", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    /**
     * 이메일 인증 코드의 유효성을 검증합니다.
     * 
     * 사용자가 제출한 이메일과 인증 코드를 검증하고 결과를 반환합니다.
     * 이 메서드는 ValidationService를 통해 실제 검증을 수행하고 결과를 DTO로 변환합니다.
     *
     * @param emailAndCodeDto 이메일과 인증 코드 정보
     * @return 인증 코드 검증 결과를 담은 DTO 객체
     */
    override fun verify(emailAndCodeDto: EmailAndCodeDto): EmailAndCodeResponseValidationDTO {
        val verifyCode = validationService.verifyCode(emailAndCodeDto.email, emailAndCodeDto.code)
        return EmailAndCodeResponseValidationDTO(
            isValid = verifyCode
        )
    }
}
