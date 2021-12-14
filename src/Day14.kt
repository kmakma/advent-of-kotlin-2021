fun main() {
    fun part1(input: PolymerManual): Int {
        val finishedPolymer = input.steps(10)
        return finishedPolymer.toInt()

    }

    fun part2(input: PolymerManual): Long {
        return input.steps(40)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = PolymerManual(readInput("Day14_test"))
    check(part1(testInput) == 1588)
    check(part2(testInput) == 2188189693529L)

    val input = PolymerManual(readInput("Day14"))
    println(part1(input))
    println(part2(input))
}

class PolymerManual(readInput: List<String>) {
    private lateinit var polymerTemplate: String
    private val insertionRules = mutableMapOf<String, String>()

    init {
        readInput.forEach {
            when {
                it.contains(" -> ") -> parseInsertion(it)
                it.isBlank() -> {}
                else -> polymerTemplate = it
            }
        }
    }

    private fun parseInsertion(line: String) {
        val (pair, element) = line.split(" -> ")
        insertionRules[pair] = element
    }

    fun steps(steps: Int): Long {
        var result =
            polymerTemplate
                .zipWithNext()
                .groupingBy { it }
                .eachCount()
                .mapValues { it.value.toLong() }
        val counter = countedTemplateMap().toMutableMap()
        for (i in 1..steps) {
            result = step(result, counter)
        }
        return counter.values.let { values -> values.maxOf { it } - values.minOf { it } }
    }

    private fun countedTemplateMap(): Map<Char, Long> {
        return polymerTemplate.groupingBy { it }.eachCount().mapValues { it.value.toLong() }
    }

    private fun step(
        input: Map<Pair<Char, Char>, Long>,
        counter: MutableMap<Char, Long>
    ): Map<Pair<Char, Char>, Long> {
        val result = mutableMapOf<Pair<Char, Char>, Long>()

        input.forEach { (pair, count) ->
            val insert = insertionRules["${pair.first}${pair.second}"]!!.first()
            counter.merge(insert, count, Long::plus)
            val first = pair.first to insert
            val second = insert to pair.second
            result.merge(first, count, Long::plus)
            result.merge(second, count, Long::plus)
        }
        return result
    }

}
