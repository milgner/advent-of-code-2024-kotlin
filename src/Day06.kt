enum class GuardDirection(val char: Char) {
    // guards turn by 90 degree, in the direction of the enum
    UP('^'), RIGHT('>'), DOWN('V'), LEFT('<');

    fun turn(): GuardDirection = entries[(entries.indexOf(this) + 1) % GuardDirection.entries.size]
}

open class GridMap(val grid: Array<CharArray>) {
    val width = grid[0].size
    val height = grid.size

    fun outOfBounds(pos: Pair<Int, Int>) = pos.first < 0 ||
            pos.first >= width ||
            pos.second < 0 ||
            pos.second >= height
    fun inBounds(pos: Pair<Int, Int>) = !outOfBounds(pos)

    operator fun get(pos: Pair<Int, Int>) = grid[pos.second][pos.first]
    operator fun set(pos: Pair<Int, Int>, value: Char) { grid[pos.second][pos.first] = value }
}

fun readGridMapInput(filename: String) = readInput(filename).map { it.toCharArray() }.toTypedArray()

class GuardMap(grid: Array<CharArray>) : GridMap(grid) {
    lateinit var guardPosition: Pair<Int, Int>
    lateinit var guardDirection: GuardDirection

    companion object {
        fun loadFromInput() = GuardMap(readGridMapInput("day06_input"))
    }

    init {
        grid.forEachIndexed findPos@{ y, line ->
            line.forEachIndexed { x, c ->
                GuardDirection.entries.forEach { direction ->
                    if (c == direction.char) {
                        guardPosition = Pair(x, y)
                        guardDirection = direction
                        set(guardPosition, '.')
                        return@findPos
                    }
                }
            }
        }
        set(guardPosition, '.')
    }

    fun withChangedField(position: Pair<Int, Int>, change: Char, block: () -> Unit) {
        val oldPos = guardPosition
        val oldDirection = guardDirection
        val currentField = grid[position.second][position.first]

        set(position, change)
        block()
        this.guardPosition = oldPos
        this.guardDirection = oldDirection
        set(position, currentField)
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

    val fieldAhead get() = fieldInDirection(guardDirection)

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
}

fun checkTrajectory() {
    val map = GuardMap.loadFromInput()
    do {
        map[map.guardPosition] = 'X'
    } while (map.step())

    val totalPassed = map.grid.sumOf { line -> line.count { it == 'X' } }
    println("Guard passed $totalPassed fields")
}

fun checkInfiniteLoops() {
    val map1 = GuardMap.loadFromInput()
    val map2 = GuardMap.loadFromInput()

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