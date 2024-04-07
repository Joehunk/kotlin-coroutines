package ioabstraction
import arrow.fx.coroutines.Schedule
import arrow.fx.coroutines.retry
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.ExperimentalTime

object CoroutineRetry {
    @ExperimentalTime
    private val schedule = Schedule.exponential<Throwable>(250.milliseconds)

    @ExperimentalTime
    suspend fun <T> exponentialBackoffRetry(ioCall: suspend () -> T): T {
        return schedule.retry { ioCall() }
    }
}
