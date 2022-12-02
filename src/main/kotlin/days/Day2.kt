package days

class Day2 : Day(2) {

    // A < B < C < A
    // X < Y < Z < X
    private val winsTo = mapOf('A' to 'B', 'B' to 'C', 'C' to 'A')
    private val loseTo = mapOf('B' to 'A', 'C' to 'B', 'A' to 'C')

    override fun partOne(): Any {
        return inputList.sumOf {
            val (one, another) = it[0] to Char('A'.code + it[2].code - 'X'.code)
            val winPoints = when {
                winsTo[one] == another -> 6
                one == another -> 3
                else -> 0
            }

            another.points() + winPoints
        }
    }

    override fun partTwo(): Any {
        return inputList.sumOf {
            val (one, result) = it[0] to it[2]
            when (result) {
                'X' -> 0 + loseTo.getValue(one).points()
                'Y' -> 3 + one.points()
                'Z' -> 6 + winsTo.getValue(one).points()
                else -> error("Unexpected input $it")
            }
        }
    }

    private fun Char.points() = 1 + this.code - 'A'.code
}
