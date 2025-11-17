package hello.ndie.domain.user.data.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import hello.ndie.shared.entity.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "users")
@JsonIgnoreProperties(value = ["hibernateLazyInitializer", "handler"])
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long?=null,

    val username: String,

    val email : String,

    val name : String,

    val role : String,

    val password:String,

    var gender: String? = null,

    var birthDate: LocalDate? = null,

    var activeArea: String? = null
): BaseEntity()
