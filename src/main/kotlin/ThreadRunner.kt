import kotlinx.coroutines.runBlocking

object ThreadRunner : Runner {
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
}
