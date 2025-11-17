package hello.ndie.shared.exception

class EmailSendingException(
    override val message: String = "이메일 전송에 실패했습니다."
) : RuntimeException(message)