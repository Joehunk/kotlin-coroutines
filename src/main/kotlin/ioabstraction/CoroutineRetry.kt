package ioabstraction
import arrow.fx.coroutines.Schedule
import arrow.fx.coroutines.retry
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

object CoroutineRetry {
    @ExperimentalTime
    private val schedule = Schedule.exponential<Throwable>(Duration.milliseconds(250))

    @ExperimentalTime
    suspend fun <T> exponentialBackoffRetry(ioCall: suspend () -> T): T {
        return schedule.retry { ioCall() }
    }
}
