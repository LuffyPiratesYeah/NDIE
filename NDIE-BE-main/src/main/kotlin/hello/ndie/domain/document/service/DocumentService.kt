package hello.ndie.domain.document.service

import hello.ndie.domain.document.data.dto.FrontBackDTO
import hello.ndie.domain.document.data.dto.ShowDocumentDTO
import hello.ndie.domain.document.data.dto.ShowDetailDocumentDTO
import hello.ndie.domain.document.data.model.Answer
import hello.ndie.domain.user.data.model.User

interface DocumentService {
    fun upload(title:String,content:String,user :User,type:String)
    fun update(title:String,content:String,titleId:Long)
    fun delete( titleId: Long)
    fun showDocuments(type:String):List<ShowDocumentDTO>
    fun showDetails(titleId:Long): ShowDetailDocumentDTO
    fun upViews(titleId:Long)

    fun commentViews(titleId:Long):List<Answer>?
    fun commentInputs(userId:Long,titleId:Long,content:String)

    fun frontBackForType(type:String, titleId:Long): FrontBackDTO
}
