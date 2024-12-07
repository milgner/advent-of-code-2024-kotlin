// just be lazy and don't check for indexes individually - helps with readability ;)
fun noAIOOBE(block: () -> Boolean) = try { block() } catch (e: ArrayIndexOutOfBoundsException) { false }

// checks whether the string matches horizontally
fun Array<CharArray>.horizontalMatch(str: String, x: Int, y: Int): Boolean = noAIOOBE {
    str.withIndex().all { (i, c) -> this[y][x + i] == c }
}

// checks whether the string matches vertically
fun Array<CharArray>.verticalMatch(str: String, x: Int, y: Int): Boolean = noAIOOBE {
    str.withIndex().all { (i, c) -> this[y + i][x] == c }
}

// checks whether the string matches diagonally to the right
fun Array<CharArray>.diagMatch(str: String, x: Int, y: Int): Boolean = noAIOOBE {
    str.withIndex().all { (i, c) -> this[y + i][x + i] == c }
}

// checks whether the string matches diagonally to the left
fun Array<CharArray>.diagBackwardsMatch(str: String, x: Int, y: Int): Boolean = noAIOOBE{
    str.withIndex().all { (i, c) -> this[y + i][x - i] == c }
}

fun Array<CharArray>.matchesAt(x: Int, y: Int): Int {
    var matches = 0
    arrayOf("XMAS", "SAMX").forEach { variant ->
        arrayOf(
            Array<CharArray>::horizontalMatch,
            Array<CharArray>::verticalMatch,
            Array<CharArray>::diagMatch,
            Array<CharArray>::diagBackwardsMatch
        ).forEach { direction ->
            if (direction(this, variant, x, y)) matches++
        }
    }
    return matches
}

fun Array<CharArray>.xedmasAt(x: Int, y: Int): Boolean =
    (diagMatch("MAS", x, y) || diagMatch("SAM", x, y)) && (
            diagBackwardsMatch("MAS", x + 2, y) || diagBackwardsMatch("SAM", x + 2, y))

fun main() {
    val input = readInput("day04_input")

    // today, let's not use regex again but some traditional imperative loops for a change

    val blockLen = input.first().length
    val array = input.map { it.toCharArray() }.toTypedArray()

    var xmases = 0
    var xedmases = 0
    for (y in array.indices) {
        for (x in 0..<blockLen) {
            xmases += array.matchesAt(x, y)
            if (array.xedmasAt(x, y)) { xedmases++ }
        }
    }
    println("There are $xmases XMASes")
    println("There are $xedmases XMASes")

}
