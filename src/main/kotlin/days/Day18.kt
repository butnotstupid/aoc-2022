package days

import java.lang.Integer.max
import kotlin.math.abs

class Day18 : Day(18) {
    override fun partOne(): Any {
        val cubes = inputList.map {
            it.split(",").map { it.toInt() }.let { Cube(it[0], it[1], it[2]) }
        }
        val touchingSurfaces = cubes.flatMap { left -> cubes.mapNotNull { right -> if (left != right) left to right else null } }
            .count { (one, another) ->
                (one.x == another.x && one.y == another.y && abs(one.z - another.z) == 1) ||
                (one.y == another.y && one.z == another.z && abs(one.x - another.x) == 1) ||
                (one.z == another.z && one.x == another.x && abs(one.y - another.y) == 1)
            }
        return 6 * cubes.size - touchingSurfaces
    }

    override fun partTwo(): Any {
        val margin = 2
        val cubes = inputList.map {
            it.split(",").map { it.toInt() }.let { Cube(it[0] + margin, it[1] + margin, it[2] + margin) }
        }.toSet()
        val outerSurface = cubes.maxOf { max(max(it.x, it.y), it.z) } + margin

        return cubes.flatMap { cube ->
            cube.next().filter { it !in cubes }
        }.count { reachSurface(it, cubes, outerSurface) }
    }

    private fun reachSurface(start: Cube, cubes: Set<Cube>, outerSurface: Int): Boolean {
        val q = ArrayDeque<Cube>().apply { add(start) }
        val visited = mutableSetOf(start)
        while (q.isNotEmpty()) {
            val cur = q.removeFirst()
            if (cur.x == outerSurface || cur.y == outerSurface || cur.z == outerSurface) {
                return true
            } else {
                val next = cur.next()
                next.filter { it !in cubes && it !in visited }.let { visited.addAll(it); q.addAll(it) }
            }
        }
        return false
    }

    data class Cube(val x: Int, val y:Int, val z:Int) {
        fun next(): List<Cube> {
            return listOf(
                this.copy(x = this.x - 1),
                this.copy(x = this.x + 1),
                this.copy(y = this.y + 1),
                this.copy(y = this.y - 1),
                this.copy(z = this.z + 1),
                this.copy(z = this.z - 1),
            )
        }
    }
}
