package hello.ndie.domain.user.repository

import hello.ndie.domain.user.data.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User?, Long?> {
    fun existsByEmail(email: String?): Boolean
    fun findByEmail(email: String?): User?
    fun findByUsername(username: String?): User?
}