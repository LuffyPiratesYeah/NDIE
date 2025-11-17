package hello.ndie.domain.document.application

import hello.ndie.domain.document.data.dto.*
import hello.ndie.domain.user.data.dto.CustomOAuth2User

/**
 * Q&A 관련 기능을 정의하는 인터페이스입니다.
 * 질문 문서의 관리 및 답변 처리 기능을 포함합니다.
 */
interface QnAApplication {
    /**
     * 새로운 Q&A 질문을 업로드합니다.
     */
    fun upload(uploadDocumentDTO: UploadDocumentDTO, user: CustomOAuth2User)

    /**
     * 기존 Q&A 질문을 업데이트합니다.
     */
    fun update(updatedDocument: UpdateDocumentDTO)

    /**
     * Q&A 질문을 삭제합니다.
     */
    fun delete(titleIDDTO: TitleIDDTO)

    /**
     * 모든 Q&A 질문 목록을 조회합니다.
     */
    fun showDocuments(): List<ShowDocumentDTO>

    /**
     * 특정 Q&A 질문의 상세 정보를 조회합니다.
     */
    fun showDetailDocuments(titleIDDTO: TitleIDDTO): ShowDetailDocumentDTO

    /**
     * Q&A 질문에 답변을 추가합니다.
     */
    fun answerInput(answerInputDTO: AnswerInputDTO, user: CustomOAuth2User)

    /**
     * 특정 Q&A 질문의 모든 답변을 조회합니다.
     */
    fun answerViews(titleID: Long): AnswerViewsDTO

    /**
     * 특정 Q&A 질문의 이전 및 다음 질문 정보를 조회합니다.
     */
    fun frontBackForTypeQNA(frontBackDataRequestDTO: FrontBackDataRequestDTO): FrontBackDTO
}
