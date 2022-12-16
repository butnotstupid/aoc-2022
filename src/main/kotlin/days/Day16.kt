package days

class Day16 : Day(16) {
    private val valves: Map<String, Valve> = parseValves()

    override fun partOne(): Any {
        return maxPressure(listOf(valves.getValue("AA")), 30)
    }

    override fun partTwo(): Any {
        return maxPressure(listOf(valves.getValue("AA"), valves.getValue("AA")), 26)
    }

    private fun maxPressure(
        startPoints: List<Valve>,
        startTimeLeft: Int
    ): Int {
        // [0] for states at T, [1] for states at T-1
        val visited = Array(2) { mutableSetOf<State>() }
        val finScores = mutableListOf<State>()
        var q = ArrayDeque<State>().apply {
            State(startPoints, emptyList(), 0, 0, startTimeLeft).let { add(it); visited[0].add(it) }
        }
        var curTimeLeft = startTimeLeft

        while (q.isNotEmpty()) {
            // cut down to the top scored, if overpopulated
            q = cutQueue(q)
            val state = q.removeFirst()
            if (state.timeLeft == 0) {
                finScores.add(state)
            } else with(state) {
                if (timeLeft < curTimeLeft) {
                    visited[0] = visited[1]
                    visited[1].clear()
                    curTimeLeft = timeLeft
                }
                toMove.flatMapIndexed { curInd, cur ->
                    val without = toMove.filterIndexed { index, _ -> index != curInd }
                    // Try going to the connected valves
                    cur.connected.map {

                        // with same [time] left if the group move is in progress
                        if (without.isNotEmpty()) State(
                            without, moved.plus(valves.getValue(it)), opened, score, timeLeft
                        ).apply { prev = state }

                        // with [time-1] if it completes the group move
                        else State(
                            moved.plus(valves.getValue(it)), emptyList(), opened, score, timeLeft - 1
                        ).apply { prev = state }

                    // Try opening the current valve
                    }.plus(
                        if (cur.name.let { valves.keys.indexOf(it) } !in opened && cur.rate > 0) {
                            // same idea with the group move and [time] and [time-1] left
                            if (without.isNotEmpty())
                                State(
                                    without,
                                    moved.plus(cur),
                                    opened.addBitAt(cur.name.let { valves.keys.indexOf(it) }),
                                    score + (timeLeft - 1) * cur.rate,
                                    timeLeft
                                ).apply { prev = state }
                            else
                                State(
                                    moved.plus(cur),
                                    emptyList(),
                                    opened.addBitAt(cur.name.let { valves.keys.indexOf(it) }),
                                    score + (timeLeft - 1) * cur.rate,
                                    timeLeft - 1
                                ).apply { prev = state }
                        } else null
                    )
                }
                    .filterNotNull()
                    .forEach {
                        // update the visited sets (could only be [time] and [time-1])
                        if (it !in visited[timeLeft - it.timeLeft]) {
                            visited[timeLeft - it.timeLeft].add(it)
                            q.add(it)
                        }
                    }
            }
        }

//        // print path
//        generateSequence(finScores.maxByOrNull { it.score }) { it.prev }
//            .takeWhile { true }.map { it.toMove.first().name }
//            .toList().reversed().let { println(it) }

        return finScores.maxOf { it.score }
    }

    private fun parseValves() = inputList.map { line ->
        line.split(";").let { (left, right) ->
            val (name, rate) = "Valve ([A-Z]*) has flow rate=(\\d*)".toRegex().find(left)
                ?.destructured?.let { (name, rateStr) -> name to rateStr.toInt() }
                ?: error("Failed to parse input $left")
            val connectedValves =
                "tunnel[s]? lead[s]? to valve[s]? (.*)".toRegex().find(right)?.destructured?.component1()
                    ?.split(", ") ?: error("Failed to parse input $right")
            Valve(name, rate, connectedValves)
        }
    }.associateBy { it.name }

    private fun cutQueue(q: ArrayDeque<State>, cap: Int = 100_000, takeTop: Double = 0.2): ArrayDeque<State> =
        if (q.size > cap) ArrayDeque(q.sortedByDescending { it.score }.take((cap * takeTop).toInt()))
        else q

    data class Valve(val name: String, var rate: Int, val connected: List<String>)
    data class State(
        val toMove: List<Valve>,
        val moved: List<Valve>,
        val opened: Long,
        val score: Int,
        val timeLeft: Int
    ) {
        var prev: State? = null
    }

    private operator fun Long.contains(index: Int): Boolean {
        return this and (1L shl index) != 0L
    }

    private fun Long.addBitAt(index: Int): Long {
        return this or (1L shl index)
    }
}
