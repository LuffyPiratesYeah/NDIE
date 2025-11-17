package hello.ndie.domain.user.data.dto

interface OAuth2Response {
    fun getProvider(): String
    fun getProviderId(): String
    fun getEmail(): String
    fun getName(): String
    fun getAttributes(): Map<String, Any>
    fun getId(): Long
}
