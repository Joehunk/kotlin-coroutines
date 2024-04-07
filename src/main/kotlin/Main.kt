import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors

fun main(args: Array<String>) {
    runBlockingTest()

    CoroutineRunner.close()
    ThreadRunner.close()
}

fun simpleCoroutineTest() {
    val unprotectedThreadsResult = measureTime { runTest(ThreadRunner) }
    val protectedThreadsResult = measureTime { runWithProtectedThreads(ThreadRunner) }
    val coroutineResult = measureTime { runTest(CoroutineRunner) }

    printResult("Unprotected threads", unprotectedThreadsResult)
    printResult("Protected threads", protectedThreadsResult)
    printResult("Coroutines", coroutineResult)
}

fun runBlockingTest() {
    val executor = Executors.newFixedThreadPool(10)
    val result = measureTime {
        runBlocking(executor.asCoroutineDispatcher()) {
            (1..10).parallelForEach {
                Thread.sleep(2000)
                println("Routine $it done.")
            }
        }
    }
    executor.shutdown()

    println("Took ${"%.3f".format(result.milliseconds)}ms")
}

fun <T> printResult(operationName: String, result: MeasuredOperation<T>) {
    println("$operationName ran in ${"%.3f".format(result.milliseconds)} ms and returned ${result.result}")
}
