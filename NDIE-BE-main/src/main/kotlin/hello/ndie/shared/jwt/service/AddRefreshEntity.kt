package hello.ndie.shared.jwt.service

import hello.ndie.shared.jwt.data.model.RefreshToken
import hello.ndie.shared.jwt.repository.RefreshRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.*

@Service
@Transactional
class AddRefreshEntity(
    private val refreshRepository: RefreshRepository
) {

    /**
     * 리프레시 토큰 엔티티를 데이터베이스에 저장하는 메서드입니다.
     *
     * @param uid 사용자 UID
     * @param email 사용자 이메일
     * @param refresh 리프레시 토큰 값
     * @param expiredMs 리프레시 토큰의 만료 시간 (밀리초)
     */
    fun addRefreshEntity(username: String, refresh: String, expiredMs: Long) {
        val createdAt = Date(System.currentTimeMillis())
        val expiresAt = Date(System.currentTimeMillis() + expiredMs)
        val refreshToken = RefreshToken(
            username = username,
            token = refresh,
            createdAt = createdAt,
            expiresAt = expiresAt
        )
        refreshRepository.save(refreshToken)
    }
}
