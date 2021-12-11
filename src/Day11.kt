fun main() {

    fun flashOctopuses(octopuses: MutableMap<Coordinates, Int>): Int {
        val flashed = mutableSetOf<Coordinates>()
        octopuses.replaceAll { _, v -> v + 1 }
        while (octopuses.values.any { v -> v > 9 }) {
            octopuses
                .filterValues { v -> v > 9 }
                .keys
                .forEach { flasher ->
                    octopuses[flasher] = 0
                    flashed.add(flasher)
                    flasher.neighbours()
                        .filter { octopuses.containsKey(it) && !flashed.contains(it) }
                        .forEach { octopuses.merge(it, 1, Int::plus) }
                }

        }
        return flashed.size
    }

    fun part1(input: Map<Coordinates, Int>): Int {
        val octopuses = input.toMutableMap()
        var flashCounter = 0
        repeat(100) {
            flashCounter += flashOctopuses(octopuses)
        }
        return flashCounter
    }

    fun part2(input: Map<Coordinates, Int>): Int {
        val octopuses = input.toMutableMap()
        var steps = 0
        while (octopuses.values.any { v -> v != 0 }) {
            flashOctopuses(octopuses)
            steps++
        }
        return steps
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test").mapIndexed { i, line ->
        line.mapIndexed { j, char ->
            Coordinates(i, j) to char.digitToInt()
        }
    }.flatten().toMap()
    check(part1(testInput) == 1656)
    check(part2(testInput) == 195)

    val input = readInput("Day11").mapIndexed { i, line ->
        line.mapIndexed { j, char ->
            Coordinates(i, j) to char.digitToInt()
        }
    }.flatten().toMap()
    println(part1(input))
    println(part2(input))
}

data class Coordinates(val x: Int, val y: Int) {
    fun neighbours(): List<Coordinates> {
        return (-1..1).flatMap { i ->
            (-1..1).mapNotNull { j -> if (i == 0 && j == 0) null else Coordinates(x + i, y + j) }
        }
    }
}
