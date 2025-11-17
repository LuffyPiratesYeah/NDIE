package hello.ndie.domain.document.repository

import hello.ndie.domain.document.data.model.Document
import org.springframework.data.jpa.repository.JpaRepository

interface DocumentRepository: JpaRepository<Document, Long> {
    fun findAllByType(type: String): List<Document>


    fun findFirstByTypeAndIdLessThanOrderByIdDesc(type: String, id: Long): Document?
    fun findFirstByTypeAndIdGreaterThanOrderByIdAsc(type: String, id: Long): Document?
    fun findAllByTypeOrderByCreatedAtDesc(type: String): List<Document>

}