import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

suspend fun <T, U> Iterable<T>.parallelMap(block: suspend (T) -> U): List<U> = coroutineScope {
    this@parallelMap.map { input ->
        async {
            block(input)
        }
    }.awaitAll()
}

suspend fun <T> Iterable<T>.parallelForEach(block: suspend (T) -> Unit): Unit = coroutineScope {
    this@parallelForEach.map { input ->
        async {
            block(input)
        }
    }.awaitAll()
}
