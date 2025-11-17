package hello.ndie.shared.exception

class DocumentNotFoundException(
    override val message: String = "해당 문서를 찾을 수 없습니다."
) : RuntimeException(message)