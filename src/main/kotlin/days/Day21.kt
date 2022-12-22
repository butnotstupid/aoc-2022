package days

typealias Operation = (Day21.LinearFunction, Day21.LinearFunction) -> Day21.LinearFunction

class Day21 : Day(21) {

    override fun partOne(): Any {
        val monkeys = parseMonkeys().associateBy { it.name }
        return monkeys.getValue("root").eval(monkeys).b.toLong()
    }

    override fun partTwo(): Any {
        val monkeys = parseMonkeys()
            .map { if (it.name == "humn") it.copy(value = LinearFunction(1.0, 0.0)) else it }
            .associateBy { it.name }

        val (left, right) = monkeys.getValue("root").expr!!
            .let { expr ->
                monkeys.getValue(expr.left).eval(monkeys) to
                monkeys.getValue(expr.right).eval(monkeys)
            }

        val x = (right.b - left.b) / left.a
        return x.toLong()
    }

    private fun parseMonkeys() = inputList.map {
        it.split(": ").let { (key, value) ->
            if (value.all(Char::isDigit)) Monkey(key, value.toLong().toLinearFunction())
            else {
                value.split(" ").let { (left, op, right) ->
                    val operation: Operation = when (op.first()) {
                        '+' -> { a, b -> a + b }
                        '-' -> { a, b -> a - b }
                        '*' -> { a, b -> a * b }
                        '/' -> { a, b -> a / b }
                        else -> throw IllegalArgumentException("Unknown operation: ${op.first()}")
                    }
                    Monkey(key, null, Expression(left, right, operation))
                }
            }
        }
    }

    data class Monkey(val name: String, var value: LinearFunction? = null, val expr: Expression? = null) {
        fun eval(others: Map<String, Monkey>): LinearFunction {
            if (value == null) {
                with (expr!!) {
                    value = operation(others.getValue(left).eval(others),others.getValue(right).eval(others))
                }
            }
            return value!!
        }
    }
    data class Expression(val left: String, val right: String, val operation: Operation)

    // Linear function: ax + b
    data class LinearFunction(val a: Double, val b: Double) {
        operator fun plus(other: LinearFunction) = LinearFunction(this.a + other.a, this.b + other.b)
        operator fun minus(other: LinearFunction) = LinearFunction(this.a - other.a, this.b - other.b)
        operator fun times(other: LinearFunction): LinearFunction {
            assert(this.a.compareTo(0) == 0 || other.a.compareTo(0) == 0) { "$this * ${other}: function should stay linear, otherwise not supported" }
            return LinearFunction(
                this.a * other.b + this.b * other.a, this.b * other.b
            )
        }
        operator fun div(other: LinearFunction): LinearFunction {
            assert(other.a.compareTo(0) == 0) { "$this / ${other}: function should stay linear, otherwise not supported" }
            return LinearFunction(this.a / other.b, this.b / other.b)
        }
    }
    private fun Long.toLinearFunction() = LinearFunction(0.0, this.toDouble())
}
