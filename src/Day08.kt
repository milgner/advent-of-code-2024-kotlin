fun GridMap.search(what: Char): List<Pair<Int, Int>> {
    val matches = mutableListOf<Pair<Int, Int>>()
    traverse { position ->
        if (get(position) == what) {
            matches.add(position)
        }
    }
    return matches
}

operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>) = (first + other.first) to (second + other.second)
operator fun Pair<Int, Int>.minus(other: Pair<Int, Int>) = (first - other.first) to (second - other.second)

fun GridMap.traverse(block: (Pair<Int, Int>) -> Unit) {
    for (y in 0..<height) {
        for (x in 0..<width) {
            block(x to y)
        }
    }
}

// let's not go overboard with a strategy pattern or coroutine here and do an ugly control couple instead 😜
enum class Behaviour {
    OnlyOne,
    AllMultiples
}

private fun GridMap.findAntinodes(behaviour: Behaviour): MutableSet<Pair<Int, Int>> {
    val antinodes = mutableSetOf<Pair<Int, Int>>()
    traverse { position ->
        val current = get(position)
        if (!current.isLetterOrDigit()) { // no antenna here
            return@traverse
        }
        search(current) // find other "antennas" of the same type
            .filterNot { it == position } // but not the current one
            .forEach { other ->
                val distance = position - other
                var candidate = position + distance
                while (true) {
                    if (inBounds(candidate)) {
                        if (behaviour == Behaviour.AllMultiples) {
                            antinodes.add(position)
                        }
                        antinodes.add(candidate)
                        candidate += distance
                    } else {
                        break
                    }
                    if (behaviour == Behaviour.OnlyOne) { break }
                }
            }
    }
    return antinodes
}

fun main() {
    val map = GridMap(readGridMapInput("day08_test"))

    val antinodes = map.findAntinodes(Behaviour.OnlyOne)

    println("Found ${antinodes.size} unique antinode locations")

    val allAntinodes = map.findAntinodes(Behaviour.AllMultiples) // 991 too low

    println("Found ${allAntinodes.size} antinode locations with repeated matches")
}
