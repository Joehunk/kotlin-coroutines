import java.util.concurrent.Executors
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.runBlocking
import java.io.Closeable

object CoroutineRunner: Runner, Closeable {
    private val executorService = Executors.newSingleThreadExecutor()
    private val dispatcher = executorService.asCoroutineDispatcher()

    private suspend fun runCoroutinesInParallel(parallelism: Int, iterations: Int, block: suspend () -> Unit) {
        (1..parallelism).parallelForEach {
            repeat(iterations) {
                block()
            }
        }
    }

    override fun runInParallel(parallelism: Int, iterations: Int, block: () -> Unit) {
        return runBlocking(dispatcher) {
            runCoroutinesInParallel(parallelism, iterations) {
                block()
            }
        }
    }

    override fun close() {
        executorService.shutdown()
    }
}
