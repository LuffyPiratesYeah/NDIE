package hello.ndie.domain.user.service


import hello.ndie.domain.user.data.model.VerificationCode
import hello.ndie.domain.user.repository.VerificationCodeRepository
import hello.ndie.shared.exception.EmailSendingException
import jakarta.mail.MessagingException
import jakarta.mail.internet.MimeMessage
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import java.util.concurrent.ThreadLocalRandom

@Service
class SendEmailServiceImpl(
    private val emailSender: JavaMailSender,
    private val verificationCodeRepository: VerificationCodeRepository,
    @Value("\${ndie.mail.disable-send:false}") private val disableMailSend: Boolean
) : SendEmailService {

    private val logger = LoggerFactory.getLogger(SendEmailServiceImpl::class.java)

    /**
     * 주어진 이메일 주소로 인증 코드를 전송합니다.
     *
     * @param email 인증 코드를 받을 이메일 주소
     * @throws RuntimeException 이메일 전송 실패 시 예외 발생
     */
    override fun sendCodeToEmail(email: String) {
        val createdCode = createVerificationCode(email)
        val title = "NDIE 인증번호"
        val content = """
            <html>
                <body>
                    <div style='background-color: #CAD5FF; display: flex; flex-direction: column; align-items: center; padding: 8px 10px;'>
                        <div>
                            <p style='margin: 0; padding-bottom: 8px;'>인증코드</p>
                            <div style='background-color: #fff; border-radius: 8px; padding: 8px 10px;'>
                                <p style='font-size: 24px; margin: 0;'>${createdCode.code}</p>
                            </div>
                            <p>해당 코드를 홈페이지에 입력하세요.</p>
                        </div>
                        <footer style='color: grey; font-size: small;'>
                            ※ 본 메일은 자동응답 메일이므로 본 메일에 회신하지 마시기 바랍니다.
                        </footer>
                    </div>
                </body>
            </html>
        """

        try {
            sendEmail(email, title, content)
        } catch (e: MessagingException) {
            e.printStackTrace()
            throw EmailSendingException("이메일 전송에 실패했습니다.")
        }
    }

    /**
     * 주어진 이메일에 대한 인증 코드를 생성하고 저장합니다.
     * 생성된 인증 코드는 10분 후에 만료됩니다.
     *
     * @param email 인증 코드를 생성할 이메일 주소
     * @return 생성된 인증 코드 객체
     */
    override fun createVerificationCode(email: String): VerificationCode {
        val randomCode = generateRandomCode(6)
        val code = VerificationCode(email = email, code = randomCode)
        return verificationCodeRepository.save(code)
    }

    /**
     * 주어진 이메일 주소로 이메일을 전송합니다.
     *
     * @param toEmail 수신자 이메일 주소
     * @param title 이메일 제목
     * @param content 이메일 내용 (HTML 형식)
     * @throws MessagingException 이메일 전송 중 예외 발생 시
     */
    override fun sendEmail(toEmail: String, title: String, content: String) {
        if (disableMailSend) {
            logger.info("메일 전송이 비활성화되어 이메일({})을 전송하지 않고 건너뜁니다.", toEmail)
            return
        }

        val message: MimeMessage = emailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true)
        helper.setTo(toEmail)
        helper.setSubject(title)
        helper.setText(content, true)
        helper.setReplyTo("ploytechcourse@gmail.com")
        emailSender.send(message)
    }

    /**
     * 주어진 길이로 랜덤한 인증 코드를 생성합니다.
     *
     * @param length 생성할 인증 코드의 길이
     * @return 생성된 랜덤 인증 코드
     */
    fun generateRandomCode(length: Int): String {
        val characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        val sb = StringBuilder()
        val random = ThreadLocalRandom.current()

        repeat(length) {
            val index = random.nextInt(characters.length)
            sb.append(characters[index])
        }

        return sb.toString()
    }
}
