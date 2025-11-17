package hello.ndie.domain.user.data.dto


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull

/**
 * 사용자의 이메일을 포함하는 DTO 클래스입니다.
 * 주로 이메일 인증 및 관련 기능에서 사용됩니다.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class EmailDto(

    @Schema(description = "사용자의 이메일 주소", example = "ploytechcourse@gmail.com")
    @field:NotNull(message = "이메일은 필수 입력 항목입니다.")
    @field:Email(message = "유효한 이메일 주소를 입력해주세요.")
    val email: String
)
