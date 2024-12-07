fun Array<Int>.followsRule(rule: Pair<Int, Int>): Boolean =
    withIndex().all { (idx, value) ->
        (value != rule.second) || // it follows the rule if the second element doesn't appear at all
                // but if it does, the first element must either occur before
                ((0..<idx).any { get(it) == rule.first } ||
                        // or not at all
                        (idx..<size).none { get(it) == rule.first })
    }

fun Array<Int>.followsAllRules(rules: List<Pair<Int, Int>>) = rules.all(::followsRule)

fun Array<Int>.applyAllRules(rules: List<Pair<Int, Int>>): Array<Int> {
    // minor optimization as it cannot possibly violate any rule
    if (this.size < 2) {
        return this
    }

    // build the list of follower-numbers and the elements that should precede them
    return rules.fold(mutableMapOf<Int, MutableList<Int>>()) { acc, rule ->
        acc.also { it.getOrPut(rule.second) { mutableListOf() }.add(rule.first) }
    }.entries // then look
        .fold(this) { acc, (following, putBefore) ->
            // look for the element that should be following
            val atIndex = acc.indexOf(following)
            // don't have to mutate if it's not there or already at the end
            if (atIndex == -1 || atIndex == size - 1) {
                acc
            } else {
                // just extract all the elements that should be before this one
                val before = acc.slice(0..<atIndex)
                val after = acc.slice(atIndex + 1..<acc.size)
                val (violatingRule, fine) = after.partition { putBefore.contains(it) }
                (before + violatingRule.toTypedArray().applyAllRules(rules) + following + fine).toTypedArray()
            }
        }
}

fun List<Array<Int>>.middleSum() = sumOf { it[it.size / 2] }

fun main() {
    // a little bit of "cheating": use pre-separated input
    val rules = readInput("day05_rules").map { it.split("|").map(String::toInt) }.map { it[0] to it[1] }
    val instructions = readInput("day05_instructions").map { it.split(',').map(String::toInt).toTypedArray() }

    val (validInstructions, invalidInstructions) = instructions.partition { it.followsAllRules(rules) }
    val validSum = validInstructions.middleSum()
    println("Totals: $validSum")

    val correctedSum = invalidInstructions.map { it.applyAllRules(rules) }.middleSum()
    println("Totals fixed: $correctedSum")
}