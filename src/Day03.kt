import kotlin.system.measureNanoTime

fun main() {
    fun part1(input: List<String>): Int {
        val binaries = input.map { it.toList().map { c -> c.digitToInt() } }
        val counted = binaries.reduce { acc, next -> acc.zip(next) { a, n -> a + n } }
        val gammaRate = counted.map { count -> if (count > input.size - count) '1' else '0' }.joinToString("").toInt(2)
        val epsilonRate =
            counted.map { count -> if (count < input.size - count) '1' else '0' }.joinToString("").toInt(2)
        return gammaRate * epsilonRate
    }

    fun filterGenNumbers(generatorNumbers: List<String>, genIdx: Int): List<String> {
        val count = generatorNumbers.count { it[genIdx] == '1' }
        val filter = if (count >= generatorNumbers.size - count) '1' else '0'
        return generatorNumbers.filter { it[genIdx] == filter }
    }

    fun filterScrNumbers(generatorNumbers: List<String>, genIdx: Int): List<String> {
        val count = generatorNumbers.count { it[genIdx] == '1' }
        val filter = if (count < generatorNumbers.size - count) '1' else '0'
        return generatorNumbers.filter { it[genIdx] == filter }
    }

    fun part2(input: List<String>): Int {
        val binaryLength = input[0].length
        var generatorNumbers = input
        var genIdx = 0
        while (generatorNumbers.size > 1 && genIdx < binaryLength) {
            generatorNumbers = filterGenNumbers(generatorNumbers, genIdx++)
        }
        var scrubberNumbers = input
        var scrIdx = 0
        while (scrubberNumbers.size > 1 && scrIdx < binaryLength) {
            scrubberNumbers = filterScrNumbers(scrubberNumbers, scrIdx++)
        }
        val generatorRating = Integer.parseInt(generatorNumbers.first(), 2)
        val scrubberRating = Integer.parseInt(scrubberNumbers.first(), 2)
        return generatorRating * scrubberRating

    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    println("time:" + measureNanoTime { println(part1(input)) } + "ns")
    println("time:" + measureNanoTime { println(part2(input)) } + "ns")
}
