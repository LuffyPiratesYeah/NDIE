package hello.ndie.domain.user.data.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.io.Serializable
import java.time.LocalDateTime


@Entity
@Table(name = "verification_code")
data class VerificationCode(
    @Id
    val email: String,

    @Column(nullable = false)
    val code: String,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()
)
