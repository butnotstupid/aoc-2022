package days

class Day20 : Day(20) {
    override fun partOne(): Any {
        val numbers = parseNumbers()
        mixNumbers(numbers)
        return generateSequence(numbers.find { it.value == 0L }) { it.next }.take(numbers.size).map { it.value }.toList()
            .let { it[1000 % numbers.size] + it[2000 % numbers.size] + it[3000 % numbers.size] }
    }

    override fun partTwo(): Any {
        val numbers = parseNumbers(811589153)
        mixNumbers(numbers, 10)
        return generateSequence(numbers.find { it.value == 0L }) { it.next }.take(numbers.size).map { it.value }.toList()
            .let { it[1000 % numbers.size] + it[2000 % numbers.size] + it[3000 % numbers.size] }
    }

    private fun parseNumbers(decryptionKey: Long = 1): List<Num> {
        val numbers = inputList.map {
            Num(it.toLong() * decryptionKey)
        }.also {
            it.zipWithNext { from, to ->
                from.next = to
                to.prev = from
            }
        }
        numbers.first().prev = numbers.last()
        numbers.last().next = numbers.first()
        return numbers
    }

    private fun mixNumbers(numbers: List<Num>, reps: Int = 1) {
        repeat (reps) {
            numbers.forEach { value -> move(value, value.value.toSteps(numbers.size)) }
        }
    }

    private fun move(element: Num, steps: Int) {
        generateSequence(element) { it.next }
            .take(steps + 1)
            .last()
            .let { nextTo ->
                if (nextTo != element) {
                    val a = element.prev!!
                    val b = element.next!!
                    val ntn = nextTo.next!!
                    connect(a, b)
                    connect(nextTo, element)
                    connect(element, ntn)
                }
            }
    }

    private fun connect(from: Num, to: Num) {
        from.next = to
        to.prev = from
    }

    private fun Long.toSteps(size: Int): Int {
        val cycle = size - 1
        return if (this >= 0) (this % cycle).toInt()
        else (this % cycle + cycle).toInt() % cycle
    }


    data class Num(val value: Long, var prev: Num? = null, var next: Num? = null) {
        override fun toString(): String {
            return "Num(value=$value, prev=${prev?.value}, next=${next?.value})"
        }
    }
}
