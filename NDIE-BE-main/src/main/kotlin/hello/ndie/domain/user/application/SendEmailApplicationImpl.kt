package hello.ndie.domain.user.application


import hello.ndie.domain.user.data.dto.EmailDto
import hello.ndie.domain.user.repository.UserRepository
import hello.ndie.domain.user.service.SendEmailService
import hello.ndie.shared.exception.SignupValidationException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class SendEmailApplicationImpl(
    private val sendEmailService: SendEmailService,
    private val userRepository: UserRepository
) : SendEmailApplication {

    private val log = org.slf4j.LoggerFactory.getLogger(SendEmailApplicationImpl::class.java)

    override fun sendEmail(emailDto: EmailDto) {
        try {
            if (userRepository.existsByEmail(emailDto.email)) {
                throw SignupValidationException("이미 있는 이메일입니다.", HttpStatus.CONFLICT)
            }

            sendEmailService.sendCodeToEmail(emailDto.email)
            log.info("인증 코드 이메일 전송 성공: 이메일={}", emailDto.email)

        } catch (e: SignupValidationException) {
            log.warn("이메일 전송 실패 - 이메일 중복: 이메일={}", emailDto.email)
            throw e
        } catch (e: Exception) {
            log.error("이메일 전송 중 오류 발생: 이메일={}, 예외={}", emailDto.email, e.message, e)
            throw SignupValidationException("이메일 전송 중 알 수 없는 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}
