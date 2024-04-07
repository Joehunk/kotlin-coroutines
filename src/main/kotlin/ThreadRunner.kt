import kotlinx.coroutines.runBlocking
import java.io.Closeable

object ThreadRunner : Runner, Closeable {
    override fun runInParallel(parallelism: Int, iterations: Int, block: () -> Unit) {
        val threads = (1..parallelism).map {
            val thread = Thread {
                repeat(iterations) {
                    block()
                }
            }

            thread.start()
            thread
        }

        threads.forEach { it.join() }
    }

    override fun close() {
    }
}
