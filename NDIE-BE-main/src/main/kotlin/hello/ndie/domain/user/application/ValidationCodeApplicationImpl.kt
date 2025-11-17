package hello.ndie.domain.user.application

import hello.ndie.domain.user.data.dto.EmailAndCodeDto
import hello.ndie.domain.user.service.ValidationService
import hello.ndie.shared.exception.SignupValidationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class ValidationCodeApplicationImpl(private val validationService: ValidationService) : ValidationCodeApplication {

    private val log = LoggerFactory.getLogger(ValidationCodeApplicationImpl::class.java)

    override fun isValid(emailAndCodeDto: EmailAndCodeDto) {
        log.info("이메일과 코드 검증 시작: 이메일={}, 코드={}", emailAndCodeDto.email, emailAndCodeDto.code)

        try {
            val isValid = validationService.verifyCode(emailAndCodeDto.email, emailAndCodeDto.code)
            if (!isValid) {
                log.error("잘못된 코드: 이메일={}, 코드={}", emailAndCodeDto.email, emailAndCodeDto.code)
                throw SignupValidationException("잘못된 코드 ${emailAndCodeDto.email} ${emailAndCodeDto.code}", HttpStatus.BAD_REQUEST)
            }
            log.info("올바른 코드입니다: 이메일={}, 코드={}", emailAndCodeDto.email, emailAndCodeDto.code)

        } catch (e: IllegalArgumentException) {
            log.error("인증 코드가 존재하지 않음: 이메일={}, 코드={}, 예외={}", emailAndCodeDto.email, emailAndCodeDto.code, e.message)
            throw SignupValidationException("인증 코드가 존재하지 않습니다: ${emailAndCodeDto.email}", HttpStatus.BAD_REQUEST)
        } catch (e: IllegalStateException) {
            log.error("인증 코드 만료: 이메일={}, 코드={}, 예외={}", emailAndCodeDto.email, emailAndCodeDto.code, e.message)
            throw SignupValidationException("인증 코드가 만료되었습니다: ${emailAndCodeDto.email}", HttpStatus.BAD_REQUEST)
        } catch (e: Exception) {
            log.error("알 수 없는 오류 발생: 이메일={}, 코드={}, 예외={}", emailAndCodeDto.email, emailAndCodeDto.code, e.message)
            throw SignupValidationException("인증 코드 검증 중 오류가 발생했습니다", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}
