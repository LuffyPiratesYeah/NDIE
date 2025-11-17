package hello.ndie.domain.document.service

import hello.ndie.domain.document.data.model.Document
import hello.ndie.domain.document.data.dto.ShowDocumentDTO
import hello.ndie.domain.document.data.dto.ShowDetailDocumentDTO
import hello.ndie.domain.document.data.dto.FrontBackDTO
import hello.ndie.domain.document.data.model.Answer
import hello.ndie.domain.document.repository.AnswerRepository
import hello.ndie.domain.document.repository.DocumentRepository
import hello.ndie.domain.user.data.model.User
import hello.ndie.domain.user.repository.UserRepository
import hello.ndie.shared.cache.CacheNames
import hello.ndie.shared.exception.DocumentNotFoundException
import hello.ndie.shared.exception.UserNotFoundException
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.stereotype.Service

@Service
@Transactional
class DocumentServiceImpl(
    private val documentRepository: DocumentRepository,
    private val answerRepository: AnswerRepository,
    private val userRepository: UserRepository,
): DocumentService {

    private val logger = LoggerFactory.getLogger(DocumentServiceImpl::class.java)
    /**
     * 제공된 정보로 새 문서를 생성합니다.
     *
     * @param title 문서의 제목
     * @param content 문서의 내용
     * @param user 문서를 생성하는 사용자
     * @param type 문서의 유형 (예: "announcement", "qna")
     */
    @CacheEvict(cacheNames = [CacheNames.DOCUMENTS_BY_TYPE], key = "#type")
    override fun upload(title: String, content: String, user: User, type: String) {
        logger.info("새 문서 생성 중: 제목: $title, 유형: $type, 사용자: ${user.id}")
        documentRepository.save(
            Document(
                user = user,
                title = title,
                content = content,
                type = type
            )
        )
        logger.debug("문서가 성공적으로 생성되었습니다")
    }

    /**
     * 기존 문서의 제목과 내용을 업데이트합니다.
     *
     * @param title 문서의 새 제목
     * @param content 문서의 새 내용
     * @param titleId 업데이트할 문서의 ID
     * @throws DocumentNotFoundException 해당 ID의 문서가 존재하지 않는 경우 발생
     */
    @Caching(
        evict = [
            CacheEvict(cacheNames = [CacheNames.DOCUMENT_DETAILS], key = "#titleId"),
            CacheEvict(cacheNames = [CacheNames.DOCUMENTS_BY_TYPE], allEntries = true)
        ]
    )
    override fun update(title: String, content: String, titleId: Long) {
        logger.info("문서 업데이트 중: ID: $titleId")
        val document = documentRepository.findById(titleId).orElseThrow {
            logger.warn("문서를 찾을 수 없음: ID: $titleId")
            DocumentNotFoundException("ID가 $titleId 인 문서를 찾을 수 없습니다.")
        }
        document.apply {
            this.title = title
            this.content = content
        }
        logger.debug("문서가 성공적으로 업데이트됨: ID: $titleId")
    }

    /**
     * 지정된 ID의 문서를 삭제합니다.
     *
     * @param titleId 삭제할 문서의 ID
     * @throws DocumentNotFoundException 해당 ID의 문서가 존재하지 않는 경우 발생
     */
    @Caching(
        evict = [
            CacheEvict(cacheNames = [CacheNames.DOCUMENT_DETAILS], key = "#titleId"),
            CacheEvict(cacheNames = [CacheNames.DOCUMENTS_BY_TYPE], allEntries = true)
        ]
    )
    override fun delete(titleId: Long) {
        logger.info("문서 삭제 중: ID: $titleId")
        if (!documentRepository.existsById(titleId)) {
            logger.warn("삭제 불가 - 문서를 찾을 수 없음: ID: $titleId")
            throw DocumentNotFoundException("ID가 $titleId 인 문서를 찾을 수 없습니다.")
        }
        documentRepository.deleteById(titleId)
        logger.debug("문서가 성공적으로 삭제됨: ID: $titleId")
    }

    /**
     * 특정 유형의 모든 문서를 생성일 기준으로 정렬하여 가져옵니다(최신순).
     *
     * @param type 가져올 문서의 유형 (예: "announcement", "qna")
     * @return 화면 표시용으로 포맷된 문서 DTO 목록
     */
    @Cacheable(cacheNames = [CacheNames.DOCUMENTS_BY_TYPE], key = "#type")
    override fun showDocuments(type: String): List<ShowDocumentDTO> {
        logger.debug("유형별 문서 조회 중: 유형: $type")
        val documents = documentRepository.findAllByTypeOrderByCreatedAtDesc(type)
        logger.debug("조회된 문서 수: ${documents.size}, 유형: $type")

        return documents.map { document ->
            ShowDocumentDTO(
                id = document.id!!,
                title = document.title,
                views = document.views,
                createdAt = document.createdAt,
                username = document.user.name,
                content = document.content
            )
        }
    }

    /**
     * ID로 특정 문서를 조회합니다.
     *
     * @param titleId 조회할 문서의 ID
     * @return 지정된 ID의 문서
     * @throws DocumentNotFoundException 해당 ID의 문서가 존재하지 않는 경우 발생
     */
    @Cacheable(cacheNames = [CacheNames.DOCUMENT_DETAILS], key = "#titleId")
    override fun showDetails(titleId: Long): ShowDetailDocumentDTO {
        logger.debug("문서 상세 정보 조회 중: ID: $titleId")
        val document = documentRepository.findById(titleId).orElseThrow {
            logger.warn("문서를 찾을 수 없음: ID: $titleId")
            DocumentNotFoundException("ID가 $titleId 인 문서를 찾을 수 없습니다.")
        }

        val userId = document.user.id ?: throw UserNotFoundException("해당 문서 작성자의 ID가 존재하지 않습니다.")

        return ShowDetailDocumentDTO(
            id = document.id,
            userId = userId,
            username = document.user.name,
            title = document.title,
            content = document.content,
            type = document.type,
            views = document.views,
            createdAt = document.createdAt
        )
    }

    /**
     * 문서의 조회수를 증가시킵니다.
     *
     * @param titleId 업데이트할 문서의 ID
     * @throws DocumentNotFoundException 해당 ID의 문서가 존재하지 않는 경우 발생
     */
    @Transactional
    @CacheEvict(cacheNames = [CacheNames.DOCUMENT_DETAILS], key = "#titleId")
    override fun upViews(titleId: Long) {
        logger.debug("문서 조회수 증가 중: ID: $titleId")
        val document = documentRepository.findById(titleId).orElseThrow {
            logger.warn("조회수 증가 불가 - 문서를 찾을 수 없음: ID: $titleId")
            DocumentNotFoundException("ID가 $titleId 인 문서를 찾을 수 없습니다.")
        }
        document.views += 1
        logger.debug("문서 조회수 업데이트됨: ID: $titleId, 조회수: ${document.views}")
    }

    /**
     * 특정 문서의 모든 댓글(답변)을 조회합니다.
     *
     * @param titleId 댓글을 조회할 문서의 ID
     * @return 문서에 대한 답변 목록, 없는 경우 null 반환
     */
    override fun commentViews(titleId: Long): List<Answer>? {
        logger.debug("문서 댓글 조회 중: ID: $titleId")
        val comments = answerRepository.findAllByDocumentId(titleId)
        logger.debug("조회된 댓글 수: ${comments?.size ?: 0}, 문서 ID: $titleId")
        return comments
    }

    /**
     * 문서에 댓글(답변)을 추가합니다.
     *
     * @param userId 댓글을 추가하는 사용자의 ID
     * @param titleId 댓글을 달 문서의 ID
     * @param content 댓글 내용
     * @throws DocumentNotFoundException 해당 ID의 문서가 존재하지 않는 경우 발생
     * @throws IllegalArgumentException 해당 ID의 사용자가 존재하지 않는 경우 발생
     */
    @CacheEvict(cacheNames = [CacheNames.DOCUMENT_DETAILS], key = "#titleId")
    override fun commentInputs(userId: Long, titleId: Long, content: String) {
        logger.info("문서에 댓글 추가 중: 문서 ID: $titleId, 사용자 ID: $userId")

        // 문서 조회, 없으면 예외 발생
        val document = documentRepository.findById(titleId).orElseThrow {
            logger.warn("댓글 추가 불가 - 문서를 찾을 수 없음: ID: $titleId")
            DocumentNotFoundException("ID가 $titleId 인 문서를 찾을 수 없습니다.")
        }

        // 사용자 조회, 없으면 예외 발생
        val user = userRepository.findById(userId).orElseThrow {
            logger.warn("댓글 추가 불가 - 사용자를 찾을 수 없음: ID: $userId")
            IllegalArgumentException("ID가 $userId 인 사용자를 찾을 수 없습니다.")
        }

        // 사용자가 null인지 추가 확인
        if (user == null) {
            logger.error("사용자를 찾았으나 null임: ID: $userId")
            throw IllegalStateException("사용자가 null일 수 없습니다")
        }

        // 답변 생성 및 저장
        answerRepository.save(
            Answer(
                document = document,
                user = user,
                content = content
            )
        )

        logger.debug("댓글이 성공적으로 추가됨: 문서 ID: $titleId")
    }

    /**
     * 지정된 문서 ID와 유형을 기준으로 이전 및 다음 문서를 조회합니다.
     *
     * @param type 탐색할 문서 유형 (예: "announcement", "qna")
     * @param titleId 기준 문서 ID
     * @return 이전 및 다음 문서에 대한 정보를 포함하는 DTO
     */
    override fun frontBackForType(type: String, titleId: Long): FrontBackDTO {
        logger.debug("이전/다음 문서 조회 중: 유형: $type, ID: $titleId")

        val prev = documentRepository.findFirstByTypeAndIdLessThanOrderByIdDesc(type, titleId)
        val next = documentRepository.findFirstByTypeAndIdGreaterThanOrderByIdAsc(type, titleId)

        logger.debug("문서 탐색 정보: ID: $titleId - " +
                "이전: ${prev?.id ?: "없음"}, 다음: ${next?.id ?: "없음"}")

        return FrontBackDTO(
            prevId = prev?.id,
            prevTitle = prev?.title,
            nextId = next?.id,
            nextTitle = next?.title
        )
    }
}
