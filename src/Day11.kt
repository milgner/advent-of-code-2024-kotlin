import kotlin.math.floor
import kotlin.math.log
import kotlin.math.pow

val Int.even: Boolean get() = this.mod(2) == 0

val Long.numOfDigits: Int get() = (floor(log(toDouble(), 10.0)) + 1).toInt()

fun List<Long>.blink(): List<Long> = flatMap {
    if (it == 0L) {
        listOf(1)
    } else if (it.numOfDigits.even) {
        val factor = 10.0.pow(it.numOfDigits/2).toLong()
        val leftSide = it.floorDiv(factor)
        val rightSide = it.rem(factor)
        listOf(leftSide, rightSide)
    } else {
        listOf(it * 2024)
    }
}


fun main() {
    val stones = readInput("day11_input").first().split(" ").map { it.toLong() }

    val numStones = (1..25).fold(stones) { blinked, _ ->
        blinked.blink()
    }.size
    println("Num of stones is $numStones")
}