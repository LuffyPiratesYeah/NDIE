package hello.ndie.domain.document.api

import hello.ndie.domain.document.data.model.Document
import hello.ndie.domain.document.application.AnnouncementApplication
import hello.ndie.domain.document.data.dto.*
import hello.ndie.domain.user.data.dto.CustomOAuth2User
import hello.ndie.shared.config.Admin
import hello.ndie.shared.config.User
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/announcement")
class AnnouncementController (
    private val announcementApplication: AnnouncementApplication
){
    @GetMapping
    fun getAnnouncement():List<ShowDocumentDTO>{
        return announcementApplication.showDocuments()
    }

    @GetMapping("/{announcementID}")
    fun getAnnouncementOne(
        @PathVariable("announcementID") announcementId: Long
    ): ShowDetailDocumentDTO {
        return announcementApplication.showDetailDocuments(
            titleIDDTO = TitleIDDTO(announcementId)
        )
    }

    @Admin
    @PostMapping
    fun uploadAnnouncement(
        @AuthenticationPrincipal
        user: CustomOAuth2User,
        @RequestBody
        uploadDocumentDTO: UploadDocumentDTO
    ){
      announcementApplication.upload(
          uploadDocumentDTO = uploadDocumentDTO,
          user = user
      )
    }

    @Admin
    @DeleteMapping
    fun deleteAnnouncement(
        @RequestBody
        titleIDDTO: TitleIDDTO
    ){
        announcementApplication.delete(
            titleIDDTO = titleIDDTO
        )
    }

    @Admin
    @PatchMapping
    fun updateAnnouncement(
        @RequestBody
        updateDocumentDTO: UpdateDocumentDTO
    ){
        announcementApplication.update(
            updatedDocument = updateDocumentDTO
        )
    }

    @PostMapping("/prev-next")
    fun getPrevNext(
        @RequestBody
        frontBackDataRequestDTO: FrontBackDataRequestDTO
    ):FrontBackDTO{
        return announcementApplication.frontBackForTypeAnnouncement(frontBackDataRequestDTO)
    }
}
