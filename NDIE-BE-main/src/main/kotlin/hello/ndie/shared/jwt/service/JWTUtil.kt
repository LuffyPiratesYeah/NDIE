package hello.ndie.shared.jwt.service

import io.jsonwebtoken.JwtException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import io.jsonwebtoken.Jwts
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec
import java.nio.charset.StandardCharsets
import java.util.*

@Component
class JWTUtil(
    @Value("\${spring.jwt.secret}") secret: String
) {

    private val secretKey: SecretKey = SecretKeySpec(secret.toByteArray(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().algorithm)

    fun getUsername(token: String): String {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body["username", String::class.java]
    }

    fun getRole(token: String): String {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body["role", String::class.java]
    }

    fun isExpired(token: String): Boolean {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body.expiration.before(Date())
    }

    fun createJwt(
        category: String,
        username: String,
        role: String,
        expiredMs: Long
    ): String {
        return try {
            Jwts.builder()
                .claim("category", category)
                .claim("username", username)
                .claim("role", role)
                .issuedAt(Date(System.currentTimeMillis()))
                .expiration(Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact()
        } catch (e: Exception) {
            throw RuntimeException("JWT 생성 중 오류가 발생했습니다.")
        }
    }
    fun getCategory(token: String): String {
        return try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .payload
                .get("category", String::class.java)
        } catch (e: JwtException) {
            throw RuntimeException("토큰에서 카테고리를 추출하는 중 오류가 발생했습니다.")
        }
    }

}