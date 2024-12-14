
fun GridMap.search(predicate: (Char) -> Boolean): List<Position> {
    val matches = mutableListOf<Position>()
    traverse { position ->
        if (predicate(get(position))) matches.add(position)
    }
    return matches
}

fun GridMap.search(what: Char) = search { it == what }

operator fun Position.plus(other: Position) = Position(x + other.x, y + other.y)
operator fun Position.minus(other: Position) = Position(x - other.x, y - other.y)

fun GridMap.traverse(block: (Position) -> Unit) {
    for (y in 0..<height) {
        for (x in 0..<width) {
            block(Position(x, y))
        }
    }
}

// let's not go overboard with a strategy pattern or coroutine here and do an ugly control couple instead 😜
enum class Behaviour {
    OnlyOne,
    AllMultiples
}

private fun GridMap.findAntinodes(behaviour: Behaviour): MutableSet<Position> {
    val antinodes = mutableSetOf<Position>()
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
