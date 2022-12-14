package days

class Day13 : Day(13) {

    override fun partOne(): Any {
        return inputList.chunked(3).withIndex().mapNotNull { (index, lines) ->
            val (left, right) = parseLine(lines[0]) to parseLine(lines[1])
            if (left < right) index + 1 else null
        }.sum()
    }

    override fun partTwo(): Any {
        return inputList.chunked(3).flatMap { lines ->
            val (left, right) = parseLine(lines[0]) to parseLine(lines[1])
            listOf(left, right)
        }.let { packets ->
            (packets.count { it < parseLine("[[2]]")} + 1) * (packets.count { it < parseLine("[[6]]")} + 2)
        }
    }

    private fun parseLine(s: String): Packet {
        val root = Packet()
        var currentPacket = root
        s.substring(1, s.length - 1).forEachIndexed { index, charAt ->
            when (charAt) {
                '[' -> {
                    currentPacket = Packet(parent = currentPacket).also { currentPacket.child.add(it) }
                }
                ',' -> {
                    if (currentPacket.value == null && currentPacket.child.isEmpty()) {
                        Packet(value = - 1, parent = currentPacket).also { currentPacket.child.add(it) }
                    } else currentPacket = currentPacket.parent!!
                }
                ']' -> {
                    if (currentPacket.value == null && currentPacket.child.isEmpty()) {
                        Packet(value = - 1, parent = currentPacket).also { currentPacket.child.add(it) }
                    } else currentPacket = currentPacket.parent!!
                }
                in '0'..'9' -> {
                    if (currentPacket.value == null) {
                        currentPacket = Packet(parent = currentPacket).also { currentPacket.child.add(it) }
                    }
                    currentPacket.value = (currentPacket.value ?: 0) * 10 + charAt.code - '0'.code
                }
                else -> throw IllegalStateException("Unknown input char: $charAt")
            }
        }
        return root
    }

    data class Packet(var value: Int? = null, var child: MutableList<Packet> = mutableListOf(), val parent: Packet? = null) {
        operator fun compareTo(other: Packet): Int {
            return when {
                this.value != null && other.value != null -> this.value!!.compareTo(other.value!!)
                this.value == null && other.value == null -> {
                    this.child.zip(other.child).firstNotNullOfOrNull { (left, right) ->
                        if (left.compareTo(right) == 0) null
                        else left.compareTo(right)
                    } ?: this.child.size.compareTo(other.child.size)
                }
                else -> {
                    val wrap = Packet()
                    when {
                        this.value != null -> {
                            this.copy(parent = wrap).also { wrap.child.add(it) }
                            wrap.compareTo(other)
                        }
                        else -> {
                            other.copy(parent = wrap).also { wrap.child.add(it) }
                            this.compareTo(wrap)
                        }
                    }
                }
            }

        }

        override fun toString(): String {
            return if (value != null) "${if (value == -1) "" else value}"
            else child.map { it.toString() }.joinToString(",").let { "[$it]" }
        }
    }
}

