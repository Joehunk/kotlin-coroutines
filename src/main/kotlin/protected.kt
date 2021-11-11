fun runWithProtectedThreads(runner: Runner): Int {
    var foo = 0
    val lock = Object()

    runner.runInParallel(128, 1000000) {
        synchronized(lock) {
            foo++
        }
    }
    return foo
}
