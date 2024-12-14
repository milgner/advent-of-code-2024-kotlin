fun Int.timesRepeat(block: () -> Unit) {
    for (i in 0..<this) block()
}

fun extractDiskLayout(input: String): List<Int?> {
    val result = mutableListOf<Int?>()
    Regex("(\\d)(\\d)?").findAll(input).forEachIndexed { idx, match ->
        val (size, space) = match.destructured
        size.toInt().timesRepeat {
            result.add(idx)
        }
        if (space.isNotBlank()) {
            space.toInt().timesRepeat { result.add(null) }
        }
    }
    return result
}

fun List<Int?>.defragment(): List<Int> {
    var base = this;
    while (true) {
        val lastNumber = base.indexOfLast { it != null }
        val firstBlank = base.indexOfFirst { it == null }
        if (firstBlank == -1) {
            return base as List<Int>
        }
        base = base.slice(0..<firstBlank) +
                base.get(lastNumber) +
                base.slice((firstBlank + 1)..<lastNumber)
    }
}

fun List<Int?>.intChecksum(): Long =
    foldIndexed(0) { idx, acc, c ->
        if (c == null) {
            return acc
        }
        acc + (idx * c)
    }

// better abstraction for part 2
data class FileElement(var idx: Int, var size: Int, var spaceAfter: Int) {
    val totalLength get() = size + spaceAfter
}

fun String.parseElements(): MutableList<FileElement> =
    Regex("(\\d)(\\d)?")
        .findAll(this)
        .foldIndexed(mutableListOf()) { idx, list, match ->
            val (size, space) = match.destructured
            list.add(FileElement(idx, size.toInt(), if (space.isNotBlank()) space.toInt() else 0))
            list
        }

fun MutableList<FileElement>.defragmentInBlocks(): List<FileElement> {
    for (idx in size - 1 downTo 1) {
        val elementIdx = indexOfLast { it.idx == idx }
        val element = get(elementIdx)

        val squeezeInAfter = indexOfFirst { it.spaceAfter >= element.size }
        if (squeezeInAfter == -1 || squeezeInAfter >= elementIdx) { continue }

        // gonna be moved, increase space after the preceding element
        get(elementIdx-1).spaceAfter += element.totalLength

        val squeezeInElement = get(squeezeInAfter)
        element.spaceAfter = squeezeInElement.spaceAfter - element.size
        squeezeInElement.spaceAfter = 0
        remove(element)
        add(squeezeInAfter + 1, element)
    }
    return this
}

fun List<FileElement>.checksum() = fold(0L to 0L) { (acc, len), e ->
    var elementTotal = 0L
    for (i in 0..< e.size) {
        elementTotal += e.idx * (len+i)
    }
    acc + elementTotal to len + e.totalLength
}.first

fun main() {
    val input = readInput("day09_input").first()

    val extract = extractDiskLayout(input)
    val defragmented = extract.defragment()
    println(defragmented.intChecksum())

    val part2 = input.parseElements()
    val blockDefragmented = part2.defragmentInBlocks()
    println(blockDefragmented.checksum()) // 5315515797220 too low

}