package days

class Day14 : Day(14) {
    private val margin = 1000
    private val mapSize = 3 * margin
    private val sandPoint = Point(0, 500).withMargin(margin)

    override fun partOne(): Any {
        val map = Array(mapSize) { CharArray(mapSize) { '.' } }
            .also { it[sandPoint.row][sandPoint.col] = '+' }
        parseShapes().forEach { it.traceOnMap(map) }
        safetyNet(map).traceOnMap(map, '=')

        return generateSequence {
            sand(sandPoint, map).also { map[it.row][it.col] = 'o'  }
        }
            .takeWhile { map[it.row + 1][it.col] != '=' }
            .count()
    }

    override fun partTwo(): Any {
        val map = Array(mapSize) { CharArray(mapSize) { '.' } }
            .also { it[sandPoint.row][sandPoint.col] = '+' }
        parseShapes().forEach { it.traceOnMap(map) }
        safetyNet(map).traceOnMap(map, '=')

        return generateSequence {
            sand(sandPoint, map).also { map[it.row][it.col] = 'o'  }
        }
            .takeWhile { map[sandPoint.row][sandPoint.col] != 'o' }
            .count() + 1
    }

    private fun sand(from: Point, map: Array<CharArray>): Point {
        return generateSequence(from) { restPoint(it, map) }
            .zipWithNext()
            .takeWhile { (prev, cur) -> prev != cur }
            .lastOrNull()
            ?.second ?: from
    }

    private fun restPoint(from: Point, map: Array<CharArray>): Point {
        return when {
            map[from.row + 1][from.col] == '.' -> Point(from.row + 1, from.col)
            map[from.row + 1][from.col - 1] == '.' -> Point(from.row + 1, from.col - 1)
            map[from.row + 1][from.col + 1] == '.' -> Point(from.row + 1, from.col + 1)
            else -> from
        }
    }

    private fun parseShapes(): List<Shape> {
        val map = inputList.map { line ->
            line.split(" -> ").map { coord ->
                coord.split(",").let { (col, row) -> Point(row.toInt(), col.toInt()).withMargin(margin) }
            }.let { Shape(it) }
        }
        return map
    }

    private fun safetyNet(a: Array<CharArray>): Shape {
        val (_, toRow, _, _) = a.getBorders("#")
        return Shape(listOf(Point(toRow + 2, 0), Point(toRow + 2, mapSize - 1)))
    }

    private fun printMap(a: Array<CharArray>) {
        val (fromRow, toRow, fromCol, toCol) = a.getBorders("#o+")
        for (row in fromRow..toRow) {
            for (col in fromCol..toCol) {
                print(a[row][col])
            }
            println()
        }
    }

    private fun Array<CharArray>.getBorders(charsConsidered: String): List<Int> {
        val considered = charsConsidered.toSet()
        val (fromRow, toRow) = this.withIndex().mapNotNull { (index, row) -> if (row.any { it in considered }) index else null }
            .let { rows -> rows.minOf { it } to rows.maxOf { it } }
        val (fromCol, toCol) = this.flatMap { row ->
            row.withIndex().mapNotNull { (index, c) -> if (c in considered) index else null }
        }
            .let { cols -> cols.minOf { it } to cols.maxOf { it } }

        return listOf(fromRow, toRow, fromCol, toCol)
    }

    data class Point(val row: Int, val col: Int) {
        fun withMargin(margin: Int) = Point(row + margin, col + margin)
    }

    data class Shape(val points: List<Point>) : List<Point> by points {
        fun traceOnMap(a: Array<CharArray>, withChar: Char = '#') {
            points.zipWithNext { from, to ->
                for (row in IntProgression.fromClosedRange(
                    from.row, to.row, if (from.row < to.row) 1 else -1
                )) {
                    for (col in IntProgression.fromClosedRange(
                        from.col, to.col, if (from.col < to.col) 1 else -1
                    )) {
                        a[row][col] = withChar
                    }
                }
            }
        }
    }
}

