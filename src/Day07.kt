typealias Operation = (Long, Long) -> Long

enum class Operators(val operation: Operation) {
    ADD({ a, b -> a + b }),
    MULTIPLY({ a, b -> a * b }),
    CONCATENATE({ a, b -> (a.toString() + b.toString()).toLong() });

    operator fun invoke(a: Long, b: Long) = operation(a, b)
}

fun List<Long>.combinations(start: Long, operators: Iterable<Operators>): List<Long> {
    val first = first()
    val variants = operators.map { operation ->
        operation(start, first)
    }
    return if (size == 1) {
        variants
    } else {
        val remaining = drop(1)
        variants.flatMap { remaining.combinations(it, operators) }
    }
}

fun List<Long>.combineTo(total: Long, operators: Iterable<Operators> = Operators.entries) =
    combinations(0, operators).contains(total)

fun main() {
    val input = readInput("day07_input").map {
        val (total, operands) = it.split(":")
        total.toLong() to operands.split(" ").drop(1).map(String::toLong)
    }

    val totalCalibrationAddOrMultiply = input
        .filter { (total, operands) ->
            operands.combineTo(total, listOf(Operators.ADD, Operators.MULTIPLY))
        }
        .sumOf { it.first }


    val totalCalibration = input
        .filter { (total, operands) ->
            operands.combineTo(total)
        }
        .sumOf { it.first }

    println("Total Calibration Result: $totalCalibrationAddOrMultiply")
    println("Total Calibration Result With Concatenation: $totalCalibration")
}