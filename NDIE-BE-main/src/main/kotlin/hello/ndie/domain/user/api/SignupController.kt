package hello.ndie.domain.user.api

import com.nimbusds.oauth2.sdk.ErrorResponse
import hello.ndie.domain.user.application.SendEmailApplication
import hello.ndie.domain.user.application.SignupApplication
import hello.ndie.domain.user.data.dto.EmailAndCodeDto
import hello.ndie.domain.user.data.dto.EmailAndCodeResponseValidationDTO
import hello.ndie.domain.user.data.dto.EmailDto
import hello.ndie.domain.user.data.dto.SignupUserDto
import hello.ndie.shared.exception.SignupValidationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid

@RestController
@Tag(name = "Signup-controller : 허동운")
class SignupController(
    private val signupApplication: SignupApplication,
    private val sendEmailApplication: SendEmailApplication
) {

    @Operation(
        summary = "회원가입",
        description = "사용자가 입력한 정보로 회원가입을 진행합니다.",
        responses = [
            ApiResponse(responseCode = "200", description = "회원가입이 완료되었습니다."),
            ApiResponse(responseCode = "400", description = "잘못된 요청 (예: 필수 정보 누락, 유효하지 않은 입력값)",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]),
            ApiResponse(responseCode = "500", description = "서버 오류",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))])
        ]
    )
    @PostMapping("/signup")
    fun signup(
        @Valid @RequestBody signupUserDto: SignupUserDto
    ): ResponseEntity<String> {
        return try {
            signupApplication.signup(signupUserDto)
            ResponseEntity.ok("회원가입 성공")
        } catch (e: SignupValidationException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원가입 실패: ${e.message}")
        }
    }

    @Operation(
        summary = "이메일 인증 메일 전송",
        description = "사용자의 이메일로 인증 메일을 전송합니다.",
        responses = [
            ApiResponse(responseCode = "200", description = "이메일 전송이 완료되었습니다."),
            ApiResponse(responseCode = "400", description = "잘못된 요청 (예: 유효하지 않은 이메일)",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]),
            ApiResponse(responseCode = "500", description = "서버 오류",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))])
        ]
    )
    @PostMapping("/email")
    fun mail(
        @Valid @RequestBody emailDto: EmailDto
    ): ResponseEntity<String> {
        return try {
            sendEmailApplication.sendEmail(emailDto)
            ResponseEntity.ok("이메일을 보냈습니다.")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이메일 전송 실패: ${e.message}")
        }
    }

    @Operation(
        summary = "이메일 인증 코드 검증",
        description = "사용자가 입력한 이메일과 인증 코드의 유효성을 검증합니다.",
        responses = [
            ApiResponse(responseCode = "200", description = "인증 코드 검증 결과",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = EmailAndCodeResponseValidationDTO::class))]),
            ApiResponse(responseCode = "400", description = "잘못된 요청 (예: 유효하지 않은 이메일 또는 코드)",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]),
            ApiResponse(responseCode = "500", description = "서버 오류",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))])
        ]
    )
    @PostMapping("/verify")
    fun verify(
        @Valid @RequestBody
        emailAndCodeDto: EmailAndCodeDto
    ): EmailAndCodeResponseValidationDTO {
        return signupApplication.verify(emailAndCodeDto)
    }
}
