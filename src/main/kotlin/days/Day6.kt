package days

class Day6 : Day(6) {

    override fun partOne(): Any {
        return firstEndOfUniqueWindow(inputList.first(), 4)
    }

    override fun partTwo(): Any {
        return firstEndOfUniqueWindow(inputList.first(), 14)
    }

    private fun firstEndOfUniqueWindow(message: String, windowSize: Int) = message
        .windowed(windowSize)
        .withIndex()
        .first { it.value.toSet().size == windowSize }
        .index + windowSize
}
