package hello.ndie.domain.activity.data.dto

import hello.ndie.domain.user.data.model.User
import jakarta.persistence.*
import java.time.LocalDateTime

data class ShowActivityDetailDTO(
    val id : Long?=null,
    val userId: Long,
    val username:String,
    val title:String,
    val content:String,
    val image:String,
    val createdAt: LocalDateTime,
)
