package hello.ndie.domain.user.api

import hello.ndie.domain.user.application.CodeRequestApplication
import hello.ndie.domain.user.application.LoginApplication
import hello.ndie.domain.user.data.dto.*
import hello.ndie.shared.config.Admin
import hello.ndie.shared.config.User
import hello.ndie.shared.exception.SignupValidationException
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@Tag(name = "Signup-controller : 허동운")
class LoginController (
    private val loginApplication: LoginApplication,
    private val codeRequestApplication: CodeRequestApplication,
){
    @PostMapping("/login")
    fun signup(
        @Valid @RequestBody emailAndPassword: EmailAndPassword,
        request: HttpServletRequest,
        response: HttpServletResponse
    ) {
        loginApplication.login(
            emailAndPassword = emailAndPassword,
            request = request,
            response = response
        )
        ResponseEntity.ok("회원가입 성공")
    }


    @PostMapping("/codeLogin")
    fun login(
        @Valid @RequestBody requestCode: RequestCode,
        request: HttpServletRequest,
        response: HttpServletResponse
    ) {
        codeRequestApplication.response(
            request = request,
            response = response,
            requestCode =requestCode,
        )
    }

    @Admin
    @PostMapping("/admin")
    fun admin(@AuthenticationPrincipal userDetails: CustomOAuth2User):CustomOAuth2User{
        return userDetails
    }

    @User
    @PostMapping("/user")
    fun user(@AuthenticationPrincipal userDetails: CustomOAuth2User):CustomOAuth2User{
        return userDetails
    }

    @User
    @PostMapping("/change")
    fun changeBirthAndGender(
        @AuthenticationPrincipal
        userDetails: CustomOAuth2User,
        @RequestBody
        genderAndBirth: GenderAndBirth
    ){
        codeRequestApplication.changeBirthAndDate(
            user = userDetails,
            genderAndBirth = genderAndBirth
        )
    }
}