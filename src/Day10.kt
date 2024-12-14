class HikingMap(input: Array<CharArray>) : GridMap(input) {
    fun findTrailHeads() = search('0')

    private fun slowSlope(position: Position): List<Position> {
        val targetHeight = get(position).digitToInt() + 1
        if (targetHeight == 10) {
            return emptyList()
        }

        val result = mutableListOf<Position>()
        listOf(position.up, position.right, position.down, position.left).forEach { position ->
            if (outOfBounds(position) || get(position).digitToInt() != targetHeight) {
                return@forEach
            }
            result.add(position)
        }
        return result
    }

    private fun Position.ascendUntilNine(): List<Position> =
        slowSlope(this).fold(emptyList()) { acc, pos ->
            if (get(pos) == '9') {
                acc + pos
            } else {
                acc + pos.ascendUntilNine()
            }
        }

    fun rateTrailHead(position: Position): Int = position.ascendUntilNine().size

    fun scoreTrailHead(position: Position): Int = position.ascendUntilNine().toSet().size
}

fun main() {
    val input = HikingMap(readGridMapInput("day10_input"))

    val (totalScore, totalRating) = input.findTrailHeads()
        .fold(0 to 0) { (score, rating), th ->
            score + input.scoreTrailHead(th) to rating + input.rateTrailHead(th)
        }
    println("Map score: $totalScore, totalRating: $totalRating")
}
