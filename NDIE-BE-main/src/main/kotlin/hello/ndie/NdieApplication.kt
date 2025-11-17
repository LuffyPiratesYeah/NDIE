package hello.ndie

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class NdieApplication

fun main(args: Array<String>) {
    runApplication<NdieApplication>(*args)
}
