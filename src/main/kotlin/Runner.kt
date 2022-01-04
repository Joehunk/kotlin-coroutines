interface Runner: AutoCloseable {
    fun runInParallel(parallelism: Int, iterations: Int, block: () -> Unit): Unit
}
