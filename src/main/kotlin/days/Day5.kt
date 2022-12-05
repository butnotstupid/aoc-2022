package days

class Day5 : Day(5) {

    override fun partOne(): Any {
        val mapState = parseMap().map { it.toMutableList() }

        parseCommands().forEach { (number, from, to) ->
            repeat(number) {
                mapState[to - 1].add(0, mapState[from - 1].first())
                mapState[from - 1].removeFirst()
            }
        }

        return mapState.map { it.first() }.joinToString("")
    }

    override fun partTwo(): Any {
        val mapState = parseMap().map { it.toMutableList() }

        parseCommands().forEach { (number, from, to) ->
            mapState[to - 1].addAll(0, mapState[from - 1].subList(0, number))
            repeat(number) { mapState[from - 1].removeFirst() }
        }

        return mapState.map { it.first() }.joinToString("")
    }

    private fun parseMap(): List<List<Char>> = inputList
        .takeWhile { !it.startsWith(" 1") }
        .map { row ->
            row.chunked(4)
                .map { it[1] }
                .withIndex()
                .filter { it.value != ' ' }
                .map { it.value to it.index }
        }
        .flatten()
        .groupBy { it.second }
        .mapValues { (_, value) -> value.map { it.first } }
        .toSortedMap().values
        .toList()

    private fun parseCommands(): List<Command> = inputList
        .dropWhile { !it.startsWith("move") }
        .map { commandRaw ->
            inputRegex
                .matchEntire(commandRaw)!!.groupValues.drop(1)
                .map { it.toInt() }
        }
        .map { (number, from, to) -> Command(number, from, to) }


    data class Command(val number: Int, val from: Int, val to: Int)

    private companion object {
        private val inputRegex = "move (\\d*) from (\\d*) to (\\d*)".toRegex()
    }
}
