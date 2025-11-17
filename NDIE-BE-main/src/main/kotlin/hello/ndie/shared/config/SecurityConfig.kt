package hello.ndie.shared.config

import hello.ndie.domain.user.data.dto.CustomOAuth2User
import hello.ndie.domain.user.service.CustomFailureHandler
import hello.ndie.domain.user.service.CustomOAuth2UserService
import hello.ndie.domain.user.service.CustomSuccessHandler
import jakarta.servlet.http.HttpServletRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import java.util.*

@Configuration
@EnableWebSecurity
class SecurityConfig (
    private val customOAuth2UserService: CustomOAuth2UserService,
    private val customFailureHandler: CustomFailureHandler,
    private val customSuccessHandler: CustomSuccessHandler
    ){

    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeRequests { auth ->
            auth.anyRequest().permitAll()
        }
            .csrf { csrf -> csrf.disable() }
            .formLogin { form -> form.disable() }
            .httpBasic { httpBasic -> httpBasic.disable() }
            .sessionManagement { session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        http.oauth2Login {
            it.userInfoEndpoint { userInfoEndpointConfig ->
                userInfoEndpointConfig.userService(customOAuth2UserService)
            }
            it.successHandler(customSuccessHandler)
            it.failureHandler(customFailureHandler)
        }
        http.sessionManagement { session ->
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }

        http.cors { corsCustomizer ->
            corsCustomizer.configurationSource(object : CorsConfigurationSource {
                override fun getCorsConfiguration(request: HttpServletRequest): CorsConfiguration {
                    val configuration = CorsConfiguration()

                    // 모든 출처 허용
                    configuration.addAllowedOriginPattern("*") // allowedOriginPatterns 사용
                    configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                    configuration.allowedHeaders = listOf("*")
                    configuration.allowCredentials = true // allowCredentials 설정
                    configuration.exposedHeaders = listOf("Authorization")
                    configuration.maxAge = 3600L

                    return configuration
                }
            })
        }
        return http.build()
    }
}
