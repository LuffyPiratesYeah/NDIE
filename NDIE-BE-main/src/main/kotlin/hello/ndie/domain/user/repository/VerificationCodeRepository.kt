package hello.ndie.domain.user.repository

import hello.ndie.domain.user.data.model.User
import hello.ndie.domain.user.data.model.VerificationCode
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface VerificationCodeRepository : JpaRepository<VerificationCode, String> {
}