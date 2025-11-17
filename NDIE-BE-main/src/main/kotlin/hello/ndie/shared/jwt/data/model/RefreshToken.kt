package hello.ndie.shared.jwt.data.model

import java.util.Date
import jakarta.persistence.*

@Entity
@Table(name = "refresh_token")
class RefreshToken(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, length = 100)
    var username: String,

    @Column(nullable = false)
    var token: String,

    @Column(name = "expires_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    var expiresAt: Date,

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    var createdAt: Date

)