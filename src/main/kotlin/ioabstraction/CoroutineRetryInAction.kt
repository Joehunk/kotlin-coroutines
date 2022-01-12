package ioabstraction

import kotlin.time.ExperimentalTime
import kotlinx.coroutines.runBlocking

object CoroutineRetryInAction {
    // Obviously a coroutine based retry facility adapts into a coroutine based IO call seamlessly, as expected.
    @ExperimentalTime
    suspend fun nonBlockingOuterCallNonBlockingIOCall(): Int {
        return CoroutineRetry.exponentialBackoffRetry {
            IO.nonBlockingIOCall()
        }
    }

    // But also, thanks to the better abstraction provided by coroutines, which separates the concerns
    // of what needs to run from how it runs, we can trivially adapt a reusable coroutine-based retry
    // facility into a blocking system without changing how it is threaded or scheduled, since the coroutine
    // abstraction is not tightly coupled to a threading model.
    @ExperimentalTime
    fun blockingOuterCallBlockingIOCall(): Int {
        return runBlocking {
            CoroutineRetry.exponentialBackoffRetry {
                IO.blockingIOCall()
            }
        }
    }
}
