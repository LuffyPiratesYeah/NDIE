package hello.ndie.domain.document.application

import hello.ndie.domain.document.data.dto.*
import hello.ndie.domain.document.data.model.Document
import hello.ndie.domain.user.data.dto.CustomOAuth2User

/**
 * 공지사항 관련 기능을 정의하는 인터페이스입니다.
 * 공지사항 문서의 관리 및 조회 기능을 포함합니다.
 */
interface AnnouncementApplication {
    /**
     * 새로운 공지사항을 업로드합니다.
     */
    fun upload(uploadDocumentDTO: UploadDocumentDTO, user: CustomOAuth2User)

    /**
     * 기존 공지사항을 업데이트합니다.
     */
    fun update(updatedDocument: UpdateDocumentDTO)

    /**
     * 공지사항을 삭제합니다.
     */
    fun delete(titleIDDTO: TitleIDDTO)

    /**
     * 모든 공지사항 목록을 조회합니다.
     */
    fun showDocuments(): List<ShowDocumentDTO>

    /**
     * 특정 공지사항의 상세 정보를 조회합니다.
     */
    fun showDetailDocuments(titleIDDTO: TitleIDDTO): ShowDetailDocumentDTO

    /**
     * 특정 공지사항의 이전 및 다음 문서 정보를 조회합니다.
     */
    fun frontBackForTypeAnnouncement(frontBackDataRequestDTO: FrontBackDataRequestDTO): FrontBackDTO
}
