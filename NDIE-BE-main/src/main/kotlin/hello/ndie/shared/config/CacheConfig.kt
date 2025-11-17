package hello.ndie.shared.config

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module
import hello.ndie.shared.cache.CacheNames
import org.springframework.cache.CacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import java.time.Duration

@Configuration
class CacheConfig(
    private val objectMapper: ObjectMapper
) {

    @Bean
    fun cacheManager(redisConnectionFactory: RedisConnectionFactory): CacheManager {
        val cacheObjectMapper = objectMapper.copy().apply {
            val typeValidator = LaissezFaireSubTypeValidator.instance
            activateDefaultTyping(typeValidator, ObjectMapper.DefaultTyping.EVERYTHING, JsonTypeInfo.As.PROPERTY)
            registerModule(
                Hibernate5Module().apply {
                    enable(Hibernate5Module.Feature.FORCE_LAZY_LOADING)
                    disable(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION)
                }
            )
        }

        val defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
            .disableCachingNullValues()
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    GenericJackson2JsonRedisSerializer(cacheObjectMapper)
                )
            )

        val documentsCache = defaultConfig.entryTtl(Duration.ofMinutes(5))
        val documentDetailsCache = defaultConfig.entryTtl(Duration.ofMinutes(10))

        return RedisCacheManager.builder(redisConnectionFactory)
            .cacheDefaults(defaultConfig)
            .withInitialCacheConfigurations(
                mapOf(
                    CacheNames.DOCUMENTS_BY_TYPE to documentsCache,
                    CacheNames.DOCUMENT_DETAILS to documentDetailsCache
                )
            )
            .build()
    }
}
