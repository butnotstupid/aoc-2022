package days

import days.Day22.Direction.*
import kotlin.math.min

class Day22 : Day(22) {
    override fun partOne(): Any {
        val monkeyMap = TiledMap(inputList.dropLast(2).map { it.toCharArray() }.toTypedArray())
        val commands = parseCommands()
        val start = Point(0, inputList[0].indexOfFirst { it == '.' })

        val fin = commands.fold(Position(start, RIGHT)) { pos, command ->
            command.move(pos, monkeyMap)//.also { monkeyMap.chars.forEach { println(it.joinToString("")) } }
        }

        return with(fin) { (point.row + 1) * 1000 + (point.col + 1) * 4 + dir.code }
    }

    override fun partTwo(): Any {
        val monkeyMap = CubedMap(inputList.dropLast(2).map { it.toCharArray() }.toTypedArray())
        val commands = parseCommands()
        val start = Point(0, inputList[0].indexOfFirst { it == '.' })

        val fin = commands.fold(Position(start, RIGHT)) { pos, command ->
            command.move(pos, monkeyMap)//.also { monkeyMap.chars.forEach { println(it.joinToString("")) } }
        }

        return with(fin) { (point.row + 1) * 1000 + (point.col + 1) * 4 + dir.code }
    }

    private fun parseCommands(): List<Command> {
        return sequence {
            var curNum = 0
            inputList.last().forEach {
                if (it.isDigit()) curNum = curNum * 10 + it.digitToInt()
                else {
                    yield(Forward(curNum))
                    curNum = 0
                    yield(Turn(it))
                }
            }
            if (curNum > 0) yield(Forward(curNum))
        }.toList()
    }

    class Turn(private val turn: Char) : Command() {
        override fun move(pos: Position, monkeyMap: TiledMap): Position {
            return move(pos)
        }

        override fun move(pos: Position, monkeyMap: CubedMap): Position {
            return move(pos)
        }

        private fun move(pos: Position): Position {
            return when (turn) {
                'L' -> pos.copy(dir = pos.dir.leftTurn())
                'R' -> pos.copy(dir = pos.dir.rightTurn())
                else -> throw IllegalArgumentException("Unknown direction: $turn")
            }
        }
    }

    class Forward(private val steps: Int) : Command() {
        override fun move(pos: Position, monkeyMap: TiledMap): Position {
            val columns = monkeyMap.rowDim[pos.point.row]
            val rows = monkeyMap.colDim[pos.point.col]
            return generateSequence(pos) { cur ->
                cur.moveForward().let {
                    when {
                        (it.point.col > columns.last) -> it.copy(point = it.point.copy(col = columns.first))
                        (it.point.col < columns.first) -> it.copy(point = it.point.copy(col = columns.last))
                        (it.point.row > rows.last) -> it.copy(point = it.point.copy(row = rows.first))
                        (it.point.row < rows.first) -> it.copy(point = it.point.copy(row = rows.last))
                        else -> it
                    }
                }.let {
                    if (monkeyMap.chars[it.point.row][it.point.col] != '#') it
                    else cur
                }.also { monkeyMap.chars[it.point.row][it.point.col] = it.dir.asChar() }
            }.take(steps + 1).last()
        }

        override fun move(pos: Position, monkeyMap: CubedMap): Position {
            return generateSequence(pos) { cur ->
                cur.moveForward().let {
                    when {
                        (it.point.col !in monkeyMap.rowDim[cur.point.row] || it.point.row !in monkeyMap.colDim[cur.point.col]) ->
                            monkeyMap.moveOverboard(cur)
                        else -> it
                    }
                }.let {
                    if (monkeyMap.chars[it.point.row][it.point.col] != '#') it
                    else cur
                }.also { monkeyMap.chars[it.point.row][it.point.col] = it.dir.asChar() }
            }.take(steps + 1).last()
        }

    }

    class TiledMap(val chars: Array<CharArray>) {
        val rowDim = chars.map {
            val (left, right) = it.indexOfFirst { it != ' ' } to it.size - 1
            left..right
        }
        val colDim = (0..chars.maxOf { it.size - 1 }).map { col ->
            chars.withIndex()
                .mapNotNull { (index, row) -> if (col < row.size && row[col] != ' ') index else null }
                .let { it.min()..it.max() }
        }
    }

