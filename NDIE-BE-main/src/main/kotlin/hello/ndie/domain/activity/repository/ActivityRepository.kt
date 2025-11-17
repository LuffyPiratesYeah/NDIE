package hello.ndie.domain.activity.repository

import hello.ndie.domain.activity.data.model.Activity
import org.springframework.data.jpa.repository.JpaRepository


interface ActivityRepository: JpaRepository<Activity, Long> {
    fun findTopByIdLessThanOrderByIdDesc(id: Long): Activity?
    fun findTopByIdGreaterThanOrderByIdAsc(id: Long): Activity?
    fun findAllByOrderByCreatedAtDesc(): List<Activity>

}