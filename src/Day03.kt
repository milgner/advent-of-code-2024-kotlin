
val mulInstruction = """mul\((\d+),(\d+)\)""".toRegex()

fun main() {
    val input = readInput("day03_input").joinToString()
    val total = mulInstruction.findAll(input)
        .map {
            val (f1, f2) = it.destructured
            f1.toInt() * f2.toInt()
        }
        .sum()

    println("Total: $total")
}
