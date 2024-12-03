import kotlin.math.absoluteValue

// working with two lists of numbers that demarcate locations to search
data class Locations(val list1: List<Int>, val list2: List<Int>) {
    class Builder {
        private val list1 = mutableListOf<Int>()
        private val list2 = mutableListOf<Int>()

        fun addLine(n1: Int, n2: Int) = apply {
            list1.add(n1)
            list2.add(n2)
        }
        fun build() = Locations(list1.sorted(), list2.sorted())
    }

    // could have been `by lazy` as well - but it’s not really useful here as its needed only once
    val distance: Int get() = list1.foldIndexed(0) { index, acc, number ->
        acc + (number - list2[index]).absoluteValue
    }

    val similarity: Int get() = list1.fold(0) { acc, n1 ->
        acc + (list2.count { it == n1 } * n1)
    }
}

fun main() {
    val input = readInput("day01_input")

    val locations = input.fold(Locations.Builder()) { builder, line ->
        val parts = line.split(Regex("\\s+")).map(String::toInt)
        builder.addLine(parts[0], parts[1])
    }.build()

    println("Total distance: ${locations.distance}")
    println("Similarity score: ${locations.similarity}")
}
