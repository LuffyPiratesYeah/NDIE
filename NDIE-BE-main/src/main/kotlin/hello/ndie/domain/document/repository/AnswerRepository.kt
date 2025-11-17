package hello.ndie.domain.document.repository

import hello.ndie.domain.document.data.model.Answer
import org.springframework.data.jpa.repository.JpaRepository

interface AnswerRepository : JpaRepository<Answer, Long> {
    fun findAllByDocumentId(documentId: Long): List<Answer>
}