package hello.ndie.shared.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import java.util.Properties

/**
 * 이메일 설정을 위한 구성 클래스입니다.
 * SMTP 서버 설정을 Spring Mail Sender에 전달하여 이메일 전송 기능을 설정합니다.
 */
@Configuration
class EmailConfig {

    @Value("\${spring.mail.host}")
    lateinit var host: String

    @Value("\${spring.mail.port}")
    var port: Int = 0

    @Value("\${spring.mail.username}")
    lateinit var username: String

    @Value("\${spring.mail.password}")
    lateinit var password: String

    @Value("\${spring.mail.properties.mail.smtp.auth}")
    var auth: Boolean = false

    @Value("\${spring.mail.properties.mail.smtp.starttls.enable}")
    var starttlsEnable: Boolean = false

    @Value("\${spring.mail.properties.mail.smtp.timeout}")
    var timeout: Int = 0

    /**
     * JavaMailSender 빈을 생성하고 반환합니다.
     * 이메일 서버 설정을 기반으로 이메일 전송을 위한 JavaMailSender를 구성합니다.
     *
     * @return JavaMailSender 설정된 이메일 전송 객체
     */
    @Bean
    fun javaMailSender(): JavaMailSender {
        val mailSender = JavaMailSenderImpl()
        mailSender.host = host
        mailSender.port = port
        mailSender.username = username
        mailSender.password = password
        mailSender.defaultEncoding = "UTF-8"
        mailSender.javaMailProperties = getMailProperties()

        return mailSender
    }

    /**
     * 이메일 전송에 필요한 추가 속성들을 반환합니다.
     *
     * @return 설정된 메일 속성(Properties 객체)
     */
    private fun getMailProperties(): Properties {
        val properties = Properties()
        properties["mail.smtp.auth"] = auth
        properties["mail.smtp.starttls.enable"] = starttlsEnable
        properties["mail.smtp.timeout"] = timeout
        return properties
    }
}
