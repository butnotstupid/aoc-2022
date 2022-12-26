package days

class Day24 : Day(24) {
    override fun partOne(): Any {
        val colNum = inputList.first().length - 2
        val rowNum = inputList.size - 2
        val blizzards = parseBlizzards()

        val start = Point(-1, 0)
        val fin = Point(rowNum, colNum - 1)

        return route(start, fin, 0, blizzards, colNum, rowNum)
    }

    override fun partTwo(): Any {
        val colNum = inputList.first().length - 2
        val rowNum = inputList.size - 2
        val blizzards = parseBlizzards()

        val start = Point(-1, 0)
        val fin = Point(rowNum, colNum - 1)

        val thereTime = route(start, fin, 0, blizzards, colNum, rowNum)
        val backTime = route(fin, start, thereTime, blizzards, colNum, rowNum)
        return route(start, fin, backTime, blizzards, colNum, rowNum)
    }

    private fun route(
        start: Point,
        fin: Point,
        startTime: Int,
        blizzards: List<(Int) -> Point>,
        colNum: Int,
        rowNum: Int
    ): Int {
        return generateSequence(setOf(State(start, startTime))) { states ->
            val time = states.first().time
            states.flatMap { state ->
                listOf(
                    Point(0, 0),
                    Point(1, 0), Point(0, 1), Point(-1, 0), Point(0, -1)
                ).mapNotNull {
                    val next = state.point + it
                    if ((next == fin || state.point == next || (next.col in 0 until colNum && next.row in 0 until rowNum)) &&
                        blizzards.none { it(time + 1) == next }
                    ) next
                    else null
                }
            }.toSet().map { State(it, time + 1) }.toSet()
        }.takeWhile { it.none { it.point == fin } }.count() + startTime
    }

    private fun parseBlizzards(): List<(Int) -> Point> {
        val colNum = inputList.first().length - 2
        val rowNum = inputList.size - 2
        return inputList.flatMapIndexed { row, rowChars ->
            rowChars.mapIndexedNotNull { col, char ->
                when (char) {
                    '>' -> { time: Int -> Point(row - 1, (col - 1 + time) % colNum) }
                    '<' -> { time: Int -> Point(row - 1, (col - 1 + time * (colNum - 1)) % colNum) }
                    'v' -> { time: Int -> Point((row - 1 + time) % rowNum, col - 1) }
                    '^' -> { time: Int -> Point((row - 1 + time * (rowNum - 1)) % rowNum, col - 1) }
                    else -> null
                }
            }
        }
    }

//    private fun printPath(rowNum: Int, colNum: Int, blizzards: List<(Int) -> Point>, finState: State) {
//        val start = Point(-1, 0)
//        val fin = Point(rowNum, colNum - 1)
//        val path = generateSequence(finState) { it.prev }.takeWhile { true }.map { it.point }.toList().reversed()
//        for ((step, point) in path.withIndex()) {
//            for (row in -1..rowNum) {
//                for (col in -1..colNum) {
//                    if (Point(row, col) == point) print('E')
//                    else if (Point(row, col) == start || Point(row, col) == fin) print('.')
//                    else if (row !in 0 until rowNum || col !in 0 until colNum) print("#")
//                    else {
//                        print(blizzards.count { it(step) == Point(row, col) })
//                    }
//                }
//                println()
//            }
//        }
//    }

    data class Point(val row: Int, val col: Int) {
        operator fun plus(other: Point) = Point(row + other.row, col + other.col)
    }

    data class State(val point: Point, val time: Int) {
//        val prev: State?
    }
}
