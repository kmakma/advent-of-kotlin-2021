fun main() {

    fun part1(input: Map<String, Cave>): Int {
        val queue = mutableListOf(listOf(input["start"]!!))
        val paths = mutableSetOf<List<Cave>>()
        while (queue.isNotEmpty()) {
            val path = queue.removeFirst()
            for (connection in path.last().connections) {
                when {
                    connection.id == "end" -> paths.add(path + connection)
                    connection.isSmall() -> {
                        if (!path.contains(connection)) {
                            queue.add(path + connection)
                        }
                    }
                    connection.isBig() -> queue.add(path + connection)
                }
            }
        }
        return paths.size
    }

    fun part2(input: Map<String, Cave>): Int {
        val queue = mutableListOf(listOf(input["start"]!!))
        val paths = mutableSetOf<List<Cave>>()
        while (queue.isNotEmpty()) {
            val path = queue.removeFirst()
            for (connection in path.last().connections) {
                when {
                    connection.id == "end" -> paths.add(path + connection)
                    connection.isSmall() -> {
                        val didDoubleVisit =
                            path.groupBy { it }
                                .any { (k, v) -> k.isSmall() && v.size > 1 }
                        if (!path.contains(connection) || !didDoubleVisit) {
                            queue.add(path + connection)
                        }
                    }
                    connection.isBig() -> queue.add(path + connection)
                }
            }
        }
//        println(paths.map { it.joinToString(",") { cave -> cave.id } }.sorted().joinToString("\n"))
        return paths.size
    }

// test if implementation meets criteria from the description, like:
    var testInput = buildGraph(readInput("Day12_test1"))
    check(part1(testInput) == 10)
    check(part2(testInput) == 36)
    testInput = buildGraph(readInput("Day12_test2"))
    check(part1(testInput) == 19)
    check(part2(testInput) == 103)
    testInput = buildGraph(readInput("Day12_test3"))
    check(part1(testInput) == 226)
    check(part2(testInput) == 3509)

    val input = buildGraph(readInput("Day12"))
    println(part1(input))
    println(part2(input))
    // TODO part2 is a bit slow (but there are > 110000 valid paths)
}

fun buildGraph(lines: List<String>): Map<String, Cave> {
    val graph = mutableMapOf<String, Cave>()
    for (line in lines) {
        val connection = line.split("-")
        val start = graph.getOrPut(connection[0]) { Cave(connection[0]) }
        val end = graph.getOrPut(connection[1]) { Cave(connection[1]) }
        start.connect(end)
        end.connect(start)
    }
    return graph
}

data class Cave(val id: String) {
    fun connect(to: Cave) {
        connections.add(to)
    }

    fun isSmall(): Boolean = isNotStart() && isNotEnd() && id.first().isLowerCase()

    private fun isNotStart(): Boolean = id != "start"
    private fun isNotEnd(): Boolean = id != "end"

    fun isBig(): Boolean = isNotStart() && isNotEnd() && id.first().isUpperCase()

    val connections = mutableSetOf<Cave>()

}
