package days

class Day10 : Day(10) {

    private val xSequence = sequence {
        var v = 1
        yield(v)
        inputList.forEach {
            when (it) {
                "noop" -> yield(v)
                else -> {
                    yield(v)
                    v += it.split(" ")[1].toInt()
                    yield(v)
                }
            }
        }
    }

    override fun partOne(): Any {
        return xSequence.take(220).toList()
            .let { xValues ->
                IntProgression.fromClosedRange(20, 220, 40)
                    .sumOf { it * xValues[it - 1] }
            }
    }

    override fun partTwo(): Any {
        xSequence.take(240).toList().withIndex()
            .map { (index, x) ->
                if (kotlin.math.abs(index % 40 - x) < 2) "#" else '.'
            }.chunked(40)
            .map { it.joinToString("") }
            .forEach(::println)

        return "FCJAPJRE"
    }
}

