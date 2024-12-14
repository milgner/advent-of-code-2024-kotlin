enum class GuardDirection(val char: Char) {
    // guards turn by 90 degree, in the direction of the enum
    UP('^'), RIGHT('>'), DOWN('V'), LEFT('<');

    fun turn(): GuardDirection = entries[(entries.indexOf(this) + 1) % GuardDirection.entries.size]
}

data class Position(val x: Int, val y: Int) {
    val left get() = Position(x-1, y)
    val right get() = Position(x+1, y)
    val up get() = Position(x, y - 1)
    val down get() = Position(x, y+1)
}

open class GridMap(val grid: Array<CharArray>) {
    val width = grid[0].size
    val height = grid.size

    fun outOfBounds(pos: Position) = pos.x < 0 ||
            pos.x >= width ||
            pos.y < 0 ||
            pos.y >= height
    fun inBounds(pos: Position) = !outOfBounds(pos)

    operator fun get(pos: Position) = grid[pos.y][pos.x]
    operator fun set(pos: Position, value: Char) { grid[pos.y][pos.x] = value }
}

fun readGridMapInput(filename: String) = readInput(filename).map { it.toCharArray() }.toTypedArray()

class GuardMap(grid: Array<CharArray>) : GridMap(grid) {
    lateinit var guardPosition: Position
    lateinit var guardDirection: GuardDirection

    companion object {
        fun loadFromInput() = GuardMap(readGridMapInput("day06_input"))
    }

    init {
        grid.forEachIndexed findPos@{ y, line ->
            line.forEachIndexed { x, c ->
                GuardDirection.entries.forEach { direction ->
                    if (c == direction.char) {
                        guardPosition = Position(x, y)
                        guardDirection = direction
                        set(guardPosition, '.')
                        return@findPos
                    }
                }
            }
        }
        set(guardPosition, '.')
    }

    fun withChangedField(position: Position, change: Char, block: () -> Unit) {
        val oldPos = guardPosition
        val oldDirection = guardDirection
        val currentField = get(position)

        set(position, change)
        block()
        this.guardPosition = oldPos
        this.guardDirection = oldDirection
        set(position, currentField)
    }

    fun fieldInDirection(direction: GuardDirection): Position =
        when (direction) {
            GuardDirection.RIGHT -> guardPosition.right
            GuardDirection.UP -> guardPosition.up
            GuardDirection.DOWN -> guardPosition.down
            GuardDirection.LEFT -> guardPosition.left
        }

    val fieldAhead get() = fieldInDirection(guardDirection)

    // walk one step and return the new position
    fun step(): Boolean {
        while (true) {
            val next = fieldAhead
            if (outOfBounds(next)) {
                return false
            }
            if (get(next) == '#') {
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
            map1.withChangedField(Position(x,y), '#') {
                map2.withChangedField(Position(x, y), '#') {
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