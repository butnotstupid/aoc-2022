package days

import java.lang.Integer.max
import kotlin.reflect.full.primaryConstructor

class Day17 : Day(17) {
    override fun partOne(): Any {
        return topRowDiffs(2022).sum()
    }

    override fun partTwo(): Any {
        val totalSize = 1_000_000_000_000
        val headSize = 10000
        val tailSize = totalSize - headSize
        val sampleSize = 5145

        val headChunk = topRowDiffs(headSize)
        val sample = headChunk.takeLast(sampleSize)

        return headChunk.sumOf { it } +
                sample.sum() * (tailSize / sampleSize) +
                sample.take(((tailSize % sampleSize).toInt())).sum()
    }

    private fun topRowDiffs(rocksFallen: Int): List<Int> {
        val map = Array(20000) { "|.......|".toCharArray() }.also { it[0] = "+-------+".toCharArray() }
        val winds = Cycled(inputList.first().toList())
        val shapes = sequence {
            while (true) yieldAll(
                listOf(
                    HorizontalBar::class, Cross::class, Corner::class, VerticalBar::class, Square::class
                )
            )
        }.iterator()

        var topRow = 0
        return (1..rocksFallen).map {
            val nextShape = shapes.next()
            val shapeBuilder: (Point) -> Shape = { nextShape.primaryConstructor!!.call(it) }
            val restPoint = fall(Point(topRow + 4, 3), shapeBuilder, map, winds)
            // mark # for the shape for the rest point
            val fallenShape = shapeBuilder(restPoint)
            fallenShape.mark(map)
            val diff = max(topRow, fallenShape.top.row) - topRow
            topRow += diff
            diff
        }
    }

    private fun printMap(map: Array<CharArray>, topRow: Int) {
        map.take(topRow + 1).reversed().forEach {
            println(it.joinToString(""))
        }
        println()
    }

    private fun fall(from: Point, shape: (Point) -> Shape, map: Array<CharArray>, winds: Cycled<Char>): Point {
        var cur = from
        do {
            val prev = cur
            cur = nextPoint(cur, shape, map, winds)
        } while (cur.row != prev.row)
        return cur
    }

    private fun nextPoint(
        from: Point,
        shape: (Point) -> Shape,
        map: Array<CharArray>,
        winds: Cycled<Char>,
    ): Point {
        val newCol = when (val wind = winds.next()) {
            '>' -> {
                if (shape(Point(from.row, from.col + 1)).fit(map)) from.col + 1 else from.col
            }
            '<' -> {
                if (shape(Point(from.row, from.col - 1)).fit(map)) from.col - 1 else from.col
            }
            else -> throw IllegalArgumentException("Unknown wind direction $wind")
        }
        val newRow = if (shape(Point(from.row - 1, newCol)).fit(map)) from.row - 1 else from.row
        return Point(newRow, newCol)
    }

    // Each Shape has anchor point (x/X): left-bottom edge (might be outside of the shape)
    // X###    .#.    ..#    #    ##
    //         ###    ..#    #    X#
    //         x#.    X##    #
    //                       X

    abstract class Shape(val anchor: Point) {
        abstract val relPoints: List<Point>
        abstract val top: Point

        fun fit(map: Array<CharArray>): Boolean {
            return relPoints.all { (dr, dc) ->
                anchor.row + dr > 0 && anchor.col + dc in 0 until map.first().size && map[anchor.row + dr][anchor.col + dc] == '.'
            }
        }
        fun mark(map: Array<CharArray>) {
            relPoints.forEach { (dr, dc) -> map[anchor.row + dr][anchor.col + dc] = '#' }
        }
    }

    class HorizontalBar(anchor: Point) : Shape(anchor) {
        override val relPoints = listOf(
            Point(0, 0), Point(0, 1), Point(0, 2), Point(0, 3),
        )
        override val top: Point = anchor
    }

    class Cross(anchor: Point) : Shape(anchor) {
        override val relPoints = listOf(
            Point(0, 1), Point(1, 0), Point(1, 1), Point(1, 2), Point(2, 1),
        )
        override val top: Point = Point(anchor.row + 2, anchor.col)
    }

    class Corner(anchor: Point) : Shape(anchor) {
        override val relPoints = listOf(
            Point(0, 0), Point(0, 1), Point(0, 2), Point(1, 2), Point(2, 2),
        )
        override val top: Point = Point(anchor.row + 2, anchor.col)
    }

    class VerticalBar(anchor: Point) : Shape(anchor) {
        override val relPoints = listOf(
            Point(0, 0), Point(1, 0), Point(2, 0), Point(3, 0),
        )
        override val top: Point = Point(anchor.row + 3, anchor.col)
    }

    class Square(anchor: Point) : Shape(anchor) {
        override val relPoints = listOf(
            Point(0, 0), Point(0, 1), Point(1, 0), Point(1, 1),
        )
        override val top: Point = Point(anchor.row + 1, anchor.col)
    }

    data class Point(val row: Int, val col: Int)

    data class Cycled <T>(private val content: List<T>) {
        var curIndex = 0
            private set

        fun next(): T {
            return content[curIndex++ % content.size]
        }
        fun stepBack(steps: Int = 1) {
            assert(steps > 0) { "Cannot step back negative steps: $steps" }
            curIndex -= steps
        }
    }
}
