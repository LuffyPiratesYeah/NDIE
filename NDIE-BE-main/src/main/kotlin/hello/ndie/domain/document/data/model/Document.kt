package hello.ndie.domain.document.data.model

import hello.ndie.domain.user.data.model.User
import hello.ndie.shared.entity.BaseEntity
import jakarta.persistence.*

@Entity
data class Document(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long?=null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(nullable = false, columnDefinition = "TEXT")
    var title: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    var content: String,

    @Column(length = 255)
    val type: String,

    var views:Long=0
): BaseEntity()