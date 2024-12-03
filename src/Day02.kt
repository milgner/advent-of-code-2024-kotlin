data class Report(val levels: List<Int>) {
    enum class Direction { Increasing, Decreasing }

    private val direction: Direction by lazy {
        if (levels[0] > levels[1]) { Direction.Decreasing } else { Direction.Increasing }
    }

    private fun undampenedCheck(prev: Int, cur: Int): Boolean {
        val change = when (direction) {
            Direction.Increasing -> cur - prev
            Direction.Decreasing -> prev - cur
        }
        return (change in 1..3)
    }

    fun undampenedSafe(): Boolean {
        levels.reduce { prev, current ->
            if (!undampenedCheck(prev, current)) {
                return false
            }
            current
        }
        return true
    }

    fun <T> List<T>.exceptIndex(idx: Int) =
        slice(0..idx-1) + slice(idx+1..size-1)

    fun dampenedSafe() = undampenedSafe() || (0..levels.size-1).any { idx ->
        Report(levels.exceptIndex(idx)).undampenedSafe()
    }
}


fun main() {
    val input = readInput("day02_input")

    val reports = input.map { it.split(' ').map(String::toInt) }.map(::Report)
    val safeReports = reports.count { it.undampenedSafe() }
    val safeAfterDampening = reports.count { it.dampenedSafe() }
    println("There are $safeReports safe reports.")
    println("There are $safeAfterDampening safe reports after dampening.")
}
