package hello.ndie.shared.jwt.repository

import hello.ndie.shared.jwt.data.model.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface RefreshRepository : JpaRepository<RefreshToken, Long> {

    /**
     * 주어진 토큰이 이미 데이터베이스에 존재하는지 확인합니다.
     *
     * @param token 리프레시 토큰
     * @return 토큰이 존재하면 true, 그렇지 않으면 false
     */
    fun existsByToken(token: String): Boolean

    /**
     * 주어진 토큰을 데이터베이스에서 삭제합니다.
     *
     * @param token 삭제할 리프레시 토큰
     */
    @Transactional
    fun deleteByToken(token: String)

    /**
     * 주어진 토큰에 해당하는 리프레시 토큰을 조회합니다.
     *
     * @param token 리프레시 토큰
     * @return 리프레시 토큰
     */
    fun findByToken(token: String): RefreshToken
}