
val mulInstruction = """mul\((\d+),(\d+)\)""".toRegex()
val mulWithDoOrDont = """(do(?:n't)?\(\))|(?:mul\((\d+),(\d+)\))""".toRegex()

fun main() {
    val input = readInput("day03_input").joinToString()
    val total = mulInstruction.findAll(input)
        .map {
            val (f1, f2) = it.destructured
            f1.toInt() * f2.toInt()
        }
        .sum()

    var doIt = true
    val totalDoOrDont = mulWithDoOrDont.findAll(input)
        .map {
            val (doOrDont, f1, f2) = it.destructured
            if (!doOrDont.isNullOrBlank()) {
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
