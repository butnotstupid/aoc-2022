package days

import java.lang.IllegalStateException

class Day25 : Day(25) {
    override fun partOne(): Any {
        val dec = inputList.map { it.toDecimal() }.sum()
        return dec.toSNAFU()
    }

    override fun partTwo(): Any {
        return "**"
    }

    private fun String.toDecimal() = this.reversed().asSequence().zip(generateSequence(1L) { it * 5 }) { char, mult ->
        mult * when (char) {
            '=' -> -2
            '-' -> -1
            '0' -> 0
            '1' -> 1
            '2' -> 2
            else -> throw IllegalArgumentException("Unknown char $char in number $this")
        }
    }.sum()

    private fun Long.toSNAFU(): String {
        if (this == 0L) return "0"

        return generateSequence(this) { it / 5 }
            .takeWhile { it > 0 }
            .map { it % 5 }
            .fold("" to 0) { (suffix, plus), rem ->
                when (rem + plus) {
                    in 0..2 -> (rem + plus).toString() + suffix to 0
                    3L -> "=$suffix" to 1
                    4L -> "-$suffix" to 1
                    5L -> "0$suffix" to 1
                    else -> throw IllegalStateException("Unexpected rem+plus=${rem + plus}")
                }
            }
            .let { (suffix, plus) -> if (plus == 1) "1$suffix" else suffix }
    }
}
