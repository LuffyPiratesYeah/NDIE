package hello.ndie.shared.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest

@ControllerAdvice
class GlobalExceptionHandler {

    // 데이터 없음 예외: 404 Not Found
    @ExceptionHandler(ActivityNotFoundException::class)
    fun handleActivityNotFound(ex: ActivityNotFoundException, request: WebRequest): ResponseEntity<ErrorResponse> {
        return buildResponseEntity(HttpStatus.NOT_FOUND, ex.message)
    }

    @ExceptionHandler(DocumentNotFoundException::class)
    fun handleDocumentNotFound(ex: DocumentNotFoundException, request: WebRequest): ResponseEntity<ErrorResponse> {
        return buildResponseEntity(HttpStatus.NOT_FOUND, ex.message)
    }

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFound(ex: UserNotFoundException, request: WebRequest): ResponseEntity<ErrorResponse> {
        return buildResponseEntity(HttpStatus.NOT_FOUND, ex.message ?: "사용자를 찾을 수 없습니다.")
    }

    // 이메일 전송 실패: 500 Internal Server Error
    @ExceptionHandler(EmailSendingException::class)
    fun handleEmailSending(ex: EmailSendingException, request: WebRequest): ResponseEntity<ErrorResponse> {
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex.message)
    }

    // 이미지 업로드 실패: 500 Internal Server Error
    @ExceptionHandler(ImageUploadException::class)
    fun handleImageUpload(ex: ImageUploadException, request: WebRequest): ResponseEntity<ErrorResponse> {
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex.message)
    }

    // 로그인 실패: 401 Unauthorized
    @ExceptionHandler(LoginException::class)
    fun handleLoginException(ex: LoginException, request: WebRequest): ResponseEntity<ErrorResponse> {
        return buildResponseEntity(ex.status, ex.message)
    }

    // 회원가입 검증 실패: 409 Conflict 등
    @ExceptionHandler(SignupValidationException::class)
    fun handleSignupValidation(ex: SignupValidationException, request: WebRequest): ResponseEntity<ErrorResponse> {
        return buildResponseEntity(ex.status, ex.message)
    }

    // 코드 요청 처리 오류: 500 Internal Server Error (필요시 400 등으로 변경 가능)
    @ExceptionHandler(CodeRequestException::class)
    fun handleCodeRequestException(ex: CodeRequestException, request: WebRequest): ResponseEntity<ErrorResponse> {
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex.message)
    }

    // 기타 예외 처리: 500 Internal Server Error
    @ExceptionHandler(Exception::class)
    fun handleAllOtherExceptions(ex: Exception, request: WebRequest): ResponseEntity<ErrorResponse> {
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 서버 오류가 발생했습니다.")
    }

    private fun buildResponseEntity(status: HttpStatus, message: String?): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(status.value(), message ?: "오류가 발생했습니다.")
        return ResponseEntity(errorResponse, status)
    }
}
