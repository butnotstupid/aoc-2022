package days

class Day4 : Day(4) {

    override fun partOne(): Any {
        return inputList.count { assignment ->
            val (first, second) = parseAssignment(assignment)
            first in second || second in first
        }
    }

    override fun partTwo(): Any {
        return inputList.count { assignment ->
            val (first, second) = parseAssignment(assignment)
            first overlaps second
        }
    }

    private fun parseAssignment(assignment: String) =
        inputRegex.matchEntire(assignment)!!.groupValues.drop(1)
            .map { it.toInt() }
            .chunked(2)
            .map { (from, to) -> from..to }

    operator fun IntRange.contains(other: IntRange): Boolean = this.first <= other.first && other.last <= this.last

    private infix fun IntRange.overlaps(other: IntRange): Boolean {
        return when {
            other.first in first..last -> true
            first in other.first..other.last -> true
            else -> false
        }
    }

    private companion object {
        private val inputRegex = "(\\d*)-(\\d*),(\\d*)-(\\d*)".toRegex()
    }
}
