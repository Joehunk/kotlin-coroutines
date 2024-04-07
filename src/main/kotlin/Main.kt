import arrow.core.Either
import arrow.core.mapOrAccumulate
import arrow.fx.coroutines.parMap
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.io.Closeable
import java.lang.RuntimeException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.time.DurationUnit
import kotlin.time.measureTime

fun <T : Closeable, U> Iterable<T>.useAll(block: () -> U) {
    try {
        block();
    } finally {
        mapOrAccumulate {
            Either.catch { it.close() }.bind()
        }.isLeft { it ->
            when(it.tail.isEmpty()) {
                true -> throw it.head
                false -> throw RuntimeException(
                    "Multiple failures: ${it.map { e -> e.message }.joinToString("\n")}",
                    it.head)
            }
        }
    }
}

fun main(args: Array<String>) {
    listOf(CoroutineRunner, ThreadRunner).useAll {
        runBlockingTest()
    }
}

fun simpleCoroutineTest() {
    val unprotectedThreadsResult = charlesMeasureTime { runTest(ThreadRunner) }
    val protectedThreadsResult = charlesMeasureTime { runWithProtectedThreads(ThreadRunner) }
    val coroutineResult = charlesMeasureTime { runTest(CoroutineRunner) }

    printResult("Unprotected threads", unprotectedThreadsResult)
    printResult("Protected threads", protectedThreadsResult)
    printResult("Coroutines", coroutineResult)
}

fun runBlockingTestWithMyMeasureTimeAndExecutor() {
    object : ExecutorService by Executors.newFixedThreadPool(10), Closeable {
        override fun close() {
            shutdown()
        }
    }.use {
        val (_, nanoseconds) = charlesMeasureTime {
            runBlocking(it.asCoroutineDispatcher()) {
                (1..10).parallelForEach {
                    Thread.sleep(2000)
                    println("Routine $it done.")
                }
            }
        }
        println("Took ${"%.3f".format(nanoseconds / 1000000.0)}ms")
    }
}

fun runBlockingTest() {
    val duration = measureTime {
        runBlocking {
            (1..10).parMap {
                delay(2000)
                println("Routine $it done.")
            }
        }
    }

    println("Took ${duration.toString(DurationUnit.MILLISECONDS, 3)}")
}

fun <T> printResult(operationName: String, result: MeasuredOperation<T>) {
    println("$operationName ran in ${"%.3f".format(result.milliseconds)} ms and returned ${result.result}")
}
