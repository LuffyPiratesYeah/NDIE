package hello.ndie.domain.user.application

import hello.ndie.domain.user.data.dto.EmailAndCodeDto
import hello.ndie.domain.user.data.dto.EmailAndCodeResponseValidationDTO
import hello.ndie.domain.user.data.dto.EmailDto
import hello.ndie.domain.user.data.dto.SignupUserDto


/**
 * 사용자 회원가입을 처리하는 애플리케이션 서비스 인터페이스입니다.
 *
 * 이 인터페이스는 회원가입 관련된 비즈니스 로직을 정의하고,
 * 실제 회원가입 처리를 구현하는 클래스에서 이를 구현합니다.
 *
 * 주요 기능으로는 회원가입 처리와 이메일 인증 코드 검증이 있습니다.
 */
interface SignupApplication {
    /**
     * 주어진 사용자 정보를 기반으로 회원가입을 처리합니다.
     *
     * 회원가입 처리 시 사용자 정보를 검증하고, 유효하다면 회원가입을 완료합니다.
     * 검증 과정에서는 비밀번호 유효성, 이메일 인증 코드, 이메일 중복 여부 등을 확인합니다.
     *
     * @param signupUserDto 회원가입에 필요한 사용자 정보가 담긴 DTO 객체
     * @throws SignupValidationException 회원가입 검증 과정에서 오류가 발생한 경우
     */
    fun signup(signupUserDto: SignupUserDto)

    /**
     * 이메일과 인증 코드의 유효성을 검증합니다.
     *
     * 사용자가 입력한 이메일과 인증 코드가 유효한지 확인하고 결과를 반환합니다.
     * 이 기능은 회원가입 전 이메일 인증 단계에서 사용됩니다.
     *
     * @param emailAndCodeDto 이메일과 인증 코드 정보가 담긴 DTO 객체
     * @return 인증 코드 검증 결과를 담은 DTO 객체
     */
    fun verify(emailAndCodeDto: EmailAndCodeDto): EmailAndCodeResponseValidationDTO
}
