package parallel

import arrow.fx.coroutines.*
import ioabstraction.IO

object CoroutineParallel {
    suspend fun tenIOCallsInParallel(): List<Int> {
        return (1..10).map { IO::nonBlockingIOCall }.parSequence()
    }
}
