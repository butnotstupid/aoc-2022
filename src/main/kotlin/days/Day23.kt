package days

class Day23 : Day(23) {
    override fun partOne(): Any {
        return elvesRounds()
            .take(10).last()
            .let(::countEmptyGround)
    }

    override fun partTwo(): Any {
        return elvesRounds()
            .zipWithNext()
            .takeWhile { (prev, cur) -> prev != cur }
            .count() + 1
    }

    private fun elvesRounds(): Sequence<Set<Elf>> {
        val elvesStart = inputList.flatMapIndexed { row, str ->
            str.mapIndexedNotNull { col, charAt ->
                if (charAt == '#') Elf(Point(row, col)) else null
            }
        }.toSet()

        return generateSequence(elvesStart to INIT_DIRECTIONS) { (elves, dir) ->
            val proposed = elves.associateWith { it.propose(elves, dir.toNewIterator()) }
            val propGroup = proposed.values.groupBy { it.position }
            dir.next()
            proposed.map { (from, to) ->
                if (propGroup.getValue(to.position).size == 1) to else from
            }.toSet() to dir.toNewIterator()
        }.map { it.first }
    }

    private fun countEmptyGround(elves: Set<Elf>): Int {
        val rows = elves.minOf { it.position.row } .. elves.maxOf { it.position.row }
        val cols = elves.minOf { it.position.col } .. elves.maxOf { it.position.col }
        val positions = elves.map { it.position }.toSet()
        return rows.sumOf { row ->
            cols.count { col -> Point(row, col) !in positions }
        }
    }

    data class Elf(val position: Point) {
        private val adjPoints = listOf(
            Point(-1, -1), Point(-1, 0), Point(-1, 1),
            Point(0, -1), Point(0, 1),
            Point(1, -1), Point(1, 0), Point(1, 1),
        )

        fun propose(otherElves: Set<Elf>, dir: Iterator<Direction>): Elf {
            val otherPositions = otherElves.map { it.position }.toSet()
            if (adjPoints.all { (this.position + it) !in otherPositions }) return this

            return (1..4).firstNotNullOfOrNull {
                val direction = dir.next()
                if (direction.toCheck.all { (this.position + it) !in otherPositions }) Elf(this.position + direction.next)
                else null
            } ?: this
        }
    }

    data class Point(val row: Int, val col: Int) {
        operator fun plus(other: Point) = Point(this.row + other.row, this.col + other.col)
    }

    enum class Direction(val next: Point, val toCheck: List<Point>) {
        NORTH(Point(-1, 0), listOf(Point(-1, -1), Point(-1, 0), Point(-1, 1))),
        SOUTH(Point(1, 0), listOf(Point(1, -1), Point(1, 0), Point(1, 1))),
        WEST(Point(0, -1), listOf(Point(-1, -1), Point(0, -1), Point(1, -1))),
        EAST(Point(0, 1), listOf(Point(-1, 1), Point(0, 1), Point(1, 1))),
    }

    private val INIT_DIRECTIONS = sequence {
        while (true) { yieldAll(Direction.values().toList()) }
    }.iterator()

    private fun Iterator<Direction>.toNewIterator(): Iterator<Direction> = sequence {
        while (true) {
            yieldAll(listOf(this@toNewIterator.next(), this@toNewIterator.next(), this@toNewIterator.next(), this@toNewIterator.next()))
        }
    }.iterator()
}
