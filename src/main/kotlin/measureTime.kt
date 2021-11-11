data class MeasuredOperation<T>(val result: T, val nanoseconds: Long)

fun <T> measureTime(block: () -> T): MeasuredOperation<T> {
    val before = System.nanoTime()
    val returnValue = block()
    val after = System.nanoTime()

    return MeasuredOperation(returnValue, after - before)
}
