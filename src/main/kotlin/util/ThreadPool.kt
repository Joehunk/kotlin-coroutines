package util

import java.util.concurrent.Executors
import kotlinx.coroutines.asCoroutineDispatcher

object ThreadPool {
    private val executorService = Executors.newFixedThreadPool(32)

    init {
        Runtime.getRuntime().addShutdownHook(Thread {
            executorService.shutdown()
        })
    }

    val forBlockingCalls = executorService.asCoroutineDispatcher()
}
