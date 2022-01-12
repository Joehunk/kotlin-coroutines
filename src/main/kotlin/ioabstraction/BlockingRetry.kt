package ioabstraction

import arrow.core.NonFatal
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@ExperimentalTime
object BlockingRetry {
    private val initialDelay = Duration.milliseconds(250)

    fun <T> exponentialBackoffRetry(ioCall: () -> T): T {
        var delay = initialDelay

        while (true) {
            try {
                // This is a less obvious place where the abstraction leaks. Not only does this utility block,
                // it also assumes your IO call blocks as well, whereas the coroutine version works with any case,
                // blocking, non-blocking (callback based), thread pool, etc.
                ioCall()
            } catch (e: Throwable) {
                if (NonFatal(e)) {
                    // This is where the abstraction leaks. Instead of saying "wake up after N milliseconds"
                    // in an abstract way, this code concretely says "tell the OS to block the current OS thread for
                    // n milliseconds", which tightly couples this abstraction to a concurrency/scheduling
                    // implementation.
                    Thread.sleep(delay.inWholeMilliseconds)
                    delay *= 2
                } else {
                    throw e
                }
            }
        }
    }
}
