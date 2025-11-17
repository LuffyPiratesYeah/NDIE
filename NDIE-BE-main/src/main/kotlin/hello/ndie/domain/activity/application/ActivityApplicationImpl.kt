package hello.ndie.domain.activity.application

import hello.ndie.domain.activity.data.dto.ActivityUploadDTO
import hello.ndie.domain.activity.data.dto.GetURLDTO
import hello.ndie.domain.activity.data.dto.ShowActivity
import hello.ndie.domain.activity.data.dto.ShowActivityDetailDTO
import hello.ndie.domain.activity.data.model.Activity
import hello.ndie.domain.activity.service.ActivityService
import hello.ndie.domain.document.data.dto.FrontBackDTO
import hello.ndie.domain.document.data.dto.FrontBackDataRequestDTO
import hello.ndie.domain.user.data.dto.CustomOAuth2User
import hello.ndie.shared.exception.ActivityNotFoundException
import hello.ndie.shared.exception.ImageUploadException
import hello.ndie.shared.service.CustomUserToUser
import hello.ndie.shared.service.ImageUploadService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

/**
 * 활동 관련 작업을 처리하는 ActivityApplication의 구현체입니다.
 * 이 클래스는 컨트롤러와 서비스 사이의 중개자 역할을 하며,
 * 데이터 변환, 유효성 검사 및 비즈니스 로직을 처리합니다.
 */
@Service
class ActivityApplicationImpl(
    private val activityService: ActivityService,
    private val imageUploadService: ImageUploadService,
    private val customUserToUser: CustomUserToUser
): ActivityApplication {

    private val logger = LoggerFactory.getLogger(ActivityApplicationImpl::class.java)

    /**
     * 이미지 파일을 업로드하고 접근 가능한 URL을 반환합니다.
     *
     * @param file 업로드할 이미지 파일
     * @return 업로드된 이미지의 URL을 포함하는 DTO
     * @throws ImageUploadException 이미지 업로드 실패 시 발생
     */
    override fun imageUpload(file: MultipartFile): GetURLDTO {
        logger.info("이미지 파일 업로드 중: ${file.originalFilename}, 크기: ${file.size} 바이트")

        val uploadedUrl = imageUploadService.upload(file)
            ?: run {
                logger.error("이미지 업로드 실패: ${file.originalFilename}")
                throw ImageUploadException("이미지 업로드에 실패했습니다.")
            }

        logger.info("이미지 업로드 성공, URL: $uploadedUrl")
        return GetURLDTO(url = uploadedUrl)
    }

    /**
     * 제공된 정보로 새 활동을 생성합니다.
     *
     * @param customOAuth2User 활동을 생성하는 인증된 사용자
     * @param activityUploadDTO 활동 세부 정보(제목, 내용, 이미지)를 포함하는 DTO
     */
    override fun activityUpload(customOAuth2User: CustomOAuth2User, activityUploadDTO: ActivityUploadDTO) {
        logger.info("제목이 '${activityUploadDTO.title}'인 새 활동 생성 중")

        val user = customUserToUser.change(customOAuth2User)
        logger.debug("OAuth2User를 User 엔티티로 변환: ID ${user.id}")

        activityService.upload(
            user = user,
            title = activityUploadDTO.title,
            content = activityUploadDTO.content,
            image = activityUploadDTO.image
        )

        logger.info("사용자 ID ${user.id}에 대한 활동 생성 완료")
    }

    /**
     * 모든 활동을 조회하고 표시를 위해 DTO로 변환합니다.
     *
     * @return 표시용으로 포맷된 활동 목록
     */
    override fun show(): List<ShowActivity> {
        logger.debug("표시를 위한 모든 활동 조회 중")

        val activities: List<Activity> = activityService.show()
        logger.debug("${activities.size}개의 활동 찾음")

        return activities.map { activity ->
            ShowActivity(
                id = activity.id,
                username = activity.user.name,
                title = activity.title,
                createdAt = activity.createdAt,
                views = activity.views
            )
        }
    }

    /**
     * ID로 특정 활동의 세부 정보를 조회합니다.
     *
     * @param id 조회할 활동의 ID
     * @return 활동에 대한 상세 정보를 포함하는 DTO
     * @throws ActivityNotFoundException 주어진 ID의 활동이 존재하지 않는 경우 발생
     */
    override fun showDetails(id: Long): ShowActivityDetailDTO {
        logger.debug("활동 ID $id 에 대한 상세 정보 조회 중")

        // 서비스 메소드는 활동을 찾지 못하면 ActivityNotFoundException을 발생시킴
        val activity = activityService.showDetails(id)

        // 사용자 ID가 null이 아닌지 확인
        val userId = activity.user.id ?: run {
            logger.error("활동 ${activity.id} 의 사용자 ID가 null입니다")
            throw IllegalStateException("사용자 ID는 null일 수 없습니다")
        }

        logger.debug("활동 ID $id 에 대한 상세 정보 조회 성공")
        return ShowActivityDetailDTO(
            id = activity.id,
            userId = userId,
            username = activity.user.name,
            title = activity.title,
            content = activity.content,
            image = activity.image,
            createdAt = activity.createdAt
        )
    }

    /**
     * 지정된 ID를 기준으로 이전 및 다음 활동에 대한 정보를 조회합니다.
     *
     * @param request 탐색할 항목의 유형과 ID를 포함하는 DTO
     * @return 이전 및 다음 항목에 대한 정보를 포함하는 DTO
     * @throws IllegalArgumentException 요청된 유형이 지원되지 않는 경우 발생
     */
    override fun getFrontBack(request: FrontBackDataRequestDTO): FrontBackDTO {
        logger.debug("유형: ${request.type}, ID: ${request.titleID} 에 대한 이전/다음 탐색 정보 조회 중")

        return when (request.type.lowercase()) {
            "activity" -> {
                val result = activityService.getFrontBackById(request.titleID)
                logger.debug("활동 ID ${request.titleID} 에 대한 이전/다음 탐색 정보 조회 완료")
                result
            }
            else -> {
                logger.warn("지원되지 않는 유형의 이전/다음 탐색 요청: ${request.type}")
                throw IllegalArgumentException("지원하지 않는 type입니다: ${request.type}")
            }
        }
    }

    /**
     * 활동의 조회수를 증가시킵니다.
     *
     * @param id 업데이트할 활동의 ID
     * @throws ActivityNotFoundException 주어진 ID의 활동이 존재하지 않는 경우 발생
     */
    override fun upViews(id: Long) {
        logger.debug("활동 ID $id 의 조회수 증가 중")
        try {
            activityService.upViews(id)
            logger.debug("활동 ID $id 의 조회수 증가 성공")
        } catch (e: ActivityNotFoundException) {
            logger.error("조회수 증가 실패 - 활동 ID $id 를 찾을 수 없음", e)
            throw e
        }
    }
}
