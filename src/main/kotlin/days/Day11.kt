package days

class Day11 : Day(11) {

    override fun partOne(): Any {
        val monkeys = parseMonkeys().map { monkeyPartTwo ->
            MonkeyPartOne(
                monkeyPartTwo.id,
                monkeyPartTwo.items.map { it.entries.first().value }.let { ArrayDeque(it) },
                monkeyPartTwo.operation,
                monkeyPartTwo.test
            )
        }

        repeat(20) {
            monkeys.forEach { monkey ->
                monkey.pass(3).forEach { (item, to) -> monkeys[to].receive(item) }
            }
        }

        return monkeys.sortedByDescending { it.inspections }.take(2)
            .let { (top, second) -> top.inspections * second.inspections }
    }

    override fun partTwo(): Any {
        val monkeys = parseMonkeys()

        repeat(10_000) {
            monkeys.forEach { monkey ->
                monkey.pass().forEach { (item, to) -> monkeys[to].receive(item) }
            }
        }

        return monkeys.sortedByDescending { it.inspections }.take(2)
            .let { (top, second) -> top.inspections * second.inspections }
    }

    data class MonkeyPartOne(val id: Int, val items: ArrayDeque<Long>, val operation: (Long) -> Long, val test: Test) {
        var inspections: Long = 0
            private set

        fun pass(worryFactor: Int): List<Pair<Long, Int>> = items.map { item ->
            inspections++
            val newItem = operation(item) / worryFactor
            newItem to
                    if (newItem % test.divider == 0L) test.ifTrueTo
                    else test.ifFalseTo
        }.also { items.clear() }

        fun receive(item: Long) {
            items.addLast(item)
        }
    }

    data class MonkeyPartTwo(val id: Int, val items: ArrayDeque<Item>, val operation: (Long) -> Long, val test: Test) {
        var inspections: Long = 0
            private set

        fun pass(): List<Pair<Item, Int>> = items.map { item ->
            inspections++
            val newItem = Item(item.value.mapValues { (divider, value) -> (operation(value)) % divider })
            newItem to
                    if (newItem.getValue(test.divider) % test.divider == 0L) test.ifTrueTo
                    else test.ifFalseTo
        }.also { items.clear() }

        fun receive(item: Item) {
            items.addLast(item)
        }
    }

    data class Test(val divider: Int, val ifTrueTo: Int, val ifFalseTo: Int)

    data class Item(val value: Map<Int, Long>) : Map<Int, Long> by value {
        companion object {
            fun init(initValue: Long, dividers: List<Int>): Item {
                return Item(dividers.associateWith { initValue })
            }
        }
    }

    private fun parseMonkeys(): Array<MonkeyPartTwo> {
        val monkeyBlocks = inputList.map(String::trim).chunked(7)
        val dividers = monkeyBlocks.map { it[3].removePrefix("Test: divisible by ").toInt() }

        return monkeyBlocks.map { monkeyList ->
            val monkeyId = monkeyList[0].removePrefix("Monkey ").dropLast(1).toInt()
            MonkeyPartTwo(
                id = monkeyId,
                items = monkeyList[1].removePrefix("Starting items: ")
                    .split(", ")
                    .map { Item.init(it.toLong(), dividers) }
                    .let(::ArrayDeque),
                operation = monkeyList[2].removePrefix("Operation: new = ")
                    .split(" ")
                    .let { (left, opString, right) ->
                        val operand: (Long, Long) -> Long = when (opString) {
                            "+" -> Long::plus
                            "*" -> Long::times
                            else -> throw IllegalArgumentException("Unsupported operand: $opString")
                        }

                        { x: Long ->
                            operand(
                                (if (left == "old") x else left.toLong()),
                                (if (right == "old") x else right.toLong())
                            )
                        }
                    },
                test = Test(
                    divider = dividers[monkeyId],
                    ifTrueTo = monkeyList[4].removePrefix("If true: throw to monkey ").toInt(),
                    ifFalseTo = monkeyList[5].removePrefix("If false: throw to monkey ").toInt()
                )
            )
        }.toTypedArray()
    }
}

