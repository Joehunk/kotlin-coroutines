fun main(args: Array<String>) {
    val unprotectedThreadsResult = measureTime { runTest(ThreadRunner) }
    val protectedThreadsResult = measureTime { runWithProtectedThreads(ThreadRunner) }
    val coroutineResult = measureTime { runTest(CoroutineRunner) }

    printResult("Unprotected threads", unprotectedThreadsResult)
    printResult("Protected threads", protectedThreadsResult)
    printResult("Coroutines", coroutineResult)
}

fun printResult(operationName: String, result: MeasuredOperation<Int>) {
    val milliseconds = result.nanoseconds.toDouble() / 1000000.0

    println("$operationName ran in ${"%.3f".format(milliseconds)} ms and returned ${result.result}")
}
