import kotlin.system.measureNanoTime

fun main() {


    fun part1(calls: List<Int>, boards: Boards): Int? {
        boards.reset()
        for (call in calls) {
            if (boards.markUntilFirst(call)) {
                return boards.finalScore(call)
            }
        }
        return null
    }

    fun part2(calls: List<Int>, boards: Boards): Int? {
        boards.reset()
        for (call in calls) {
            if (boards.markUntilLast(call)) {
                return boards.finalScore(call)
            }
        }
        return null
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    val testDrawnNum = testInput.first().split(",").map { it.toInt() }
    val testBingoBoards = Boards.parseBoards(testInput.subList(2, testInput.size))

    check(part1(testDrawnNum, testBingoBoards) == 4512)
    check(part2(testDrawnNum, testBingoBoards) == 1924)

    val input = readInput("Day04")
    val dayDrawnNum = input.first().split(",").map { it.toInt() }
    val dayBingoBoards = Boards.parseBoards(input.subList(2, input.size))
    println("time:" + measureNanoTime { println(part1(dayDrawnNum, dayBingoBoards)) } + "ns")
    println("time:" + measureNanoTime { println(part2(dayDrawnNum, dayBingoBoards)) } + "ns")
}

const val BINGO_SIZE = 5

class Boards(private val boards: List<BingoBoard>) {
    var winner: BingoBoard? = null
    var winnerList: MutableList<BingoBoard> = mutableListOf()

    fun markUntilFirst(drawn: Int): Boolean {
        for (board in boards) {
            board.mark(drawn)
            if (board.hasBingo()) {
                winner = board
                return true
            }
        }
        return winner != null
    }

    fun markUntilLast(drawn: Int): Boolean {
        for (board in boards) {
            board.mark(drawn)
            if (!winnerList.contains(board) && board.hasBingo()) {
                winnerList.add(board)
                winner = board
            }
            if (winnerList.size == boards.size) {
                return true
            }
        }
        return winnerList.size == boards.size
    }

    fun finalScore(drawn: Int): Int? {
        return winner?.finalScore(drawn)
    }

    fun reset() {
        boards.forEach { it.reset() }
        winner = null
        winnerList = mutableListOf()
    }

    companion object {
        fun parseBoards(stringBoards: List<String>): Boards {
            return Boards(stringBoards
                .filter { it != "" }
                .chunked(BINGO_SIZE)
                .map { BingoBoard.parseBoards(it) })

        }
    }
}

class BingoBoard(private val board: List<List<Int>>) {
    private var marked = MutableList(board.size) { idx -> MutableList(board[idx].size) { false } }

    fun mark(drawn: Int) {
        board.forEachIndexed { i, line -> line.forEachIndexed { j, num -> if (num == drawn) marked[i][j] = true } }
    }

    fun hasBingo(): Boolean {
        marked.forEach { line -> if (!line.contains(false)) return true }
        for (j in 0 until BINGO_SIZE) {
            for (i in 0 until BINGO_SIZE) {
                if (!marked[i][j]) break
                else if (i == BINGO_SIZE - 1) return true

            }
        }
        return false
    }

    fun finalScore(lastCalled: Int): Int {
        val flatMarked = marked.flatten()
        val sumNotCalled = board.flatten().filterIndexed { index, _ -> !flatMarked[index] }.sum()
        return lastCalled * sumNotCalled
    }

    fun reset() {
        marked = MutableList(board.size) { idx -> MutableList(board[idx].size) { false } }
    }

    companion object {
        fun parseBoards(stringBoard: List<String>): BingoBoard {
            return BingoBoard(stringBoard
                .map { line ->
                    line
                        .split(" ")
                        .filter { it.isNotBlank() }
                        .map { num -> num.toInt() }
                })
        }
    }
}
