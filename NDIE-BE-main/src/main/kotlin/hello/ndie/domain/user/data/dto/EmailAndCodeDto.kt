package hello.ndie.domain.user.data.dto


import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull

/**
 * 이메일과 인증 코드를 담고 있는 DTO 클래스입니다.
 */
data class EmailAndCodeDto(
    @Schema(description = "사용자의 이메일", example = "ploytechcourse@gmail.com")
    @field:Email(message = "유효한 이메일 주소를 입력하세요.")
    @field:NotNull
    val email: String,

    @Schema(description = "사용자에게 전송된 인증 코드", example = "C1ki1")
    @field:NotNull
    val code: String
)
