enum class GuardDirection(val char: Char) {
    // guards turn by 90 degree, in the direction of the enum
    UP('^'), RIGHT('>'), DOWN('V'), LEFT('<');

    fun turn(): GuardDirection = entries[(entries.indexOf(this) + 1) % GuardDirection.entries.size]
}

data class Map(val grid: Array<CharArray>) {
    lateinit var guardPosition: Pair<Int, Int>
    lateinit var guardDirection: GuardDirection

    val width = grid[0].size
    val height = grid.size

    init {
        grid.forEachIndexed findPos@{ y, line ->
            line.forEachIndexed { x, c ->
                GuardDirection.entries.forEach { direction ->
                    if (c == direction.char) {
                        guardPosition = Pair(x, y)
                        guardDirection = direction
                        markGrid(guardPosition, '.')
                        return@findPos
                    }
                }
            }
        }
        markGrid(guardPosition, '.')
    }

    val fieldAhead get() = fieldInDirection(guardDirection)

    fun withChangedField(position: Pair<Int, Int>, change: Char, block: () -> Unit) {
        val oldPos = guardPosition
        val oldDirection = guardDirection
        val currentField = grid[position.second][position.first]

        markGrid(position, change)
        block()
        this.guardPosition = oldPos
        this.guardDirection = oldDirection
        markGrid(position, currentField)
    }

    fun fieldInDirection(direction: GuardDirection): Pair<Int, Int> =
        when (direction) {
            GuardDirection.RIGHT -> {
                guardPosition.first + 1 to guardPosition.second
            }

            GuardDirection.UP -> {
                guardPosition.first to guardPosition.second - 1
            }

            GuardDirection.DOWN -> {
                guardPosition.first to guardPosition.second + 1
            }

            GuardDirection.LEFT -> {
                guardPosition.first - 1 to guardPosition.second
            }
        }

    // walk one step and return the new position
    fun step(): Boolean {
        while (true) {
            val next = fieldAhead
            if (outOfBounds(next)) {
                return false
            }
            if (grid[next.second][next.first] == '#') {
                guardDirection = guardDirection.turn()
                continue
            } else {
                guardPosition = next
                return true
            }
        }
    }

    fun outOfBounds(pos: Pair<Int, Int>) = pos.first < 0 || pos.first == width || pos.second < 0 || pos.second == height

    fun markGrid(pos: Pair<Int, Int>, c: Char) {
        grid[pos.second][pos.first] = c
    }
}

fun loadMapFromInput() = Map(readInput("day06_input").map { it.toCharArray() }.toTypedArray())

fun checkTrajectory() {
    val map = loadMapFromInput()
    do {
        map.markGrid(map.guardPosition, 'X')
    } while (map.step())

    val totalPassed = map.grid.sumOf { line -> line.count { it == 'X' } }
    println("Guard passed $totalPassed fields")
}

fun checkInfiniteLoops() {
    val map1 = loadMapFromInput()
    val map2 = loadMapFromInput()

    var obstacles = 0;

    for (x in 0..<map1.width) {
        for (y in 0..<map1.height) {
            if (map1.grid[y][x] == '#') {
                continue
            }
            map1.withChangedField(x to y, '#') {
                map2.withChangedField(x to y, '#') {
                    // with guard on map 1 travelling twice as fast as the guard on map 2,
                    // if they ever meet on the same field while facing in the same direction,
                    // there is an infinite loop
                    // but if the guard leaves map 1, there is no infinite loop
                    while (map1.step() && map1.step()) {
                        map2.step()
                        if (map1.guardPosition == map2.guardPosition && map1.guardDirection == map2.guardDirection) {
                            obstacles++
                            break
                        }
                    }
                }
            }
        }
    }

    println("Managed to set $obstacles obstacles for infinite loops")
}

fun main() {
    checkTrajectory()
    checkInfiniteLoops()
}