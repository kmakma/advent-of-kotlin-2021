fun main() {
    fun neighbours(element: Pair<Int, Int>): List<Pair<Int, Int>> {
        val x = element.first
        val y = element.second
        return listOf(x - 1 to y, x + 1 to y, x to y + 1, x to y - 1)

    }

    fun dijkstra(input: List<List<Int>>, pair: Pair<Int, Int>): Map<Pair<Int, Int>, Int> {
        val distances = mutableMapOf<Pair<Int, Int>, Int>()

        val queue = mutableMapOf<Pair<Int, Int>, Int>()
        distances[0 to 0] = 0
        queue[0 to 0] = distances[0 to 0]!!

        while (queue.isNotEmpty()) {
            val element = queue.minByOrNull { it.value }!!.key
            if (element == pair) break
            queue.remove(element)
            val value = distances[element]!!
            neighbours(element).forEach { neighbour ->
                val (x, y) = neighbour
                if (y < 0 || y > input.lastIndex || x < 0 || x > input[y].lastIndex) return@forEach // skip those not in bounds
                val newValue = value + input[y][x]
                if (newValue < distances.getOrDefault(neighbour, Int.MAX_VALUE)) {
                    distances[neighbour] = newValue
                    queue[neighbour] = newValue
                }
            }
        }

        return distances
    }

    fun part1(input: List<List<Int>>): Int {
        val targetY = input.lastIndex
        val targetX = input[targetY].lastIndex

        val distances = dijkstra(input, targetX to targetY)
        return distances[targetX to targetY]!!
    }


    fun extendLine(line: List<Int>): List<Int> {
        val extended = line.toMutableList()
        var increased = line
        repeat(4) {
            increased = increased.map { it.incWrap(9) }
            extended.addAll(increased)
        }
        return extended
    }

    fun extendLines(input: List<List<Int>>): List<List<Int>> {
        val extended = input.toMutableList()
        var increased = input
        repeat(4) {
            increased = increased.map { line -> line.map { num -> num.incWrap(9) } }
            extended.addAll(increased)
        }
        return extended
    }

    fun extend(input: List<List<Int>>): List<List<Int>> {
        return extendLines(input).map { line -> extendLine(line) }
    }

    fun part2(input: List<List<Int>>): Int {
        val extendedInput = extend(input)
        val targetY = extendedInput.lastIndex
        val targetX = extendedInput[targetY].lastIndex
        val distances = dijkstra(extendedInput, targetX to targetY)
        return distances[targetX to targetY]!!
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test").map { line -> line.map { c -> c.digitToInt() } }
    check(part1(testInput) == 40)
    check(part2(testInput) == 315)

    val input = readInput("Day15").map { line -> line.map { c -> c.digitToInt() } }
    println(part1(input))
    println(part2(input))
}

private fun Int.incWrap(wrap: Int): Int {
    val inc = inc()
    return if (inc > wrap) {
        (inc % (wrap + 1)) + 1
    } else {
        inc
    }
}
