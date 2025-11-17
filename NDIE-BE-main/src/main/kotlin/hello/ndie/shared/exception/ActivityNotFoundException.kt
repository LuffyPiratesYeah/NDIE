package hello.ndie.shared.exception

class ActivityNotFoundException(
    override val message: String = "해당 활동을 찾을 수 없습니다."
) : RuntimeException(message)