package hello.ndie.domain.activity.data.model

import hello.ndie.domain.user.data.model.User
import hello.ndie.shared.entity.BaseEntity
import jakarta.persistence.*

@Entity
data class Activity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long?=null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    var title:String,
    var content:String,
    var image:String,
    var views:Long=0
): BaseEntity()
