import kotlin.math.absoluteValue

fun main() {
    val input = readInput("day01_input")

    val lists = input.map {
        val parts = it.split(Regex("\\s+"))
        parts[0].toInt() to parts[1].toInt()
    }
    val numbers1 = lists.map { it.first }.sorted()
    val numbers2 = lists.map { it.second }.sorted()

    val distance = numbers1.foldIndexed(0) { index, acc, number ->
        acc + (number - numbers2[index]).absoluteValue
    }

    val similarity = numbers1.fold(0) { acc, n1 ->
        acc + (numbers2.count { it == n1 } * n1)
    }

    println("Total distance: $distance")
    println("Similarity score: $similarity")
}
