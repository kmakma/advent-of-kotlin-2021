fun main() {

    fun part1(input: List<String>): Int {
        return input
            .map { it.split(" | ")[1].split(" ") }
            .flatten()
            .map { it.length }
            .count { segments -> segments == 2 || segments == 4 || segments == 3 || segments == 7 }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { DisplayWiring.parseEntry(it) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 26)
    check(part2(testInput) == 61229)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}

class DisplayWiring(private val entry: String) {
    private val display: Display

    init {
        val displayBuilder = DisplayBuilder()
        entry.split(" | ", " ").forEach { displayBuilder.reduce(it) }
        display = displayBuilder.getDisplay()
    }

    fun getOutput(): Int {
        return entry.split(" | ")[1]
            .split(" ")
            .map { display.parseDigit(it) }
            .joinToString("")
            .toInt()
    }

    companion object {
        fun parseEntry(entry: String): Int = DisplayWiring(entry).getOutput()
    }
}

class DisplayBuilder {
    /**
     * Segments:
     *  0000
     * 5    1
     * 5    1
     *  6666
     * 4    2
     * 4    2
     *  3333
     */
    private val segments = (0..6).associateWith { allSegments() }

    private fun allSegments() = ('a'..'g').toMutableList()

    fun reduce(digit: String) {
        when (digit.length) {
            2 -> reduce1(digit)
            3 -> reduce7(digit)
            4 -> reduce4(digit)
            5 -> reduce235(digit)
            6 -> reduce069(digit)
            7 -> {}
            else -> throw IllegalArgumentException()
        }
    }

    private fun reduce1(digit: String) {
        (1..2).forEach { reduceSegmentExclusive(it, digit) }
        listOf(0, 3, 4, 5, 6).forEach { reduceSegmentNot(it, digit) }
    }

    private fun reduce7(digit: String) {
        (0..2).forEach { reduceSegmentExclusive(it, digit) }
        (3..6).forEach { reduceSegmentNot(it, digit) }
    }

    private fun reduce4(digit: String) {
        listOf(1, 2, 5, 6).forEach { reduceSegmentExclusive(it, digit) }
        listOf(0, 3, 4).forEach { reduceSegmentNot(it, digit) }
    }

    private fun reduce235(digit: String) {
        listOf(0, 3, 6).forEach { reduceSegmentExclusive(it, digit) }
    }

    private fun reduce069(digit: String) {
        listOf(0, 2, 3, 5).forEach { reduceSegmentExclusive(it, digit) }
    }

    private fun reduceSegmentNot(segment: Int, forbidden: String) {
        segments[segment]?.removeAll { forbidden.contains(it) }
    }

    private fun reduceSegmentExclusive(segment: Int, allowed: String) {
        segments[segment]?.removeAll { !allowed.contains(it) }
    }

    private fun basicReduction() {
        var changes = false
        do {
            segments.values
                .filter { it.size == 1 }
                .forEach {
                    changes = reduceRemaining(it)
                }
        } while (changes)
    }

    private fun reduceRemaining(char: List<Char>): Boolean {
        return segments.values
            .filter { it.size > 1 }
            .map { it.removeAll(char) }
            .contains(true)
    }

    fun getDisplay(): Display {
        basicReduction()
        segments.values.forEach { if (it.size != 1) throw IllegalStateException() }
        return Display(segments.map { (k, v) -> v.first() to k }.toMap())
    }


}

class Display(private val wiring: Map<Char, Int>) {
    fun parseDigit(digit: String): Char {
        val segments = digit.map { wiring[it] }
        if (segments.contains(null)) throw IllegalArgumentException()
        return when {
            segments.containsOnly(listOf(0, 1, 2, 3, 4, 5)) -> '0'
            segments.containsOnly(listOf(1, 2)) -> '1'
            segments.containsOnly(listOf(0, 1, 3, 4, 6)) -> '2'
            segments.containsOnly(listOf(0, 1, 2, 3, 6)) -> '3'
            segments.containsOnly(listOf(1, 2, 5, 6)) -> '4'
            segments.containsOnly(listOf(0, 2, 3, 5, 6)) -> '5'
            segments.containsOnly(listOf(0, 2, 3, 4, 5, 6)) -> '6'
            segments.containsOnly(listOf(0, 1, 2)) -> '7'
            segments.containsOnly(listOf(0, 1, 2, 3, 4, 5, 6)) -> '8'
            segments.containsOnly(listOf(0, 1, 2, 3, 5, 6)) -> '9'
            else -> throw IllegalArgumentException()
        }
    }

}

private fun <E> Collection<E>.containsOnly(elements: Collection<E>): Boolean {
    return this.containsAll(elements) && elements.containsAll(this)
}
