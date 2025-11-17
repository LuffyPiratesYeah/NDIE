package hello.ndie.domain.document.data.dto

import jakarta.persistence.Column
import java.time.LocalDateTime

data class ShowDetailDocumentDTO(
    val id : Long?=null,
    val userId: Long,
    val username: String,
    val title: String,
    val content: String,
    val type: String,
    val views:Long=0,
    val createdAt: LocalDateTime,
)
