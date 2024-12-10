typealias Operation = (Long, Long) -> Long

enum class Operators(val operation: Operation) {
    ADD({ a, b -> a + b }),
    MULTIPLY({ a, b -> a * b });

    operator fun invoke(a: Long, b: Long) = operation(a, b)
}

fun List<Long>.combinations(start: Long): List<Long> {
    val first = first()
    val variants = Operators.entries.map { operation ->
        operation(start, first)
    }
    return if (size == 1) {
        variants
    } else {
        val remaining = drop(1)
        variants.flatMap { remaining.combinations(it) }
    }
}

fun List<Long>.combineTo(total: Long) = combinations(0).contains(total)

fun main() {
    val input = readInput("day07_input").map {
        val (total, operands) = it.split(":")
        total.toLong() to operands.split(" ").drop(1).map(String::toLong)
    }

    val totalCalibration = input
        .filter { (total, operands) ->
            operands.combineTo(total)
        }
        .sumOf { it.first }

    println("Total Calibration Result: $totalCalibration")
}