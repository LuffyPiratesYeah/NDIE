package hello.ndie.shared.exception

class CodeRequestException (
    override val message: String = "코드 응답 처리 중 오류가 발생했습니다."
) : RuntimeException(message)