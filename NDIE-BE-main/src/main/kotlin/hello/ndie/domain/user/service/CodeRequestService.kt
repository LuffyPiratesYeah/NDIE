package hello.ndie.domain.user.service

import java.time.LocalDate

interface CodeRequestService {
    fun request(userId:Int):String
    fun response(code:String):Int
    fun changeGender(gender: String,id:Long)
    fun changeBirth(birth: LocalDate, id:Long)
    fun changeActivityArea(activityArea: String,id:Long)
}