package hello.ndie.domain.document.application

import hello.ndie.domain.document.data.dto.*
import hello.ndie.domain.document.service.DocumentService
import hello.ndie.domain.user.data.dto.CustomOAuth2User
import hello.ndie.domain.user.repository.UserRepository
import hello.ndie.shared.exception.UserNotFoundException
import org.springframework.stereotype.Service

/**
 * 공지사항 관련 기능을 구현하는 애플리케이션 서비스 클래스입니다.
 * 문서 서비스를 활용하여 공지사항 타입의 문서를 관리합니다.
 */
@Service
class AnnouncementApplicationImpl(
    private val documentService: DocumentService,
    private val userRepository: UserRepository,
): AnnouncementApplication {
    /**
     * 새로운 공지사항을 업로드합니다.
     *
     * @param uploadDocumentDTO 업로드할 문서 정보(제목, 내용)를 담은 DTO
     * @param user 문서를 업로드하는 인증된 사용자
     * @throws UserNotFoundException 사용자를 찾을 수 없는 경우 발생
     */
    override fun upload(
        uploadDocumentDTO: UploadDocumentDTO,
        user: CustomOAuth2User
    ) {
        val foundUser = userRepository.findByUsername(user.getUsername())
            ?: throw UserNotFoundException("사용자를 찾을 수 없습니다: ${user.getUsername()}")

        documentService.upload(
            title = uploadDocumentDTO.title,
            content = uploadDocumentDTO.content,
            user = foundUser,
            type = "announcement"
        )
    }

    /**
     * 기존 공지사항을 업데이트합니다.
     *
     * @param updatedDocument 업데이트할 문서 정보(제목, 내용, ID)를 담은 DTO
     */
    override fun update(updatedDocument: UpdateDocumentDTO) {
        documentService.update(
            title = updatedDocument.title,
            content = updatedDocument.content,
            titleId = updatedDocument.titleId
        )
    }

    /**
     * 공지사항을 삭제합니다.
     *
     * @param titleIDDTO 삭제할 문서의 ID를 담은 DTO
     */
    override fun delete(titleIDDTO: TitleIDDTO) {
        documentService.delete(
            titleId = titleIDDTO.titleId
        )
    }

    /**
     * 모든 공지사항 목록을 조회합니다.
     *
     * @return 공지사항 목록을 담은 DTO 리스트
     */
    override fun showDocuments(): List<ShowDocumentDTO> {
        return documentService.showDocuments(
            type = "announcement"
        )
    }

    /**
     * 특정 공지사항의 상세 정보를 조회합니다.
     *
     * @param titleIDDTO 조회할 문서의 ID를 담은 DTO
     * @return 공지사항 상세 정보를 담은 DTO
     * @throws UserNotFoundException 문서 작성자의 ID가 존재하지 않는 경우 발생
     */
    override fun showDetailDocuments(titleIDDTO: TitleIDDTO): ShowDetailDocumentDTO {
        return documentService.showDetails(
            titleId = titleIDDTO.titleId
        )
    }

    /**
     * 특정 공지사항의 이전 및 다음 문서 정보를 조회합니다.
     *
     * @param frontBackDataRequestDTO 문서 유형과 ID 정보를 담은 DTO
     * @return 이전 및 다음 문서 정보를 담은 DTO
     */
    override fun frontBackForTypeAnnouncement(
        frontBackDataRequestDTO: FrontBackDataRequestDTO
    ): FrontBackDTO {
        return documentService.frontBackForType(
            type = frontBackDataRequestDTO.type,
            titleId = frontBackDataRequestDTO.titleID
        )
    }
}
