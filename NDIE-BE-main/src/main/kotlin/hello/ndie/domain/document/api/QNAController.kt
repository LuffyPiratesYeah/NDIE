package hello.ndie.domain.document.api

import hello.ndie.domain.document.data.model.Document
import hello.ndie.domain.document.application.QnAApplication
import hello.ndie.domain.document.data.dto.*
import hello.ndie.domain.user.data.dto.CustomOAuth2User
import hello.ndie.shared.config.Admin
import hello.ndie.shared.config.User
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import javax.xml.stream.events.Comment

@RestController
@RequestMapping("/QNA")
class QNAController(
    private val qnAApplication: QnAApplication
) {
    @GetMapping
    fun getAnnouncement():List<ShowDocumentDTO>{
        return qnAApplication.showDocuments()
    }

    @GetMapping("/{announcementID}")
    fun getAnnouncementOne(
        @PathVariable("announcementID") announcementId: Long
    ): ShowDetailDocumentDTO {
        return qnAApplication.showDetailDocuments(
            titleIDDTO = TitleIDDTO(announcementId)
        )
    }

    @User
    @PostMapping
    fun uploadAnnouncement(
        @AuthenticationPrincipal
        user: CustomOAuth2User,
        @RequestBody
        uploadDocumentDTO: UploadDocumentDTO
    ){
        qnAApplication.upload(
            uploadDocumentDTO = uploadDocumentDTO,
            user = user
        )
    }

    @User
    @DeleteMapping
    fun deleteAnnouncement(
        @RequestBody
        titleIDDTO: TitleIDDTO
    ){
        qnAApplication.delete(
            titleIDDTO = titleIDDTO
        )
    }

    @User
    @PatchMapping
    fun updateAnnouncement(
        @RequestBody
        updateDocumentDTO: UpdateDocumentDTO
    ){
        qnAApplication.update(
            updatedDocument = updateDocumentDTO
        )
    }


    @GetMapping("/comment/{commentID}")
    fun getComments(
        @PathVariable("commentID") commentID: Long
    ):AnswerViewsDTO{
        return qnAApplication.answerViews(commentID)
    }

    @Admin
    @PostMapping("/comment")
    fun insertComment(
        @RequestBody
        answerInputDTO: AnswerInputDTO,
        @AuthenticationPrincipal
        user: CustomOAuth2User
    ){
        qnAApplication.answerInput(
            answerInputDTO = answerInputDTO,
            user = user
        )
    }


    @PostMapping("/prev-next")
    fun getPrevNext(
        @RequestBody
        frontBackDataRequestDTO: FrontBackDataRequestDTO
    ): FrontBackDTO{
        return qnAApplication.frontBackForTypeQNA(frontBackDataRequestDTO)
    }
}
