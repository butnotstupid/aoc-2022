package days

import kotlin.collections.ArrayDeque

class Day12 : Day(12) {

    override fun partOne(): Any {
        val input = parseInput()
        val height = input.height
        val (s, e) = input.start to input.end

        return findClosestPath(s, e, height) ?: throw IllegalStateException("Couldn't find a way from $s to $e")
    }

    override fun partTwo(): Any {
        val input = parseInput()
        val height = input.height
        val e = input.end

        return height.findAll('a').mapNotNull { findClosestPath(it, e, height) }.minOf { it }
    }

    private fun Array<CharArray>.findAll(c: Char): List<Point> {
        return this.withIndex().flatMap { (row, line) ->
            line.withIndex().mapNotNull{ (col, charAt) ->
                if (charAt == c) Point(row, col) else null
            }
        }
    }

    private fun findClosestPath(from: Point, to: Point, height: Array<CharArray>): Int? {
        val (rows, cols) = height.size to height.first().size
        val queue = ArrayDeque<Pair<Point, Int>>().apply { add(from to 0) }
        val visited = mutableSetOf<Point>().apply { add(from) }
        while (queue.isNotEmpty()) {
            val (cur, dist) = queue.removeFirst()
            if (cur == to) {
                return dist
            } else {
                cur.safeNeighbours(rows, cols).forEach { next ->
                    if (isClimbable((height at cur) to (height at next)) && next !in visited) {
                        queue.add(next to dist + 1)
                        visited.add(next)
                    }
                }
            }
        }

        return null
    }

    private fun parseInput(): Input {
        lateinit var s: Point
        lateinit var e: Point
        val height = inputList.mapIndexed { row, line ->
            line.toCharArray().also {
                line.withIndex().find { (_, c) -> c == 'S' }?.let { s = Point(row, it.index) }
                line.withIndex().find { (_, c) -> c == 'E' }?.let { e = Point(row, it.index) }
            }
        }.toTypedArray().also {
            it[s.row][s.col] = 'a'
            it[e.row][e.col] = 'z'
        }

        return Input(s, e, height)
    }

    private fun isClimbable(fromTo: Pair<Char, Char>) = fromTo.let { (from, to) -> from >= to || from + 1 == to }

    data class Point(val row: Int, val col: Int) {
        fun safeNeighbours(rows: Int, cols: Int): List<Point> {
            val dr = listOf(-1, 0, 1 ,0)
            val dc = listOf(0, 1, 0 ,-1)
            return dr.zip(dc).mapNotNull { (dr, dc) ->
                if (row + dr in 0 until rows && col + dc in 0 until cols) Point(row + dr, col + dc)
                else null
            }
        }
    }

    class Input(val start: Point, val end: Point, val height: Array<CharArray>)

    private infix fun Array<CharArray>.at(point: Point) = this[point.row][point.col]
}

