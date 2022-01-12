package parallel

import ioabstraction.IO
import java.util.stream.Collectors

object BlockingParallel {
    fun tenIOCallsInParallel(): List<Int> {
        // At first glance this looks nice and abstract, but under the hood, parallelStream() is concretely
        // coupled to ForkJoinPool.common (the JVM global thread pool). There are ways to change this with
        // some ugly boilerplate: https://www.baeldung.com/java-8-parallel-streams-custom-threadpool
        // ...but even if you change it you are still required to use a concrete instance of a ForkJoinPool.
        return (1..10).toList().parallelStream().map { IO.blockingIOCall() }.collect(Collectors.toList())
    }
}