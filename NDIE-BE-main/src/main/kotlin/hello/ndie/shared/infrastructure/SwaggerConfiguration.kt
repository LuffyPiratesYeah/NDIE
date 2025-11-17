package kr.co.allergicv2.infrastructure

import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfiguration {

    @Bean
    fun chatOpenApi(): GroupedOpenApi {
        val paths = arrayOf("/**")

        return GroupedOpenApi.builder()
            .group("NDIE")
            .pathsToMatch(*paths)
            .build()
    }
}
