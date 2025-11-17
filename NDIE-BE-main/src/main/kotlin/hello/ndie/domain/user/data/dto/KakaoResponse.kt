package hello.ndie.domain.user.data.dto

class KakaoResponse(private val attributes: Map<String, Any>) : OAuth2Response {
    private val kakaoAccount: Map<String, Any> = attributes["kakao_account"] as Map<String, Any>
    private val profile: Map<String, Any> = kakaoAccount["profile"] as Map<String, Any>

    override fun getProvider(): String {
        return "kakao"
    }

    override fun getProviderId(): String {
        return attributes["id"].toString()
    }

    override fun getEmail(): String {
        return kakaoAccount["email"]?.toString() ?: "N/A"
    }

    override fun getName(): String {
        return profile["nickname"].toString()
    }

    override fun getAttributes(): Map<String, Any> {
        return attributes
    }
    override fun getId(): Long {
        return attributes["id"].toString().toLong()
    }
}
