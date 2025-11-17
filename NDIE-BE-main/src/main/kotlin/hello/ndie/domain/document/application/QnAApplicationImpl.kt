package hello.ndie.domain.document.application

import hello.ndie.domain.document.data.dto.*
import hello.ndie.domain.document.data.model.Answer
import hello.ndie.domain.document.service.DocumentService
import hello.ndie.domain.user.data.dto.CustomOAuth2User
import hello.ndie.domain.user.repository.UserRepository
import hello.ndie.shared.exception.UserNotFoundException
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Service

@Service
class QnAApplicationImpl(
    private val documentService: DocumentService,
    private val userRepository: UserRepository,
): QnAApplication {
    override fun upload(
        uploadDocumentDTO: UploadDocumentDTO,
        @AuthenticationPrincipal
        user: CustomOAuth2User
    ) {
        val foundUser = userRepository.findByUsername(user.getUsername())
            ?: throw UserNotFoundException("사용자를 찾을 수 없습니다: ${user.getUsername()}")

        documentService.upload(
            title = uploadDocumentDTO.title,
            content = uploadDocumentDTO.content,
            user = foundUser,
            type = "QNA",
        )
    }

    override fun update(updatedDocument: UpdateDocumentDTO) {
        documentService.update(
            title = updatedDocument.title,
            content = updatedDocument.content,
            titleId = updatedDocument.titleId
        )
    }

    override fun delete(titleIDDTO: TitleIDDTO) {
        documentService.delete(
            titleId = titleIDDTO.titleId
        )
    }

    override fun showDocuments(): List<ShowDocumentDTO> {
        return documentService.showDocuments(
            type = "QNA"
        )
    }

    override fun showDetailDocuments(titleIDDTO: TitleIDDTO): ShowDetailDocumentDTO {
        return documentService.showDetails(
            titleId = titleIDDTO.titleId,
        )
    }

    override fun answerInput(
        answerInputDTO: AnswerInputDTO,
        user: CustomOAuth2User
    ) {
        documentService.commentInputs(
            userId = user.getId()!!,
            titleId = answerInputDTO.titleId,
            content = answerInputDTO.content,
        )
    }

    override fun answerViews(titleID: Long): AnswerViewsDTO {
        val commentViews: List<Answer>? = documentService.commentViews(titleID)

        if (commentViews.isNullOrEmpty()) {
            return AnswerViewsDTO()
        } else {
            val firstComment = commentViews[0]
            return AnswerViewsDTO(
                commentID = firstComment.id,
                comment = firstComment.content
            )
        }
    }

    override fun frontBackForTypeQNA(frontBackDataRequestDTO: FrontBackDataRequestDTO): FrontBackDTO {
        return documentService.frontBackForType(
            type = frontBackDataRequestDTO.type,
            titleId = frontBackDataRequestDTO.titleID
        )
    }
}
