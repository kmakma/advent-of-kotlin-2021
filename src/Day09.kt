fun main() {
    fun isLowPoint(input: List<List<Int>>, i: Int, j: Int): Boolean {
        val point = input[i][j]

        if (i > 0 && input[i - 1][j] <= point) return false
        if (i < input.size - 1 && input[i + 1][j] <= point) return false
        if (j > 0 && input[i][j - 1] <= point) return false
        if (j < input[i].size - 1 && input[i][j + 1] <= point) return false
        return true
    }

    fun part1(input: List<List<Int>>): Int {
        return input.mapIndexed { i, line ->
            line.filterIndexed { j, _ ->
                isLowPoint(input, i, j)
            }
        }
            .flatten()
            .sumOf { it.inc() }
    }

    fun <T> MutableSet<T>.pop(): T = this.first().also { this.remove(it) }

    fun basinNeighbours(input: List<List<Int>>, p: Pair<Int, Int>): List<Pair<Int, Int>> {
        val value = input[p.first][p.second]
        val neighbours = mutableListOf<Pair<Int, Int>>()
        if (p.first > 0 && input[p.first - 1][p.second] >= value && input[p.first - 1][p.second] < 9) neighbours.add(p.first - 1 to p.second)
        if (p.first < input.size - 1 && input[p.first + 1][p.second] >= value && input[p.first + 1][p.second] < 9) neighbours.add(
            p.first + 1 to p.second
        )
        if (p.second > 0 && input[p.first][p.second - 1] >= value && input[p.first][p.second - 1] < 9) neighbours.add(p.first to p.second - 1)
        if (p.second < input[p.first].size - 1 && input[p.first][p.second + 1] >= value && input[p.first][p.second + 1] < 9) neighbours.add(
            p.first to p.second + 1
        )
        return neighbours
    }

    fun basinSizeOf(input: List<List<Int>>, point: Pair<Int, Int>): Int {
        val basin = mutableSetOf(point)
        val check = mutableSetOf(point)
        while (check.isNotEmpty()) {
            val p = check.pop()
            basinNeighbours(input, p).forEach { if (basin.add(it)) check.add(it) }
        }
        return basin.size
    }

    fun part2(input: List<List<Int>>): Int {
        return input.mapIndexed { i, line ->
            List(line.size) { j -> i to j }.filter { isLowPoint(input, it.first, it.second) }
        }
            .flatten()
            .map { basinSizeOf(input, it) }
            .sortedDescending()
            .take(3)
            .reduce(Int::times)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test").map { it.map(Character::getNumericValue) }
    check(part1(testInput) == 15)
    check(part2(testInput) == 1134)

    val input = readInput("Day09").map { it.map(Character::getNumericValue) }
    println(part1(input))
    println(part2(input))
}
