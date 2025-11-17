package hello.ndie.domain.user.service

import hello.ndie.domain.user.data.model.VerificationCode
import jakarta.mail.MessagingException

interface SendEmailService {
    fun sendCodeToEmail(email: String)
    fun createVerificationCode(email: String): VerificationCode
    @Throws(MessagingException::class)
    fun sendEmail(toEmail: String, title: String, content: String)
}