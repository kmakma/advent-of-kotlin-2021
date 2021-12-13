fun main() {

    fun part1(input: TransparentOrigami): Int {
        return input.foldFirst().size
    }

    fun part2(input: TransparentOrigami): Int {
        val mappedPoints = input.foldAll()
            .groupBy { p -> p.y }
            .mapValues { (_, pList) -> pList.map { it.x } }
        val maxX = mappedPoints.values.flatten().maxOf { it }
        val maxY = mappedPoints.keys.maxOf { it }
        for (y in 0..maxY) {
            var line = ""
            for (x in 0..maxX) {
                if (mappedPoints.getOrDefault(y, listOf()).contains(x)) {
                    line += "#"
                } else {
                    line += "."
                }
            }
            println(line)
        }
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = TransparentOrigami(readInput("Day13_test"))
    check(part1(testInput) == 17)
    // TODO check output should be an O
    check(part2(testInput) == 0)

    val input = TransparentOrigami(readInput("Day13"))
    println(part1(input))
    println(part2(input))
    // part2 output = AHPRPAUZ
}

class TransparentOrigami(input: List<String>) {
    val instructions = mutableListOf<Pair<String, Int>>()
    val points = mutableSetOf<Point>()

    init {
        for (line in input) {
            when {
                line.isBlank() -> {}
                line.startsWith("fold along ") -> parseInstruction(line)
                else -> parsePoint(line)
            }
        }
    }

    private fun parsePoint(line: String) {
        val point = line.split(",")
        points.add(Point(point[0].toInt(), point[1].toInt()))
    }

    private fun parseInstruction(line: String) {
        val instr = line.substring(11).split("=")
        instructions.add(instr[0] to instr[1].toInt())
    }

    fun foldFirst(): Set<Point> {
        return fold(instructions[0], points)
    }

    private fun fold(instruction: Pair<String, Int>, paper: Set<Point>): Set<Point> {
        return paper.map { p -> p.fold(instruction) }.toSet()
    }

    fun foldAll(): Set<Point> {
        return instructions.fold(points as Set<Point>) { p, i -> fold(i, p) }
    }

    data class Point(val x: Int, val y: Int) {
        fun fold(instruction: Pair<String, Int>): Point {
            return when (instruction.first) {
                "x" -> if (instruction.second > x) this else Point(x - 2 * (x - instruction.second), y)
                "y" -> if (instruction.second > y) this else Point(x, y - 2 * (y - instruction.second))
                else -> throw IllegalArgumentException()
            }
        }
    }
}
