package hello.ndie.shared.config

import jakarta.annotation.PreDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.springframework.stereotype.Component
import kotlin.coroutines.CoroutineContext

/**
 * Dedicated coroutine scope for fire-and-forget background jobs such as email sending.
 * The supervisor job prevents sibling failures from cancelling other jobs, and the IO
 * dispatcher keeps the work off the main thread.
 */
@Component
class EmailCoroutineScope : CoroutineScope {
    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext = Dispatchers.IO + job

    @PreDestroy
    fun shutdown() {
        job.cancel()
    }
}
