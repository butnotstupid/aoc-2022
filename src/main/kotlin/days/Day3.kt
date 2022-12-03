package days

import java.lang.IllegalArgumentException

class Day3 : Day(3) {

    override fun partOne(): Any {
        return inputList.sumOf { rucksack ->
            val inter = rucksack
                .chunked(rucksack.length / 2)
                .map(String::toSet)
                .reduce(Set<Char>::intersect)

            assert(inter.size == 1)
            inter.first().priority()
        }
    }

    override fun partTwo(): Any {
        return inputList.chunked(3).sumOf { group ->
            val inter = group
                .map(String::toSet)
                .reduce(Set<Char>::intersect)

            assert(inter.size == 1)
            inter.first().priority()
        }
    }

    private fun Char.priority() = when (this) {
        in 'a'..'z' -> 1 + code - 'a'.code
        in 'A'..'Z' -> 27 + code - 'A'.code
        else -> throw IllegalArgumentException("Unexpected char: $this")
    }
}