    class CubedMap(val chars: Array<CharArray>) {
        private val sideSize = min(chars.first().size, chars.size) / 3
        private val folding = arrayOf(
            arrayOf(0, 1, 2),
            arrayOf(0, 3, 0),
            arrayOf(4, 5, 0),
            arrayOf(6, 0, 0)
        )

        private fun side(point: Point): Int {
            return with(point) {
                when (row) {
                    in 0 until sideSize ->
                        if (col < 2 * sideSize) 1 else 2
                    in sideSize until 2 * sideSize ->
                        3
                    in 2 * sideSize until 3 * sideSize ->
                        if (col < sideSize) 4 else 5
                    else -> 6
                }
            }
        }

        fun moveOverboard(pos: Position): Position {
            val (nextFunc, sideNumber) = overboardMap[side(pos.point) to pos.dir]!!
            val (rk, ck) = folding.mapIndexedNotNull { rowNum, row ->
                row.mapIndexedNotNull { colNum, elem ->
                    if (elem == sideNumber) (rowNum to colNum) else null
                }.firstOrNull()
            }.first()

            return nextFunc(Point(pos.point.row % sideSize, pos.point.col % sideSize)).let { next: Position ->
                with(next.point) {
                    next.copy(point = Point(
                        rk * sideSize + row,
                        ck * sideSize + col
                    ))
                }
            }
        }

        private val overboardMap = mapOf(
            1 to LEFT to ({ it: Point -> Position(Point(sideSize - 1 - it.row, it.col), RIGHT) } to 4),
            4 to LEFT to ({ it: Point -> Position(Point(sideSize - 1 - it.row, it.col), RIGHT) } to 1),

            1 to UP to ({ it: Point -> Position(Point(it.col, it.row), RIGHT) } to 6),
            6 to LEFT to ({ it: Point -> Position(Point(it.col, it.row), DOWN) } to 1),

            2 to UP to ({ it: Point -> Position(Point(sideSize - 1 - it.row, it.col), UP) } to 6),
            6 to DOWN to ({ it: Point -> Position(Point(sideSize - 1 - it.row, it.col), DOWN) } to 2),

            3 to LEFT to ({ it: Point -> Position(Point(it.col, it.row), DOWN) } to 4),
            4 to UP to ({ it: Point -> Position(Point(it.col, it.row), RIGHT) } to 3),

            3 to RIGHT to ({ it: Point -> Position(Point(it.col, it.row), UP) } to 2),
            2 to DOWN to ({ it: Point -> Position(Point(it.row, it.col), LEFT) } to 3),

            5 to RIGHT to ({ it: Point -> Position(Point(sideSize - 1 - it.row, it.col), LEFT) } to 2),
            2 to RIGHT to ({ it: Point -> Position(Point(sideSize - 1 - it.row, it.col), LEFT) } to 5),

            5 to DOWN to ({ it: Point -> Position(Point(it.col, it.row), LEFT) } to 6),
            6 to RIGHT to ({ it: Point -> Position(Point(it.col, it.row), UP) } to 5),
        )

        val rowDim = chars.map {
            val (left, right) = it.indexOfFirst { it != ' ' } to it.size - 1
            left..right
        }
        val colDim = (0..chars.maxOf { it.size - 1 }).map { col ->
            chars.withIndex()
                .mapNotNull { (index, row) -> if (col < row.size && row[col] != ' ') index else null }
                .let { it.min()..it.max() }
        }
    }

    abstract class Command {
        abstract fun move(pos: Position, monkeyMap: TiledMap): Position
        abstract fun move(pos: Position, monkeyMap: CubedMap): Position
    }

    data class Position(val point: Point, val dir: Direction) {
        fun moveForward() = this.copy(
            point = with(this.point) {
                when (dir) {
                    LEFT -> Point(row, col - 1)
                    RIGHT -> Point(row, col + 1)
                    UP -> Point(row - 1, col)
                    DOWN -> Point(row + 1, col)
                }
            }
        )
    }

    data class Point(val row: Int, val col: Int)
    enum class Direction(val code: Int) {
        RIGHT(0), DOWN(1), LEFT(2), UP(3);

        fun rightTurn(): Direction {
            return values().find { it.code == (this.code + 1) % 4 }!!
        }

        fun leftTurn(): Direction {
            return values().find { it.code == (this.code + 3) % 4 }!!
        }

        fun asChar(): Char {
            return when (this) {
                RIGHT -> '>'
                DOWN -> 'v'
                LEFT -> '<'
                UP -> '^'
            }
        }
    }
}
