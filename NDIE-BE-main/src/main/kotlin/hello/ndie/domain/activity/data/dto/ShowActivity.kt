package hello.ndie.domain.activity.data.dto

import java.time.LocalDateTime

data class ShowActivity(
    val id : Long?=null,
    val username:String,
    val title:String,
    val createdAt: LocalDateTime,
    val views:Long=0,
)
