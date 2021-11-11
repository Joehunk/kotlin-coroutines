interface Runner {
    fun runInParallel(parallelism: Int, iterations: Int, block: () -> Unit): Unit
}
