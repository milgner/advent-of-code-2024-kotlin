enum class GuardDirection(val char: Char) {
    // guards turn by 90 degree, in the direction of the enum
    UP('^'), RIGHT('>'), DOWN('V'), LEFT('<'),
}

fun main() {
    val mapArray = readInput("day06_input").map { it.toCharArray() }.toTypedArray()
    val mapWidth = mapArray[0].size

    var guardPos: Pair<Int, Int>? = null

    fun Pair<Int, Int>.outOfBounds() = first < 0 || first == mapWidth || second < 0 || second == mapArray.size

    var guardDirection: GuardDirection? = null
    mapArray.forEachIndexed findPos@{ y, line ->
        line.forEachIndexed { x, c ->
            GuardDirection.entries.forEach { direction ->
                if (c == direction.char) {
                    guardPos = Pair(x, y)
                    guardDirection = direction
                    return@findPos
                }
            }
        }
    }

    while (true) {
        val newGuardPos = when (guardDirection) {
            GuardDirection.RIGHT -> {
                guardPos!!.first + 1 to guardPos!!.second
            }

            GuardDirection.UP -> {
                guardPos!!.first to guardPos!!.second - 1
            }

            GuardDirection.DOWN -> {
                guardPos!!.first to guardPos!!.second + 1
            }

            GuardDirection.LEFT -> {
                guardPos!!.first - 1 to guardPos!!.second
            }

            null -> null
        }
        if (newGuardPos == null || newGuardPos.outOfBounds()) {
            mapArray[guardPos!!.second][guardPos!!.first] = 'X'
            break
        }
        if (mapArray[newGuardPos.second][newGuardPos.first] == '#') {
            // need to turn
            val idx = GuardDirection.entries.indexOf(guardDirection)
            guardDirection = GuardDirection.entries[(idx + 1) % GuardDirection.entries.size]
        } else {
            mapArray[guardPos!!.second][guardPos!!.first] = 'X'
            mapArray[newGuardPos.second][newGuardPos.first] = guardDirection!!.char
            guardPos = newGuardPos
        }
    }

    val totalPassed = mapArray.sumOf { line -> line.count { it == 'X' } }
    println("Guard passed $totalPassed fields")
}