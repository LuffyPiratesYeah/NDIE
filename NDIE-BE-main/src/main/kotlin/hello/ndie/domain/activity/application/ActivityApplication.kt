package hello.ndie.domain.activity.application

import hello.ndie.domain.activity.data.dto.ActivityUploadDTO
import hello.ndie.domain.activity.data.dto.GetURLDTO
import hello.ndie.domain.activity.data.dto.ShowActivity
import hello.ndie.domain.activity.data.dto.ShowActivityDetailDTO
import hello.ndie.domain.document.data.dto.FrontBackDTO
import hello.ndie.domain.document.data.dto.FrontBackDataRequestDTO
import hello.ndie.domain.user.data.dto.CustomOAuth2User
import hello.ndie.shared.exception.ActivityNotFoundException
import hello.ndie.shared.exception.ImageUploadException
import org.springframework.web.multipart.MultipartFile

/**
 * 활동 관련 작업을 위한 애플리케이션 인터페이스입니다.
 * 이 계층은 컨트롤러와 서비스 사이의 비즈니스 로직을 처리하며,
 * 데이터 변환 및 유효성 검사를 포함합니다.
 */
interface ActivityApplication {
    /**
     * 이미지 파일을 업로드하고 접근 가능한 URL을 반환합니다.
     *
     * @param file 업로드할 이미지 파일
     * @return 업로드된 이미지의 URL을 포함하는 DTO
     * @throws ImageUploadException 이미지 업로드 실패 시 발생
     */
    fun imageUpload(file: MultipartFile): GetURLDTO

    /**
     * 제공된 정보로 새 활동을 생성합니다.
     *
     * @param customOAuth2User 활동을 생성하는 인증된 사용자
     * @param activityUploadDTO 활동 세부 정보(제목, 내용, 이미지)를 포함하는 DTO
     */
    fun activityUpload(customOAuth2User: CustomOAuth2User, activityUploadDTO: ActivityUploadDTO)

    /**
     * 모든 활동을 조회하고 표시를 위해 DTO로 변환합니다.
     *
     * @return 표시용으로 포맷된 활동 목록
     */
    fun show(): List<ShowActivity>

    /**
     * ID로 특정 활동의 세부 정보를 조회합니다.
     *
     * @param id 조회할 활동의 ID
     * @return 활동에 대한 상세 정보를 포함하는 DTO
     * @throws ActivityNotFoundException 주어진 ID의 활동이 존재하지 않는 경우 발생
     */
    fun showDetails(id: Long): ShowActivityDetailDTO

    /**
     * 지정된 ID를 기준으로 이전 및 다음 활동에 대한 정보를 조회합니다.
     *
     * @param request 탐색할 항목의 유형과 ID를 포함하는 DTO
     * @return 이전 및 다음 항목에 대한 정보를 포함하는 DTO
     * @throws IllegalArgumentException 요청된 유형이 지원되지 않는 경우 발생
     */
    fun getFrontBack(request: FrontBackDataRequestDTO): FrontBackDTO

    /**
     * 활동의 조회수를 증가시킵니다.
     *
     * @param id 업데이트할 활동의 ID
     * @throws ActivityNotFoundException 주어진 ID의 활동이 존재하지 않는 경우 발생
     */
    fun upViews(id: Long)
}
