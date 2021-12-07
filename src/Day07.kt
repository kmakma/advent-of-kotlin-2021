import kotlin.math.abs

fun main() {
    fun getFuelConsumption(positions: List<Int>, target: Int): Int {
        return positions.sumOf { pos -> abs(pos - target) }
    }

    fun part1(input: List<Int>): Int {
        val min = input.minOrNull()!!
        val max = input.maxOrNull()!!
        var totalConsumption = Int.MAX_VALUE
        for (target in min..max) {
            val fuelConsumption = getFuelConsumption(input, target)
            if (fuelConsumption < totalConsumption) totalConsumption = fuelConsumption
        }
        return totalConsumption
    }

    fun getExpensiveFuelConsumption(positions: List<Int>, target: Int): Int {
        return positions.sumOf { pos ->
            val steps = abs(pos - target)
            return@sumOf steps * (steps + 1) / 2
        }
    }

    fun part2(input: List<Int>): Int {
        val min = input.minOrNull()!!
        val max = input.maxOrNull()!!
        var totalConsumption = Int.MAX_VALUE
        for (target in min..max) {
            val fuelConsumption = getExpensiveFuelConsumption(input, target)
            if (fuelConsumption < totalConsumption) totalConsumption = fuelConsumption
        }
        return totalConsumption
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test").first().split(",").map { it.toInt() }
    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    val input = readInput("Day07").first().split(",").map { it.toInt() }
    println(part1(input))
    println(part2(input))
}
