package hello.ndie.domain.user.repository

import hello.ndie.domain.user.data.model.Code
import hello.ndie.domain.user.data.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository

interface CodeRepository: JpaRepository<Code, String> {
}