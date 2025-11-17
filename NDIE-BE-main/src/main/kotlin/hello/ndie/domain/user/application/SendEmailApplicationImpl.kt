package hello.ndie.domain.user.application


import hello.ndie.domain.user.data.dto.EmailDto
import hello.ndie.domain.user.repository.UserRepository
import hello.ndie.domain.user.service.SendEmailService
import hello.ndie.shared.config.EmailCoroutineScope
import hello.ndie.shared.exception.SignupValidationException
import kotlinx.coroutines.launch
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class SendEmailApplicationImpl(
    private val sendEmailService: SendEmailService,
    private val userRepository: UserRepository,
    private val emailCoroutineScope: EmailCoroutineScope
) : SendEmailApplication {

    private val log = org.slf4j.LoggerFactory.getLogger(SendEmailApplicationImpl::class.java)

    override fun sendEmail(emailDto: EmailDto) {
        if (userRepository.existsByEmail(emailDto.email)) {
            log.warn("이메일 전송 요청 거부 - 이미 존재하는 이메일: {}", emailDto.email)
            throw SignupValidationException("이미 있는 이메일입니다.", HttpStatus.CONFLICT)
        }

        emailCoroutineScope.launch {
            try {
                sendEmailService.sendCodeToEmail(emailDto.email)
                log.info("인증 코드 이메일 전송 완료(비동기): 이메일={}", emailDto.email)
            } catch (e: Exception) {
                log.error("비동기 이메일 전송 실패: 이메일={}, 예외={}", emailDto.email, e.message, e)
            }
        }
    }
}
