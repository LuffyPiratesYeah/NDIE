package hello.ndie.domain.document.data.dto

import java.time.LocalDateTime

data class ShowDocumentDTO(
    val id:Long,
    val title:String,
    val username:String,
    val content:String,
    val views:Long,
    val createdAt: LocalDateTime,
)
