package hello.ndie.domain.user.data.dto

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User

class CustomOAuth2User(private val userDTO: UserDto) : OAuth2User {

    override fun getAttributes(): Map<String, Any>? {
        return null
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf(GrantedAuthority { userDTO.role })
    }

    override fun getName(): String {
        return userDTO.name
    }

    fun getUsername(): String {
        return userDTO.username
    }

    fun getId(): Long? {
        return userDTO.id
    }
}
