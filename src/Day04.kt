
// just be lazy and don't check for indexes; helps with readability ;)

// checks whether the string matches horizontally
fun Array<CharArray>.horizontalMatch(str: String, x: Int, y: Int): Boolean =
    str.withIndex().all { (i, c) -> this[y][x + i] == c }

// checks whether the string matches vertically
fun Array<CharArray>.verticalMatch(str: String, x: Int, y: Int): Boolean =
    str.withIndex().all { (i, c) -> this[y + i][x] == c }

// checks whether the string matches diagonally to the right
fun Array<CharArray>.diagMatch(str: String, x: Int, y: Int): Boolean =
    str.withIndex().all { (i, c) -> this[y + i][x + i] == c }

// checks whether the string matches diagonally to the left
fun Array<CharArray>.diagBackwardsMatch(str: String, x: Int, y: Int): Boolean =
    str.withIndex().all { (i, c) -> this[y + i][x - i] == c }

fun main() {
    val input = readInput("day04_input")

    // today, let's not use regex again but some traditional imperative loops for a change

    val blockLen = input.first().length
    val array = input.map { it.toCharArray() }.toTypedArray()

    fun Array<CharArray>.matchesAt(x: Int, y: Int): Int {
        var matches = 0
        arrayOf("XMAS", "SAMX").forEach { variant ->
            arrayOf(
                Array<CharArray>::horizontalMatch,
                Array<CharArray>::verticalMatch,
                Array<CharArray>::diagMatch,
                Array<CharArray>::diagBackwardsMatch
            ).forEach { direction ->
                try {
                    if (direction(this, variant, x, y)) matches++
                } catch (e: ArrayIndexOutOfBoundsException) {}
            }
        }
        return matches
    }

    var acc = 0;
    for (y in array.indices) {
        for (x in 0..<blockLen) {
            acc += array.matchesAt(x, y)
        }
    }
    println("There are $acc XMASes")
}
