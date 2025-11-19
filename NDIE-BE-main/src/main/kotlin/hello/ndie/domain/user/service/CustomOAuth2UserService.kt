package hello.ndie.domain.user.service


import hello.ndie.domain.user.data.dto.CustomOAuth2User
import hello.ndie.domain.user.data.dto.KakaoResponse
import hello.ndie.domain.user.data.dto.OAuth2Response
import hello.ndie.domain.user.data.dto.UserDto
import hello.ndie.domain.user.data.model.User
import hello.ndie.domain.user.repository.UserRepository
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class CustomOAuth2UserService(
    private val userRepository: UserRepository
) : DefaultOAuth2UserService() {

    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = super.loadUser(userRequest)
        println("OAuth2UserRequest: $userRequest")
        println("OAuth2User: ${oAuth2User.attributes}")
        val registrationId = userRequest.clientRegistration.registrationId

        var oAuth2Response: OAuth2Response? = null
        if (registrationId == "kakao") {
            oAuth2Response = KakaoResponse(oAuth2User.attributes)
        }

        println("OAuth2Response: $oAuth2Response")
        val username = "${oAuth2Response?.getProvider()} ${oAuth2Response?.getProviderId()}"
        val existData = userRepository.findByUsername(username)
        println("username = $username")
        println("Existing User: $existData")

        return if (existData == null) {
            val savedUser = userRepository.save(
                User(
                    username = username,
                    name = oAuth2Response?.getName().toString(),
                    email = oAuth2Response?.getEmail().toString(),
                    role = "ROLE_USER",
                    password = ""
                )
            )

            val userDTO = UserDto(
                username = savedUser.username,
                name = savedUser.name,
                email = savedUser.email,
                role = savedUser.role,
                id = savedUser.id
            )

            CustomOAuth2User(userDTO)
        } else {
            val userDTO = UserDto(
                username = existData.username,
                name = existData.name,
                role = existData.role,
                email = existData.email,
                id = existData.id
            )
            CustomOAuth2User(userDTO)
        }
    }
}
