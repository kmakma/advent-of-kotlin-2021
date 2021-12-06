fun main() {
    fun dayCycle(fishes: List<Int>): List<Int> {
        val newFish = mutableListOf<Int>()
        for (fish in fishes) {
            if (fish == 0) {
                newFish.add(6)
                newFish.add(8)
            } else {
                newFish.add(fish - 1)
            }
        }
        return newFish
    }

    fun part1(input: List<Int>): Int {
        var fish = input
        for (i in 1..80) {
            fish = dayCycle(fish)
        }
        return fish.size
    }

    fun dayCycle(fishes: Map<Int, Long>): Map<Int, Long> {
        val newFish = mutableMapOf<Int, Long>()
        for (fish in fishes) {
            if (fish.key == 0) {
                newFish.merge(6, fish.value, Long::plus)
                newFish.merge(8, fish.value, Long::plus)
            } else {
                newFish.merge(fish.key - 1, fish.value, Long::plus)
            }
        }
        return newFish
    }


    fun part2(input: List<Int>): Long {
        var fish = input.groupingBy { it }.eachCount().mapValues { it.value.toLong() }
        for (i in 1..256) {
            fish = dayCycle(fish)
        }
        return fish.values.sum()
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test").first().split(",").map { it.toInt() }
    check(part1(testInput) == 5934)
    check(part2(testInput) == 26984457539)

    val input = readInput("Day06").first().split(",").map { it.toInt() }
    println(part1(input))
    println(part2(input))
}

