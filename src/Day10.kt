fun main() {
    fun corruptionPoints(bracket: Char): Int {
        return when (bracket) {
            ')' -> 3
            ']' -> 57
            '}' -> 1197
            '>' -> 25137
            else -> throw IllegalArgumentException()
        }
    }

    fun part1(input: List<String>): Int {
        val illegalBrackets = mutableMapOf<Char, Int>()
        for (line in input) {
            val brackets = Brackets()
            for (bracket in line) {
                if (!brackets.parse(bracket)) {
                    illegalBrackets.merge(bracket, 1, Int::plus)
                    break
                }
            }
        }
        return illegalBrackets.map { (k, v) -> corruptionPoints(k) * v }.sum()
    }


    fun isNotCorrupted(line: String): Boolean {
        val brackets = Brackets()
        for (bracket in line) {
            if (!brackets.parse(bracket)) return false
        }
        return true
    }

    fun autocompletePoints(bracket: Char): Int {
        return when (bracket) {
            ')' -> 1
            ']' -> 2
            '}' -> 3
            '>' -> 4
            else -> throw IllegalArgumentException()
        }
    }

    fun autocompleteScore(autocompleted: String): Long {
        return autocompleted.fold(0L) { score, bracket -> score * 5L + autocompletePoints(bracket) }
    }

    fun part2(input: List<String>): Long {
        return input
            .filter { isNotCorrupted(it) }
            .map { Brackets().autocomplete(it) }
            .map { autocompleteScore(it) }
            .sorted()
            .let { scores -> scores[scores.size / 2 ] }
    }

    // TODO Idea: find all bracket pairs and replace them with empty string
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 26397)
    check(part2(testInput) == 288957L)

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}

class Brackets {
    private val opened = "([{<"
    private val closed = ")]}>"
    fun parse(bracket: Char): Boolean {
        return when {
            opened.contains(bracket) -> brackets.add(bracket)
            closed.contains(bracket) -> brackets.removeLast() == open(bracket)
            else -> throw IllegalArgumentException()
        }
    }

    private fun open(bracket: Char): Char {
        return when (bracket) {
            ')' -> '('
            ']' -> '['
            '}' -> '{'
            '>' -> '<'
            else -> throw IllegalArgumentException()
        }
    }

    fun autocomplete(line: String): String {
        for (bracket in line) {
            if (!parse(bracket)) throw IllegalStateException()
        }
        return brackets
            .reversed()
            .map { close(it) }
            .joinToString("")
    }

    private fun close(bracket: Char): Char {
        return when (bracket) {
            '(' -> ')'
            '[' -> ']'
            '{' -> '}'
            '<' -> '>'
            else -> throw IllegalArgumentException()
        }
    }

    private val brackets = mutableListOf<Char>()

}