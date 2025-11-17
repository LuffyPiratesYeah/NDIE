package hello.ndie.shared.exception

// 에러 응답 DTO
data class ErrorResponse(
    val status: Int,
    val message: String
)


