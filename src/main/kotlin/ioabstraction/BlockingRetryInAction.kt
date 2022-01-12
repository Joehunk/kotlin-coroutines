package ioabstraction

import java.util.concurrent.Executors
import kotlin.time.ExperimentalTime
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import util.ThreadPool

@ExperimentalTime
object BlockingRetryInAction {
    // As expected, a blocking retry facility adapts to a blocking IO system just fine.
    fun blockingOuterCallBlockingIOCall(): Int {
        return BlockingRetry.exponentialBackoffRetry {
            IO.blockingIOCall()
        }
    }

    // Whereas CoroutineRetryInAction shows that we can use a coroutine based retry
    // facility in a blocking IO system (such as Bobcat) without changing the underlying behavior,
    // The reverse is not true: if we try to use a blocking retry facility in a coroutine based system, the leaky
    // abstraction of the blocking system changes the behavior.
    //
    // Even if we introduce a thread pool to try to handle all the Thread.sleep()s under the hood of the blocking
    // retry system, we still must force the IO call itself to block (which also consumes a thread pool thread).
    // So whereas a coroutine based retry facility has an abstraction that allows us to adapt it into both blocking
    // and non-blocking IO without bubbling the concept of "thread" (or generally how the code must execute and
    // be scheduled) out of the abstraction, a blocking facility does not allow this. We also lose the ability to
    // control how threading/execution of the code happens above this level.
    suspend fun nonBlockingOuterCallNonBlockingIOCall(): Int = withContext(ThreadPool.forBlockingCalls) {
        BlockingRetry.exponentialBackoffRetry {
            runBlocking {
                IO.nonBlockingIOCall()
            }
        }
    }
}