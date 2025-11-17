package hello.ndie.domain.activity.service

import hello.ndie.domain.activity.data.model.Activity
import hello.ndie.domain.document.data.dto.FrontBackDTO
import hello.ndie.domain.user.data.model.User
import hello.ndie.shared.exception.ActivityNotFoundException

/**
 * 활동을 관리하기 위한 서비스 인터페이스.
 * 활동 생성, 조회 및 업데이트를 위한 메소드를 제공합니다.
 */
interface ActivityService {
    /**
     * 제공된 정보로 새 활동을 생성합니다.
     *
     * @param user 활동을 생성하는 사용자
     * @param title 활동의 제목
     * @param content 활동의 내용
     * @param image 활동의 이미지 URL
     */
    fun upload(user: User, title: String, content: String, image: String)

    /**
     * 생성일 기준으로 정렬된 모든 활동을 조회합니다(최신순).
     *
     * @return 활동 목록
     */
    fun show(): List<Activity>

    /**
     * ID로 특정 활동을 조회합니다.
     *
     * @param id 조회할 활동의 ID
     * @return 지정된 ID의 활동
     * @throws ActivityNotFoundException 주어진 ID의 활동이 존재하지 않는 경우 발생
     */
    fun showDetails(id: Long): Activity

    /**
     * 활동의 조회수를 증가시킵니다.
     *
     * @param id 업데이트할 활동의 ID
     * @throws ActivityNotFoundException 주어진 ID의 활동이 존재하지 않는 경우 발생
     */
    fun upViews(id: Long)

    /**
     * 지정된 활동 ID를 기준으로 이전 및 다음 활동을 조회합니다.
     *
     * @param id 기준 활동 ID
     * @return 이전 및 다음 활동에 대한 정보를 포함하는 DTO
     */
    fun getFrontBackById(id: Long): FrontBackDTO
}
