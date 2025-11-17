package hello.ndie.domain.activity.service

import hello.ndie.domain.activity.data.model.Activity
import hello.ndie.domain.activity.repository.ActivityRepository
import hello.ndie.domain.document.data.dto.FrontBackDTO
import hello.ndie.domain.user.data.model.User
import hello.ndie.shared.exception.ActivityNotFoundException
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * 활동 관련 작업을 처리하는 ActivityService의 구현체입니다.
 * 이 서비스는 활동 생성, 조회 및 업데이트를 위한 메소드를 제공합니다.
 */
@Service
class ActivityServiceImpl(
    private val activityRepository: ActivityRepository
) : ActivityService {

    private val logger = LoggerFactory.getLogger(ActivityServiceImpl::class.java)

    /**
     * 제공된 정보로 새 활동을 생성합니다.
     *
     * @param user 활동을 생성하는 사용자
     * @param title 활동의 제목
     * @param content 활동의 내용
     * @param image 활동의 이미지 URL
     */
    override fun upload(user: User, title: String, content: String, image: String) {
        logger.info("사용자: ${user.id}, 제목: $title 로 새 활동 생성 중")
        activityRepository.save(
            Activity(
                user = user,
                title = title,
                content = content,
                image = image
            )
        )
    }

    /**
     * 생성일 기준으로 정렬된 모든 활동을 조회합니다(최신순).
     *
     * @return 활동 목록
     */
    override fun show(): List<Activity> {
        logger.debug("생성일 기준으로 정렬된 모든 활동 조회 중")
        return activityRepository.findAllByOrderByCreatedAtDesc()
    }

    /**
     * ID로 특정 활동을 조회합니다.
     *
     * @param id 조회할 활동의 ID
     * @return 지정된 ID의 활동
     * @throws ActivityNotFoundException 주어진 ID의 활동이 존재하지 않는 경우 발생
     */
    override fun showDetails(id: Long): Activity {
        logger.debug("ID: $id 에 대한 활동 상세 정보 조회 중")
        return activityRepository.findById(id).orElseThrow {
            logger.warn("ID: $id 인 활동을 찾을 수 없음")
            ActivityNotFoundException("ID가 $id 인 활동을 찾을 수 없습니다.")
        }
    }

    /**
     * 활동의 조회수를 증가시킵니다.
     *
     * @param id 업데이트할 활동의 ID
     * @throws ActivityNotFoundException 주어진 ID의 활동이 존재하지 않는 경우 발생
     */
    @Transactional
    override fun upViews(id: Long) {
        logger.debug("활동 ID: $id 의 조회수 증가 중")
        val activity = activityRepository.findById(id).orElseThrow {
            logger.warn("조회수 증가 불가 - ID: $id 인 활동을 찾을 수 없음")
            ActivityNotFoundException("ID가 $id 인 활동을 찾을 수 없습니다.")
        }
        activity.views += 1
        logger.debug("활동 ID: $id 의 조회수가 ${activity.views}로 업데이트됨")
    }

    /**
     * 지정된 활동 ID를 기준으로 이전 및 다음 활동을 조회합니다.
     *
     * @param id 기준 활동 ID
     * @return 이전 및 다음 활동에 대한 정보를 포함하는 DTO
     */
    override fun getFrontBackById(id: Long): FrontBackDTO {
        logger.debug("ID: $id 에 대한 이전 및 다음 활동 조회 중")
        val prev = activityRepository.findTopByIdLessThanOrderByIdDesc(id)
        val next = activityRepository.findTopByIdGreaterThanOrderByIdAsc(id)

        return FrontBackDTO(
            prevId = prev?.id,
            prevTitle = prev?.title,
            nextId = next?.id,
            nextTitle = next?.title
        )
    }
}
