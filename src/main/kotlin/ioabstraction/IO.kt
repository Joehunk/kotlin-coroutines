package ioabstraction

import kotlinx.coroutines.delay

object IO {
    fun blockingIOCall(): Int {
        Thread.sleep(1000)
        return 5
    }

    suspend fun nonBlockingIOCall(): Int {
        delay(1000)
        return 5
    }
}