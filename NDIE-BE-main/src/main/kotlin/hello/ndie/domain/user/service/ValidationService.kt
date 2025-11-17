package hello.ndie.domain.user.service

/**
 * 사용자 등록 및 인증 관련 유효성 검사를 처리하는 서비스 인터페이스입니다.
 * 비밀번호 유효성 검사와 이메일 인증 코드 검증 기능을 제공합니다.
 */
interface ValidationService {
    /**
     * 비밀번호와 확인용 비밀번호의 유효성을 검사합니다.
     * 
     * 비밀번호는 다음 조건을 충족해야 합니다:
     * - 비밀번호와 확인용 비밀번호가 일치해야 함
     * - 최소 1개의 알파벳, 특수문자, 숫자가 포함되어야 함
     * - 이메일 주소가 비밀번호에 포함되지 않아야 함
     *
     * @param password 사용자가 입력한 비밀번호
     * @param rePassword 확인용 비밀번호
     * @param email 사용자의 이메일 주소
     * @return 비밀번호가 모든 유효성 검사를 통과하면 true, 그렇지 않으면 예외 발생
     * @throws IllegalArgumentException 비밀번호가 유효성 검사를 통과하지 못한 경우
     */
    fun isValidPassword(password: String, rePassword: String, email: String): Boolean

    /**
     * 이메일과 인증 코드가 일치하는지 확인하고, 코드가 만료되지 않았는지 검사합니다.
     *
     * @param email 인증을 확인할 이메일 주소
     * @param code 사용자가 입력한 인증 코드
     * @return 코드가 유효하면 true, 그렇지 않으면 false
     * @throws IllegalArgumentException 해당 이메일의 인증 코드가 존재하지 않는 경우
     * @throws IllegalStateException 인증 코드가 만료된 경우 (10분 초과)
     */
    fun verifyCode(email: String, code: String): Boolean
}
