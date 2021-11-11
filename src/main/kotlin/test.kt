fun runTest(runner: Runner): Int {
    var foo = 0

    runner.runInParallel(128, 1000000) {
        foo++
    }
    return foo
}
