fun main() {
    fun part1(input: Vents): Int {
        return input.withoutDiagonal().countOverlaps()
    }

    fun part2(input: Vents): Int {
        return input.countOverlaps()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = Vents.parseVents(readInput("Day05_test"))
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = Vents.parseVents(readInput("Day05"))
    println(part1(input))
    println(part2(input))
}

class Vents(private val lines: List<List<Int>>) {
    fun withoutDiagonal(): Vents {
        return Vents(lines.filter { line -> line[0] == line[2] || line[1] == line[3] })
    }

    fun countOverlaps(): Int {
        val vents = mutableMapOf<Pair<Int, Int>, Int>()
        for (line in lines) {
            for (vent in getVents(line)) {
                vents.merge(vent, 1, Int::plus)
            }
        }
        return vents.values.count { v -> v > 1 }
    }

    private fun getVents(line: List<Int>): List<Pair<Int, Int>> {
        return if (line[0] == line[2]) {
            val x = line[0]
            val yRange = if (line[1] < line[3]) line[1]..line[3] else line[1] downTo line[3]
            yRange.map { y -> Pair(x, y) }
        } else if (line[1] == line[3]) {
            val y = line[1]
            val xRange = if (line[0] < line[2]) line[0]..line[2] else line[0] downTo line[2]
            xRange.map { x -> Pair(x, y) }
        } else {
            val xRange = if (line[0] < line[2]) line[0]..line[2] else line[0] downTo line[2]
            val yRange = if (line[1] < line[3]) line[1]..line[3] else line[1] downTo line[3]
            xRange.zip(yRange)
        }
    }

    companion object {
        fun parseVents(input: List<String>): Vents {
            return Vents(input.map { line ->
                line.split(",", " -> ").map { coords -> coords.toInt() }
            })
        }
    }
}
