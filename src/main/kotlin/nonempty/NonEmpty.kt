package nonempty

import arrow.core.NonEmptyList
import arrow.core.nonEmptyListOf

fun getStuff() = listOf(1, 2, 3)

fun doStuffWithStuff() {
    val foo = getStuff()

    val bar = foo.first();
}