package hello.ndie.domain.activity.api

import hello.ndie.domain.activity.application.ActivityApplication
import hello.ndie.domain.activity.data.dto.*
import hello.ndie.domain.document.data.dto.FrontBackDTO
import hello.ndie.domain.document.data.dto.FrontBackDataRequestDTO
import hello.ndie.domain.document.service.DocumentService
import hello.ndie.domain.user.data.dto.CustomOAuth2User
import hello.ndie.shared.config.Admin
import hello.ndie.shared.config.User
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

/**
 * 활동 관련 엔드포인트를 위한 REST 컨트롤러입니다.
 * 활동 관련 HTTP 요청을 처리하고 비즈니스 로직을 애플리케이션 계층에 위임합니다.
 */
@RestController
class ActivityController(
    private val activityApplication: ActivityApplication,
    private val documentService: DocumentService
) {
    private val logger = LoggerFactory.getLogger(ActivityController::class.java)

    /**
     * 이미지 파일을 업로드하고 접근 가능한 URL을 반환합니다.
     *
     * @param fileUploadDTO 업로드할 파일을 포함하는 DTO
     * @return 업로드된 이미지의 URL을 포함하는 DTO
     */
    @User
    @PostMapping("/upload", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun imageUpload(
        @ModelAttribute fileUploadDTO: FileUploadDTO
    ): ResponseEntity<GetURLDTO> {
        logger.info("파일 업로드 요청 수신: ${fileUploadDTO.file.originalFilename}")
        val result = activityApplication.imageUpload(fileUploadDTO.file)
        return ResponseEntity.ok(result)
    }

    /**
     * 제공된 정보로 새 활동을 생성합니다.
     * 관리자만 접근 가능합니다.
     *
     * @param activityUploadDTO 활동 세부 정보(제목, 내용, 이미지)를 포함하는 DTO
     * @param user 활동을 생성하는 인증된 사용자
     * @return HTTP 201 Created 상태
     */
    @Admin
    @PostMapping("/activity")
    fun activityUpload(
        @RequestBody activityUploadDTO: ActivityUploadDTO,
        @AuthenticationPrincipal user: CustomOAuth2User
    ): ResponseEntity<Void> {
        logger.info("사용자 ${user.name}로부터 활동 업로드 요청 수신, 제목: ${activityUploadDTO.title}")

        activityApplication.activityUpload(
            customOAuth2User = user,
            activityUploadDTO = activityUploadDTO
        )

        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    /**
     * 모든 활동을 조회합니다.
     *
     * @return 표시용으로 포맷된 활동 목록
     */
    @GetMapping("/activity")
    fun getActivity(): ResponseEntity<List<ShowActivity>> {
        logger.info("모든 활동 조회 요청 수신")
        val activities = activityApplication.show()
        return ResponseEntity.ok(activities)
    }

    /**
     * ID로 특정 활동의 세부 정보를 조회합니다.
     *
     * @param activityId 조회할 활동의 ID
     * @return 활동에 대한 상세 정보를 포함하는 DTO
     */
    @GetMapping("/activity/{activityId}")
    fun getActivityOne(
        @PathVariable("activityId") activityId: Long
    ): ResponseEntity<ShowActivityDetailDTO> {
        logger.info("활동 ID $activityId 에 대한 상세 정보 조회 요청 수신")
        val activityDetail = activityApplication.showDetails(activityId)
        return ResponseEntity.ok(activityDetail)
    }

    /**
     * 문서의 조회수를 증가시킵니다.
     *
     * @param announcementId 업데이트할 문서의 ID
     * @return HTTP 200 OK 상태
     */
    @GetMapping("/document/up/{announcementID}")
    fun upDocument(
        @PathVariable("announcementID") announcementId: Long
    ): ResponseEntity<Void> {
        logger.info("문서 ID $announcementId 의 조회수 증가 중")
        documentService.upViews(announcementId)
        return ResponseEntity.ok().build()
    }

    /**
     * 활동의 조회수를 증가시킵니다.
     *
     * @param activityId 업데이트할 활동의 ID
     * @return HTTP 200 OK 상태
     */
    @GetMapping("/activity/up/{activityId}")
    fun upActivity(
        @PathVariable("activityId") activityId: Long
    ): ResponseEntity<Void> {
        logger.info("활동 ID $activityId 의 조회수 증가 중")
        activityApplication.upViews(activityId)
        return ResponseEntity.ok().build()
    }

    /**
     * 지정된 ID를 기준으로 이전 및 다음 활동에 대한 정보를 조회합니다.
     *
     * @param request 탐색할 항목의 유형과 ID를 포함하는 DTO
     * @return 이전 및 다음 항목에 대한 정보를 포함하는 DTO
     */
    @PostMapping("/activity/prev-next")
    fun getFrontBack(
        @RequestBody request: FrontBackDataRequestDTO
    ): ResponseEntity<FrontBackDTO> {
        logger.info("이전/다음 탐색 요청 수신, 유형: ${request.type}, ID: ${request.titleID}")
        val result = activityApplication.getFrontBack(request)
        return ResponseEntity.ok(result)
    }
}
