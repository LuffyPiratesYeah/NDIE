package hello.ndie.domain.user.data.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDate

/**
 * 사용자의 회원가입 정보를 담고 있는 DTO 클래스입니다.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class SignupUserDto(

    @Schema(description = "사용자의 이름", example = "허온")
    @field:NotNull
    @field:Size(min = 2, max = 30)
    val name: String,

    @Schema(description = "사용자의 이메일 주소", example = "ploytechcourse@gmail.com")
    @field:NotNull
    @field:Email
    @field:Size(max = 320)
    val email: String,

    @Schema(description = "이메일 인증 코드", example = "C1kdee")
    @field:NotNull
    val code: String,

    @Schema(description = "사용자의 비밀번호", example = "ploytechcourse2025!!")
    @field:NotNull
    @field:Size(min = 10, max = 30)
    val password: String,

    @Schema(description = "사용자가 입력한 비밀번호 확인", example = "ploytechcourse2025!!")
    @field:NotNull
    val rePassword: String,

    val gender: String? = null,

    val birthDate: LocalDate? = null,

    val activityArea: String? = null,
)

