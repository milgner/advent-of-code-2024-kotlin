
const val MUL_INSTRUCTION = """mul\((\d+),(\d+)\)"""
const val DO_OR_MUL_INSTRUCTION = """(do(?:n't)?\(\))|(?:$MUL_INSTRUCTION)"""

fun main() {
    val input = readInput("day03_input").joinToString()
    val total = MUL_INSTRUCTION.toRegex().findAll(input)
        .map {
            val (f1, f2) = it.destructured
            f1.toInt() * f2.toInt()
        }
        .sum()

    var doIt = true
    val totalDoOrDont = DO_OR_MUL_INSTRUCTION.toRegex().findAll(input)
        .map {
            val (doOrDont, f1, f2) = it.destructured
            if (doOrDont.isNotBlank()) {
                doIt = doOrDont == "do()"
                0
            } else if (doIt) {
                f1.toInt() * f2.toInt()
            } else {
                0
            }
        }.sum()

    println("Total: $total")
    println("DoOrDont: $totalDoOrDont")
}
