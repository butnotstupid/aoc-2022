package days

import kotlin.math.sign

class Day9 : Day(9) {

    override fun partOne(): Any = calculateNextKnotVisits(inputList.map { it.split(" ") }
        .map { Configuration.fromCode(it[0]) to it[1].toInt() }).toSet().size

    override fun partTwo(): Any =
        generateSequence(inputList.map { it.split(" ") }.map { Configuration.fromCode(it[0]) to it[1].toInt() }) {
            generateNextKnotMoves(it)
        }.take(9).last().let { calculateNextKnotVisits(it) }.toSet().size

    private fun move(rope: Rope, dir: Configuration): Rope {
        val (newR, newC) = dir.let { rope.conf.dr + it.dr to rope.conf.dc + it.dc }
        return if (newR in -1..1 && newC in -1..1) {
            rope.copy(conf = Configuration.values().find { it.dr == newR && it.dc == newC }!!)
        } else {
            Rope(
                pos = rope.pos.first + newR.sign to rope.pos.second + newC.sign,
                conf = Configuration.fromDelta(newR - newR.sign to newC - newC.sign)
            )
        }
    }

    private fun calculateNextKnotVisits(input: List<Pair<Configuration, Int>>): Sequence<Pair<Int, Int>> = sequence {
        var state = Rope(0 to 0, Configuration.CENTER)
        yield(state)
        input.forEach { (command, steps) ->
            repeat(steps) {
                state = move(state, command)
                yield(state)
            }
        }
    }.map { it.pos }

    private fun generateNextKnotMoves(input: List<Pair<Configuration, Int>>): List<Pair<Configuration, Int>> {
        return sequence {
            var state = Rope(0 to 0, Configuration.CENTER)
            input.forEach { (command, steps) ->
                repeat(steps) {
                    val newState = move(state, command)
                    val nextConf = Configuration.fromDelta(newState.pos.first - state.pos.first to newState.pos.second - state.pos.second)
                    yield(nextConf to 1)
                    state = newState
                }
            }
        }.toList()
    }

    /**
     *  Tail is in the middle at position `pos`, head is in `conf` configuration
     *      UL U UR
     *      L  C  R
     *      DL D DR
     */
    data class Rope(val pos: Pair<Int, Int>, val conf: Configuration)

    enum class Configuration(val dr: Int, val dc: Int) {
        UP_LEFT(-1,-1), UP(-1,0), UP_RIGHT(-1, 1),
        LEFT(0, -1), CENTER(0,0), RIGHT(0, 1),
        DOWN_RIGHT(1,1), DOWN(1, 0), DOWN_LEFT(1, -1);

        companion object {
            fun fromDelta(delta: Pair<Int, Int>) =
                values().find { it.dr == delta.first && it.dc == delta.second }!!

            fun fromCode(code: String) = when (code) {
                "R" -> RIGHT
                "L" -> LEFT
                "U" -> UP
                "D" -> DOWN
                else -> throw IllegalArgumentException("Unknown move code $code")
            }
        }
    }
}

