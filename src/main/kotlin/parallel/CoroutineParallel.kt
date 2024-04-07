package parallel

import arrow.fx.coroutines.*
import ioabstraction.IO
import kotlinx.coroutines.Dispatchers

object CoroutineParallel {
    suspend fun tenIOCallsInParallel(): List<Int> {
        return (1..10).map { IO::nonBlockingIOCall }.parMap { it() }
    }
}
