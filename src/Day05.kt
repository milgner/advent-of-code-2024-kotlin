fun List<Int>.followsRule(rule: Pair<Int, Int>): Boolean =
    withIndex().all { (idx, value) ->
        (value != rule.second) || // it follows the rule if the second element doesn't appear at all
                // but if it does, the first element must either occur before
                ((0..<idx).any { get(it) == rule.first } ||
                        // or not at all
                        (idx..<size).none { get(it) == rule.first })
    }

fun List<Int>.followsAllRules(rules: List<Pair<Int, Int>>) = rules.all(::followsRule)

fun main() {
    // a little bit of "cheating": use pre-separated input
    val rules = readInput("day05_rules").map { it.split("|").map(String::toInt) }.map { it[0] to it[1] }
    val instructions = readInput("day05_instructions").map { it.split(',').map(String::toInt) }

    val validInstructions = instructions.filter { it.followsAllRules(rules) }
    val middleElement = validInstructions.map { it[it.size / 2] }
    val totals = middleElement.sum()
    println("Totals: $totals")

}