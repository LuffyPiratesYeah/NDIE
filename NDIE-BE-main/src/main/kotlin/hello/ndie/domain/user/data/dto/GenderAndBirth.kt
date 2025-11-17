package hello.ndie.domain.user.data.dto

import java.time.LocalDate

data class GenderAndBirth(
    val gender: String,
    val birth: LocalDate,
    val activityArea: String,
)
