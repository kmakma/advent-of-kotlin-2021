fun main() {
    fun part1(input: List<Pair<String, Int>>): Int {
        var depth = 0
        var horiz = 0
        for (command in input) {
            when (command.first) {
                "forward" -> horiz += command.second
                "down" -> depth += command.second
                "up" -> depth -= command.second
            }
        }
        return depth * horiz
    }

    fun part1grouped(input: List<Pair<String, Int>>): Int {
        val grouped = input.groupBy({ it.first }, { it.second }).mapValues { it.value.sum() }
        return grouped.getOrDefault("forward", 0) * (grouped.getOrDefault("down", 0) - grouped.getOrDefault("up", 0))
    }

    fun part2(input: List<Pair<String, Int>>): Int {
        var depth = 0
        var horiz = 0
        var aim = 0
        for (command in input) {
            when (command.first) {
                "forward" -> {
                    horiz += command.second
                    depth += aim * command.second
                }
                "down" -> aim += command.second
                "up" -> aim -= command.second
            }
        }
        return depth * horiz
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test").map {
        val split = it.split(' ', limit = 2)
        split[0] to split[1].toInt()
    }
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = readInput("Day02").map {
        val split = it.split(' ', limit = 2)
        split[0] to split[1].toInt()
    }
    println(part1(input))
    println(part2(input))
}
