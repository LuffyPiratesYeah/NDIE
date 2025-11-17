package hello.ndie.shared.service

import hello.ndie.domain.user.data.dto.CustomOAuth2User
import hello.ndie.domain.user.data.model.User
import hello.ndie.domain.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class CustomUserToUser(
    val userRepository: UserRepository
) {
    fun change(customOAuth2User: CustomOAuth2User): User{
        return userRepository.findByUsername(customOAuth2User.getUsername())?: throw IllegalArgumentException("User not found")
    }
}